package com.sos.scheduler.engine.data.order

import com.sos.scheduler.engine.data.filebased.FileBasedState
import com.sos.scheduler.engine.data.job.TaskId
import com.sos.scheduler.engine.data.jobchain.JobChainPath
import java.time.Instant
import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.junit.JUnitRunner
import spray.json._

/**
  * @author Joacim Zschimmer
  */
@RunWith(classOf[JUnitRunner])
final class OrderOverviewTest extends FreeSpec {

  "JSON" in {
    val obj = OrderOverview(
      JobChainPath("/a") orderKey "1",
      FileBasedState.active,
      OrderSourceType.adHoc,
      OrderState("100"),
      nextStepAt = Some(Instant.parse("2016-07-18T12:00:00Z")),
      taskId = Some(TaskId(123)))
    val jsValue = """{
      "path": "/a,1",
      "fileBasedState": "active",
      "sourceType": "adHoc",
      "orderState": "100",
      "nextStepAt": "2016-07-18T12:00:00Z",
      "isSuspended": false,
      "isBlacklisted": false,
      "taskId": "123"
    }""".parseJson
    assert(obj.toJson == jsValue)
    assert(obj == jsValue.convertTo[OrderOverview])
  }
}