package com.sos.scheduler.engine.taskserver

import akka.actor.ActorSystem
import com.google.common.io.{ByteStreams, Closer}
import com.google.inject.Guice
import com.google.inject.Stage._
import com.sos.scheduler.engine.common.commandline.CommandLineArguments
import com.sos.scheduler.engine.common.guice.GuiceImplicits.RichInjector
import com.sos.scheduler.engine.common.scalautil.AutoClosing._
import com.sos.scheduler.engine.common.scalautil.Futures._
import com.sos.scheduler.engine.common.scalautil.Logger
import com.sos.scheduler.engine.common.time.ScalaTime._
import com.sos.scheduler.engine.taskserver.configuration.inject.TaskServerMainModule
import com.sos.scheduler.engine.taskserver.data.TaskStartArguments
import spray.json._

/**
 * @author Joacim Zschimmer
 */
object TaskServerMain {

  private val logger = Logger(getClass)

  def main(args: Array[String]): Unit = {
    CommandLineArguments.parse(args) { _.optionAs[String]("-agent-task-id=") }  // -agent-task-id=.. is only for the kill script and ignored
    try {
      val startArguments = new JsonParser(ByteStreams.toByteArray(System.in)).parseJsValue().asJsObject.convertTo[TaskStartArguments]
      run(startArguments)
      logger.info("Terminating")
    } catch {
      case t: Throwable ⇒
        logger.error(s"$t", t)
        System.err.println(t.toString)
        System.exit(1)
    }
  }

  private def run(startArguments: TaskStartArguments): Unit = {
    val injector = Guice.createInjector(PRODUCTION, new TaskServerMainModule(startArguments.dotnet))
    implicit val executionContext = injector.instance[ActorSystem].dispatcher
    autoClosing(injector.instance[Closer]) { closer ⇒
      autoClosing(new SimpleTaskServer(injector, startArguments, isMain = true)) { taskServer ⇒
        taskServer.start()
        awaitResult(taskServer.terminated, MaxDuration)
      }
    }
  }
}
