package com.sos.scheduler.engine.plugins.newwebservice.html

import com.sos.scheduler.engine.common.scalautil.Logger
import scala.language.implicitConversions
import scalatags.Text.TypedTag
import spray.http.HttpEntity
import spray.http.MediaTypes.`text/html`
import spray.httpx.marshalling.Marshaller

/**
  * @author Joacim Zschimmer
  */
trait HtmlPage {
  def scalatag: TypedTag[String]
}

object HtmlPage {
  private val logger = Logger(getClass)

  implicit val marshaller = Marshaller.of[HtmlPage](`text/html`) { (htmlPage, contentType, ctx) ⇒
    try {
      val sb = new StringBuilder(10000)
      sb.append("<!DOCTYPE html>")
      htmlPage.scalatag.writeTo(sb)
      ctx.marshalTo(HttpEntity(contentType, sb.toString))
    } catch {
      case e: OutOfMemoryError ⇒
        logger.error(e.toString)
        throw new RuntimeException(e.toString, e)  // Too avoid termination of Akka
    }
  }
}