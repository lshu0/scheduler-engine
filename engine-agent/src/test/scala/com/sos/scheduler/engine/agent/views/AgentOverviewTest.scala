package com.sos.scheduler.engine.agent.views

import java.time.Instant
import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.junit.JUnitRunner
import spray.json._

/**
 * @author Joacim Zschimmer
 */
@RunWith(classOf[JUnitRunner])
final class AgentOverviewTest extends FreeSpec {
  "JSON" in {
    val obj = AgentOverview(
      startedAt = Instant.parse("2015-06-01T12:00:00Z"),
      version = "TEST-VERSION",
      currentTaskCount = 111,
      totalTaskCount = 222,
      isTerminating = false,
      system = AgentOverview.SystemInformation(hostname = "TEST-HOSTNAME"),
      java = AgentOverview.JavaInformation(systemProperties = Map("test" → "TEST")))
    val json =
      s"""{
        "startedAt": "2015-06-01T12:00:00Z",
        "version": "TEST-VERSION",
        "currentTaskCount": 111,
        "totalTaskCount": 222,
        "isTerminating": false,
        "java": {
          "systemProperties": {
            "test": "TEST"
          }
        },
        "system": {
          "hostname": "TEST-HOSTNAME",
          "mxBeans": {}
         }
      }""".parseJson
    assert(obj.toJson == json)
    //assert(obj = json.convertTo[AgentOverview])
  }
}
