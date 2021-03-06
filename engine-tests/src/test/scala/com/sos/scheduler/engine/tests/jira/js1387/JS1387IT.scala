package com.sos.scheduler.engine.tests.jira.js1387

import com.sos.scheduler.engine.common.time.ScalaTime._
import com.sos.scheduler.engine.data.jobchain.JobChainPath
import com.sos.scheduler.engine.data.order.OrderKey
import com.sos.scheduler.engine.data.time.SchedulerDateTime.{formatLocally, formatUtc}
import com.sos.scheduler.engine.kernel.Scheduler
import com.sos.scheduler.engine.test.configuration.TestConfiguration
import com.sos.scheduler.engine.test.scalatest.ScalaSchedulerTest
import com.sos.scheduler.engine.tests.jira.js1387.JS1387IT._
import java.time.Instant.now
import java.time.temporal.ChronoUnit.SECONDS
import java.time.{Instant, Period}
import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.junit.JUnitRunner

/**
  * @author Joacim Zschimmer
  */
@RunWith(classOf[JUnitRunner])
final class JS1387IT extends MyTests {
  override protected lazy val testConfiguration = TestConfiguration(getClass, mainArguments = List("-distributed-orders"))
}

@RunWith(classOf[JUnitRunner])
final class JS1387nonDistributedIT extends MyTests

private[js1387] object JS1387IT {
  private val NonDistributedJobChainPath = JobChainPath("/test-non-distributed")
  private val DistributedJobChainPath = JobChainPath("/test-distributed")

  private val TimedOrders = {
    val t = now truncatedTo SECONDS plus Period.ofDays(1)
    Vector(
      AtTimedOrder     (NonDistributedJobChainPath orderKey "at-non-distributed"     , t),
      RuntimeTimedOrder(NonDistributedJobChainPath orderKey "runtime-non-distributed", t + 1.s),
      AtTimedOrder     (DistributedJobChainPath    orderKey "at-distributed"         , t + 2.s),
      RuntimeTimedOrder(DistributedJobChainPath    orderKey "runtime-non-distributed", t + 3.s))
  }

  abstract class MyTests extends FreeSpec with ScalaSchedulerTest {
    s"Command show_calendar" in {
      for (o ← TimedOrders) scheduler executeXml o.toOrderCommand
      val answer = (scheduler executeXml <show_calendar what="orders" before="2030-12-31T12:00:00Z" limit="10"/>).answer
      val entries = (answer \ "calendar" \ "_") collect { case e: xml.Elem ⇒ e }
      assert(entries.size == TimedOrders.size)
      assert(entries.toSet == (TimedOrders map { _.toExpectedCalendarEntry}).toSet)
    }
  }

  private trait TimedOrder {
    val orderKey: OrderKey
    val at: Instant
    def toOrderCommand: xml.Elem
    def toExpectedCalendarEntry =
      <at job_chain={orderKey.jobChainPath.string} order={orderKey.id.string} at={formatUtc(at)}/>
  }

  private final case class AtTimedOrder(orderKey: OrderKey, at: Instant) extends TimedOrder {
    def toOrderCommand =
      <order job_chain={orderKey.jobChainPath.string} id={orderKey.id.string} at={formatLocally(Scheduler.DefaultZoneId, at)}/>
  }

  private final case class RuntimeTimedOrder(orderKey: OrderKey, at: Instant) extends TimedOrder {
    def toOrderCommand =
      <order job_chain={orderKey.jobChainPath.string} id={orderKey.id.string}>
        <run_time>
          <at at={formatLocally(Scheduler.DefaultZoneId, at)}/>
        </run_time>
      </order>
  }
}
