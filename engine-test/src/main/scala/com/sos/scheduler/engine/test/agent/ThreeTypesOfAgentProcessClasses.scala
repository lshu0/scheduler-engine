package com.sos.scheduler.engine.test.agent

import com.sos.scheduler.engine.common.utils.FreeTcpPortFinder._
import com.sos.scheduler.engine.data.processclass.ProcessClassPath
import com.sos.scheduler.engine.data.xmlcommands.ProcessClassConfiguration
import com.sos.scheduler.engine.test.SchedulerTestUtils._
import com.sos.scheduler.engine.test.agent.ThreeTypesOfAgentProcessClasses.TitledProcessClassConfiguration
import com.sos.scheduler.engine.test.configuration.TestConfiguration
import com.sos.scheduler.engine.test.scalatest.ScalaSchedulerTest
import org.scalatest.FreeSpec

/**
  * @author Joacim Zschimmer
  */
trait ThreeTypesOfAgentProcessClasses extends AgentWithSchedulerTest {
  this: ScalaSchedulerTest with FreeSpec ⇒

  protected lazy val tcpPort = findRandomFreeTcpPort()

  override protected lazy val testConfiguration = TestConfiguration(
    getClass,
    mainArguments = List(s"-tcp-port=$tcpPort"))

  protected lazy val processClassConfigurations = List(
    TitledProcessClassConfiguration("without Agent", () ⇒ ProcessClassConfiguration()),
    TitledProcessClassConfiguration("with Universal Agent", () ⇒ ProcessClassConfiguration(agentUris = List(agentUri))),
    TitledProcessClassConfiguration("with Classic Agent", () ⇒ ProcessClassConfiguration(agentUris = List(s"127.0.0.1:$tcpPort"))))

  protected def addTestsForThreeTypesOfAgent(processClassPath: ProcessClassPath)(body: String ⇒ Unit): Unit = {
    for (TitledProcessClassConfiguration(title, config) ← processClassConfigurations) {
      title in {
        deleteAndWriteConfigurationFile(processClassPath, config())
        body(title)
      }
    }
  }
}

object ThreeTypesOfAgentProcessClasses {
  final case class TitledProcessClassConfiguration(title: String, lazyConfig: () ⇒ ProcessClassConfiguration)
}
