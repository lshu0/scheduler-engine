package com.sos.scheduler.engine.plugins.newwebservice.json

import com.sos.scheduler.engine.common.scalautil.Logger
import com.sos.scheduler.engine.common.sprayutils.YamlJsonConversion.ToYamlString
import com.sos.scheduler.engine.common.time.Stopwatch
import com.sos.scheduler.engine.data.filebased.FileBasedState
import com.sos.scheduler.engine.data.job.TaskId
import com.sos.scheduler.engine.data.jobchain.JobChainPath
import com.sos.scheduler.engine.data.order.{OrderOverview, OrderSourceType, OrderState}
import com.sos.scheduler.engine.plugins.newwebservice.json.JsonSpeedTest._
import java.time.Instant.now
import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.junit.JUnitRunner
import scala.collection.immutable
import spray.json.DefaultJsonProtocol._
import spray.json._

/**
  * @author Joacim Zschimmer
  */
@RunWith(classOf[JUnitRunner])
final class JsonSpeedTest extends FreeSpec {

  if (false)
  "Speed" in {
    for (_ ← 1 to 5) {
      val orderOverviews = newOrderOverviews(n)
      toJson(orderOverviews)
      toYaml(orderOverviews)
      logger.info("-------")
    }
  }
}

private object JsonSpeedTest {
  val n = 10000
  private val logger = Logger(getClass)

  private def newOrderOverviews(n: Int) = {
    val stopwatch = new Stopwatch
    val numbers = Iterator.from(1)
    val result = for (i ← 1 to n) yield OrderOverview(
      JobChainPath("/a") orderKey numbers.next().toString,
      FileBasedState.active,
      OrderSourceType.adHoc,
      OrderState(numbers.next().toString),
      nextStepAt = Some(now),
      taskId = Some(TaskId(numbers.next())))
    logger.info(stopwatch.itemsPerSecondString(n, "OrderOverview"))
    result
  }

  private def toJson(orderOverviews: immutable.Seq[OrderOverview]): Unit = {
    val stopwatch = new Stopwatch
    val jsArray = orderOverviews.toJson.asInstanceOf[JsArray]
    logger.info(stopwatch.itemsPerSecondString(n, "OrderOverview.toJson"))
    assert(jsArray.elements.size == n)
  }

  private def toYaml(orderOverviews: immutable.Seq[OrderOverview]): Unit = {
    val stopwatch = new Stopwatch
    orderOverviews.toYaml: String
    logger.info(stopwatch.itemsPerSecondString(n, "OrderOverview.toYaml"))
  }
}