package com.sos.scheduler.engine.agent.client

import com.google.inject.{AbstractModule, Provides}
import com.sos.scheduler.engine.agent.client.TextAgentClientIT._
import com.sos.scheduler.engine.agent.command.{CommandExecutor, CommandMeta}
import com.sos.scheduler.engine.agent.configuration.AgentConfiguration
import com.sos.scheduler.engine.agent.data.commandresponses.EmptyResponse
import com.sos.scheduler.engine.agent.data.commands.{Command, Terminate}
import com.sos.scheduler.engine.agent.test.{AgentConfigDirectoryProvider, AgentTest}
import com.sos.scheduler.engine.agent.web.auth.SimpleUserPassAuthenticator
import com.sos.scheduler.engine.base.generic.SecretString
import com.sos.scheduler.engine.common.auth.{Account, UserAndPassword}
import com.sos.scheduler.engine.common.scalautil.AutoClosing.autoClosing
import com.sos.scheduler.engine.common.scalautil.HasCloser
import com.sos.scheduler.engine.common.time.ScalaTime._
import com.sos.scheduler.engine.common.utils.FreeTcpPortFinder.findRandomFreeTcpPort
import javax.inject.Singleton
import org.junit.runner.RunWith
import org.scalatest.Assertions._
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FreeSpec}
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import spray.can.Http
import spray.http.StatusCodes._
import spray.httpx.UnsuccessfulResponseException
import spray.routing.authentication._

/**
 * @author Joacim Zschimmer
 */
@RunWith(classOf[JUnitRunner])
final class TextAgentClientIT extends FreeSpec with BeforeAndAfterAll with HasCloser with AgentTest with AgentConfigDirectoryProvider {

  override protected def agentConfiguration = AgentConfiguration.forTest(Some(dataDirectory)).copy(
    httpAddress = None)
    .withHttpsInetSocketAddress(super.agentConfiguration.httpAddress.get)

  override def afterAll() = {
    onClose { super.afterAll() }
    close()
  }

  override protected def extraAgentModule = new AbstractModule {
    def configure() = {}

    @Provides @Singleton
    def commandExecutor(): CommandExecutor = new CommandExecutor {
      def executeCommand(command: Command, meta: CommandMeta): Future[command.Response] = {
        val response = command match {
          case ExpectedTerminate ⇒ EmptyResponse
        }
        Future.successful(response.asInstanceOf[command.Response])
      }
    }

    @Provides @Singleton
    def authenticator(conf: AgentConfiguration)(implicit ec: ExecutionContext): UserPassAuthenticator[Account] =
      new SimpleUserPassAuthenticator(Set(UserAndPassword(UserName → Password)))
  }

  "Unauthorized" in {
    autoClosing(newTextAgentClient(_ ⇒ (), login = None)) { client ⇒
      interceptUnauthorized {
        client.executeCommand("""{ $TYPE: Terminate, sigtermProcesses: true, sigkillProcessesAfter: 10 }""")
      }
    }
    autoClosing(newTextAgentClient(_ ⇒ (), Some(UserName → SecretString("WRONG-PASSWORD")))) { client ⇒
      interceptUnauthorized {
        client.executeCommand("""{ $TYPE: Terminate, sigtermProcesses: true, sigkillProcessesAfter: 10 }""")
      }
    }
  }

  "Command" in {
    val output = mutable.Buffer[String]()
    autoClosing(newTextAgentClient(output += _, Some(UserName → Password))) { client ⇒
      client.executeCommand("""{ $TYPE: Terminate, sigtermProcesses: true, sigkillProcessesAfter: 10 }""")
      client.get("")
    }
    assert(output.size == 3)
    assert(output(0) == "{}")
    assert(output(1) == "---")
    assert(output(2) contains "startedAt: '2")
    assert(output(2) contains "isTerminating: false")
    assert(output(2) contains "totalTaskCount: 0")
    assert(output(2) contains "currentTaskCount: 0")
  }

  "requireIsResponding" in {
    val output = mutable.Buffer[String]()
    autoClosing(newTextAgentClient(output += _, Some(UserName → Password))) { client ⇒
      client.requireIsResponding()
    }
    assert(output == List("JobScheduler Agent is responding"))
    autoClosing(new TextAgentClient(agentUri = s"http://127.0.0.1:${findRandomFreeTcpPort()}", _ ⇒ Unit)) { client ⇒
      intercept[Http.ConnectionAttemptFailedException] {client.requireIsResponding() }
    }
  }

  private def newTextAgentClient(output: String ⇒ Unit, login: Option[UserAndPassword]) =
    new TextAgentClient(agentUri = agent.localUri, output, login, Some(keystoreReference))
}

private object TextAgentClientIT {
  private val ExpectedTerminate = Terminate(sigtermProcesses = true, sigkillProcessesAfter = Some(10.s))
  private val UserName = "SHA512-USER"
  private val Password = SecretString("SHA512-PASSWORD")

  private def interceptUnauthorized(body: ⇒ Unit) = {
    val e = intercept[UnsuccessfulResponseException] {
      body
    }
    assert(e.response.status == Unauthorized)
  }
}
