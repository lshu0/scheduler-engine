package com.sos.scheduler.engine.common.process

import com.sos.scheduler.engine.common.process.Processes._
import com.sos.scheduler.engine.common.scalautil.FileUtils.implicits.RichPath
import com.sos.scheduler.engine.common.scalautil.Futures.implicits.RichFutures
import com.sos.scheduler.engine.common.time.ScalaTime._
import com.sos.scheduler.engine.common.time.WaitForCondition.waitForCondition
import java.nio.file.Files.delete
import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.junit.JUnitRunner
import scala.collection.JavaConversions._
import scala.concurrent.forkjoin.ForkJoinPool
import scala.concurrent.{ExecutionContext, Future}

/**
  * JS-1581 "Text file busy" when starting many processes.
  *
  * @author Joacim Zschimmer
  * @see https://bugs.openjdk.java.net/browse/JDK-8068370
  */
@RunWith(classOf[JUnitRunner])
final class ProcessesIT extends FreeSpec {

  private val n = 1000
  private val threadCount = 10 * sys.runtime.availableProcessors

  s"$n concurrent process starts with $threadCount threads" in {
    val forkJoinPool = new ForkJoinPool(threadCount)
    implicit val executionContext = ExecutionContext.fromExecutor(forkJoinPool)
    val filesAndProcesses = for (i ← 0 until n) yield Future {
      val file = newTemporaryShellFile(s"#$i")
      file.contentString = "exit"
      val process = new ProcessBuilder(List(s"$file")).startRobustly()
      (file, process)
    }
    val (files, processes) = (filesAndProcesses await 300.s).unzip
    waitForCondition(300.s, 100.ms) { !(processes exists { _.isAlive }) }
    for (p ← processes) {
      val rc = p.waitFor()
      assert(rc == 0)
    }
    for (f ← files) delete(f)
    forkJoinPool.shutdown()
  }
}
