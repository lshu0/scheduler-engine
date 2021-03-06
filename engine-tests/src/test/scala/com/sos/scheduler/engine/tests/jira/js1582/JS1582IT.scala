package com.sos.scheduler.engine.tests.jira.js1582

import com.sos.scheduler.engine.common.scalautil.Futures.implicits.SuccessFuture
import com.sos.scheduler.engine.common.time.ScalaTime._
import com.sos.scheduler.engine.data.job.{JobPath, TaskStartedEvent}
import com.sos.scheduler.engine.test.EventBusTestFutures.implicits.RichEventBus
import com.sos.scheduler.engine.test.scalatest.ScalaSchedulerTest
import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.junit.JUnitRunner

/**
  * @author Joacim Zschimmer
  */
@RunWith(classOf[JUnitRunner])
final class JS1582IT extends FreeSpec with ScalaSchedulerTest {

  private lazy val apiStarted = controller.eventBus.eventFuture[TaskStartedEvent](_.jobPath == JobPath("/test-api"))
  private lazy val shellStarted = controller.eventBus.eventFuture[TaskStartedEvent](_.jobPath == JobPath("/test-shell"))

  override protected def onBeforeSchedulerActivation() = {
    apiStarted
    shellStarted
    super.onBeforeSchedulerActivation()
  }

  "min_tasks not allowed for shell jobs" in {
    apiStarted await 10.s
    sleep(2.s)
    if (shellStarted.isCompleted) fail(shellStarted.value.get.toString)
  }
}
