package com.sos.scheduler.engine.plugins.newwebservice.html

import com.sos.scheduler.engine.data.scheduler.SchedulerOverview
import com.sos.scheduler.engine.plugins.newwebservice.html.SchedulerHtmlPage._
import scala.concurrent.{ExecutionContext, Future}
import scalatags.Text.all._

/**
  * @author Joacim Zschimmer
  */
final class SchedulerOverviewHtmlPage private(
  protected val schedulerOverview: SchedulerOverview,
  protected val webServiceContext: WebServiceContext)
extends SchedulerHtmlPage {

  import schedulerOverview._

  override def title = "JobScheduler"
  override def headlineSuffix = ""

  def scalatag =
    htmlPage(
      headline,
      p(marginBottom := "30px")(
        s"Started at ",
        instantWithDurationToHtml(startedAt),
        " · ",
        ( (httpPort map { o ⇒ s" HTTP port $o" }) ++
          (udpPort map { o ⇒ s" UDP port $o" }) ++
          Some(s"PID $pid") ++
          Some(state))
          .mkString(" · "),
        p(
          a(href := "api/order/")("Orders"),
          br,
          a(href := "api/jobChain/")("Job chains"),
          br,
          form(action := "api/command", method := "get")(
            label(cls := "input-group input-group-sm")(
              span(cls := "input-group-addon")("XML command: "),
              input(`type` := "text", name := "command", value := "s", placeholder := "For example: show_state", cls := "form-control"),
              " ",
              span(cls := "input-group-btn")(
                button(`type` := "submit", cls := "btn btn-primary")("Execute")))))))
}

object SchedulerOverviewHtmlPage {
  import scala.language.implicitConversions

  implicit def toHtml(overview: SchedulerOverview)(implicit webServiceContext: WebServiceContext, ec: ExecutionContext): Future[HtmlPage] =
    Future.successful(new SchedulerOverviewHtmlPage(overview, webServiceContext))
}