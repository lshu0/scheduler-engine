package com.sos.scheduler.engine.agent.data.commands

import com.sos.scheduler.engine.data.job.{TaskId, JobPath}
import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.junit.JUnitRunner
import spray.json._

/**
 * @author Joacim Zschimmer
 */
@RunWith(classOf[JUnitRunner])
final class StartApiTaskTest extends FreeSpec {

  "JSON minimum" in {
    val obj = StartApiTask(
      meta = None,
      javaOptions = "JAVA-OPTIONS",
      javaClasspath = "JAVA-CLASSPATH")
    val json = """{
      "$TYPE": "StartApiTask",
      "javaOptions": "JAVA-OPTIONS",
      "javaClasspath": "JAVA-CLASSPATH"
    }""".parseJson
    assert((obj: Command).toJson == json)   // Command serializer includes $TYPE
    assert(obj == json.convertTo[Command])
  }

  "JSON maximum" in {
    val obj = StartApiTask(
      meta = Some(StartTask.Meta(
        JobPath("/folder/test"),
        TaskId(123))),
      javaOptions = "JAVA-OPTIONS",
      javaClasspath = "JAVA-CLASSPATH")
    val json = """{
      "$TYPE": "StartApiTask",
      "meta": {
        "job": "/folder/test",
        "taskId": "123"
      },
      "javaOptions": "JAVA-OPTIONS",
      "javaClasspath": "JAVA-CLASSPATH"
    }""".parseJson
    assert((obj: Command).toJson == json)   // Command serializer includes $TYPE
    assert(obj == json.convertTo[Command])
  }
}
