package com.sos.scheduler.engine.tests.jira.js1595

import com.sos.scheduler.engine.data.jobchain.JobChainPath
import com.sos.scheduler.engine.data.xmlcommands.OrderCommand
import com.sos.scheduler.engine.test.SchedulerTestUtils._
import com.sos.scheduler.engine.test.agent.AgentWithSchedulerTest
import com.sos.scheduler.engine.test.scalatest.ScalaSchedulerTest
import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.junit.JUnitRunner

/**
  * @author Joacim Zschimmer
  */
@RunWith(classOf[JUnitRunner])
final class JS1595IT extends FreeSpec with ScalaSchedulerTest with AgentWithSchedulerTest {

  "JavaScript, as reference" in {
    check(JobChainPath("/test-javascript"))
  }

  "PowerShell" in {
    pending
    check(JobChainPath("/test-powershell"))
  }

  private def check(jobChainPath: JobChainPath): Unit = {
    val result = runOrder(OrderCommand(jobChainPath orderKey "1", parameters = Map("TEST" → "test-value")))
    assert(result.variables == Map("TEST" → "test-value", "NEW-TEST" → "test-value"))
  }
}