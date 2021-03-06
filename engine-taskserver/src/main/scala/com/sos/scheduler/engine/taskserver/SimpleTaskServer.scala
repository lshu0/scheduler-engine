package com.sos.scheduler.engine.taskserver

import akka.util.ByteString
import com.google.inject.Injector
import com.sos.scheduler.engine.base.process.ProcessSignal
import com.sos.scheduler.engine.base.utils.ScalazStyle.OptionRichBoolean
import com.sos.scheduler.engine.common.scalautil.Closers.implicits._
import com.sos.scheduler.engine.common.scalautil.{HasCloser, Logger}
import com.sos.scheduler.engine.common.tcp.TcpConnection
import com.sos.scheduler.engine.minicom.remoting.{DialogConnection, Remoting}
import com.sos.scheduler.engine.taskserver.SimpleTaskServer._
import com.sos.scheduler.engine.taskserver.configuration.inject.TaskServerModule
import com.sos.scheduler.engine.taskserver.data.TaskStartArguments
import com.sos.scheduler.engine.taskserver.spoolerapi.{ProxySpooler, ProxySpoolerLog, ProxySpoolerTask}
import com.sos.scheduler.engine.taskserver.task.RemoteModuleInstanceServer
import com.sos.scheduler.engine.tunnel.data.TunnelConnectionMessage
import java.nio.channels.AsynchronousCloseException
import scala.concurrent._
import scala.util.{Failure, Success}
import spray.json._

/**
 * A blocking [[TaskServer]], running in a own thread.
 *
 * @author Joacim Zschimmer
 */
final class SimpleTaskServer(injector: Injector, val taskStartArguments: TaskStartArguments, isMain: Boolean = false)(implicit ec: ExecutionContext)
extends TaskServer with HasCloser {

  private val logger = Logger.withPrefix(getClass, taskStartArguments.agentTaskId.toString)
  private lazy val master = TcpConnection.connect(taskStartArguments.masterInetSocketAddress).closeWithCloser
  private val terminatedPromise = Promise[Unit]()
  private val childInjector = injector.createChildInjector(new TaskServerModule(taskStartArguments, taskServerMainTerminated = isMain option terminated))
  private val remoting = new Remoting(
    childInjector,
    new DialogConnection(master),
    IDispatchFactories,
    ProxyIDispatchFactories,
    name = taskStartArguments.agentTaskId.toString,
    returnAfterReleaseOf = _.isInstanceOf[RemoteModuleInstanceServer],
    keepaliveDurationOption = taskStartArguments.rpcKeepaliveDurationOption)

  def terminated = terminatedPromise.future

  def start(): Unit =
    Future {
      blocking {
        val connectionMessage = TunnelConnectionMessage(taskStartArguments.tunnelToken)
        master.sendMessage(ByteString(connectionMessage.toJson.compactPrint))
        remoting.run()
        master.close()
      }
    } onComplete { tried ⇒
      val (correctedTried, msg) = tried match {
        case Failure(t: AsynchronousCloseException) ⇒ (Success(()), s"Terminated after close(): $t")
        case Failure(t: Remoting.ConnectionClosedException) ⇒ (Success(()), s"Terminated, $t")
        case _ ⇒ (tried, Some("Terminated") ++ tried.failed.toOption mkString ", ")
      }
      logger.info(msg)
      terminatedPromise.complete(correctedTried)
    }

  def sendProcessSignal(signal: ProcessSignal) = {
    val signalables = remoteModuleInstanceServers
    logger.debug(s"sendProcessSignal $signal to $signalables")
    signalables foreach { _.sendProcessSignal(signal) }
  }

  def deleteLogFiles() = {}  // Files are closed when master via COM RPC releases RemoteModuleInstanceServer

  override def toString = s"SimpleTaskServer(master=${taskStartArguments.masterAddress})"

  def pidOption = (remoteModuleInstanceServers flatMap { _.pidOption }).headOption

  private def remoteModuleInstanceServers = remoting.iUnknowns[RemoteModuleInstanceServer]  // Should return one
}

object SimpleTaskServer {
  private val IDispatchFactories = List(RemoteModuleInstanceServer)
  private val ProxyIDispatchFactories = List(ProxySpooler, ProxySpoolerLog, ProxySpoolerTask)
}
