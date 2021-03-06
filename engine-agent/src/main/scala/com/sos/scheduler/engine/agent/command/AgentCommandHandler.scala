package com.sos.scheduler.engine.agent.command

import com.sos.scheduler.engine.agent.command.AgentCommandHandler._
import com.sos.scheduler.engine.agent.data.commands._
import com.sos.scheduler.engine.agent.fileordersource.{FileCommandExecutor, RequestFileOrderSourceContentExecutor}
import com.sos.scheduler.engine.agent.task.TaskHandler
import com.sos.scheduler.engine.base.sprayjson.JavaTimeJsonFormats.implicits._
import com.sos.scheduler.engine.common.scalautil.{Logger, ScalaConcurrentHashMap}
import java.time.Instant
import java.time.Instant.now
import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import spray.json.DefaultJsonProtocol._

/**
 * Executes public Agent commands.
 *
 * @author Joacim Zschimmer
 */
@Singleton
final class AgentCommandHandler @Inject private(
  taskHandler: TaskHandler,
  executeRequestFileOrderSourceContent: RequestFileOrderSourceContentExecutor)
  (implicit ec: ExecutionContext)
extends CommandExecutor
with CommandHandlerOverview
with CommandHandlerDetails {

  private val nextId = new AtomicLong(0)
  private val totalCounter = new AtomicInteger(0)
  private val idToCommand = new ScalaConcurrentHashMap[InternalCommandId, CommandRun]

  def totalCommandCount = totalCounter.get
  def currentCommandCount = idToCommand.size
  def commandRuns = (idToCommand.values map { _.overview }).toVector

  def executeCommand(command: Command, meta: CommandMeta): Future[command.Response] = {
    totalCounter.incrementAndGet()
    val id = InternalCommandId(nextId.incrementAndGet())
    logger.info(s"$id ${command.toShortString}")
    if (command.toStringIsLonger) logger.debug(s"$id $command")  // Complete string
    idToCommand += id → CommandRun(id, now(), command)
    val future = executeCommand2(id, command, meta)
    future onComplete { _ ⇒ idToCommand -= id }
    future
  }

  private def executeCommand2(id: InternalCommandId, command: Command, meta: CommandMeta) =
    (command match {
      case command: FileCommand ⇒ Future.successful(FileCommandExecutor.executeCommand(command))
      case command: TaskCommand ⇒ taskHandler.execute(command, meta)
      case command: TerminateOrAbort ⇒ taskHandler.execute(command, meta)
      case command: RequestFileOrderSourceContent ⇒ executeRequestFileOrderSourceContent(command)
    }) map { response ⇒
      logger.debug(s"Response to $id ${command.getClass.getSimpleName}: $response")
      response.asInstanceOf[command.Response]
    }
}

object AgentCommandHandler {
  private val logger = Logger(getClass)

  final case class CommandRun(internalId: InternalCommandId, startedAt: Instant, command: Command) {
    def overview = new CommandRunOverview(internalId, startedAt, command)
  }

  object CommandRun {
    implicit def MyJsonFormat = jsonFormat3(apply)
  }
}
