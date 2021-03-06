package com.sos.scheduler.engine.test.agent

import com.sos.scheduler.engine.agent.Agent
import com.sos.scheduler.engine.agent.client.AgentClient
import com.sos.scheduler.engine.agent.configuration.AgentConfiguration
import com.sos.scheduler.engine.client.agent.SchedulerAgentClientFactory
import com.sos.scheduler.engine.common.scalautil.Futures.implicits.SuccessFuture
import com.sos.scheduler.engine.common.scalautil.HasCloser
import com.sos.scheduler.engine.common.time.ScalaTime._
import com.sos.scheduler.engine.data.job.JobPath
import com.sos.scheduler.engine.data.processclass.ProcessClassPath
import com.sos.scheduler.engine.data.scheduler.SchedulerCloseEvent
import com.sos.scheduler.engine.eventbus.EventSubscription
import com.sos.scheduler.engine.test.agent.AgentWithSchedulerTest._
import com.sos.scheduler.engine.test.scalatest.ScalaSchedulerTest
import java.nio.file.Path

/**
 * @author Joacim Zschimmer
 */
trait AgentWithSchedulerTest extends HasCloser with ScalaSchedulerTest {

  protected final lazy val agent = {
    val agent = new Agent(newAgentConfiguration())
    eventBus register EventSubscription[SchedulerCloseEvent] { _ ⇒ agent.close() }   // Shutdown the server Agent after the client Engine
    // Never unregistered, Scheduler closes anyway
    agent
  }
  protected final lazy val agentUri = agent.localUri
  protected final lazy val agentClient: AgentClient = instance[SchedulerAgentClientFactory].apply(agent.localUri)

  protected def newAgentConfiguration(): AgentConfiguration = newAgentConfiguration(None)

  protected final def newAgentConfiguration(data: Option[Path]) = AgentConfiguration.forTest(data = data)

  protected override def onSchedulerActivated() = {
    val started = agent.start()
    scheduler executeXml <process_class name={AgentProcessClassPath.withoutStartingSlash} remote_scheduler={agentUri}/>
    started await 10.s
    super.onSchedulerActivated()
  }
}

object AgentWithSchedulerTest {
  val AgentProcessClassPath = ProcessClassPath("/test-agent")

  implicit class AgentJobPath(val s: JobPath) extends AnyVal {
    def asAgent = JobPath(s.string.concat("-agent"))
  }
}
