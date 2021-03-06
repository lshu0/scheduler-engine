package com.sos.scheduler.engine.plugins.jetty.cpp

import CppServlet._
import com.sos.scheduler.engine.common.scalautil.Logger
import com.sos.scheduler.engine.cplusplus.runtime.{CppProxyInvalidatedException, DisposableCppProxyRegister}
import com.sos.scheduler.engine.kernel.scheduler.{SchedulerIsClosed, SchedulerHttpService}
import javax.inject.Inject
import javax.inject.Singleton
import javax.servlet.http.HttpServletResponse.SC_MOVED_PERMANENTLY
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

@Singleton
final class CppServlet @Inject private(
    schedulerHttpService: SchedulerHttpService,
    cppProxyRegister: DisposableCppProxyRegister,
    schedulerIsClosed: SchedulerIsClosed)
  extends HttpServlet {

  override def service(request: HttpServletRequest, response: HttpServletResponse): Unit = {
    if (request.getMethod == "GET" && request.getPathInfo == null) {
      response.setStatus(SC_MOVED_PERMANENTLY)
      response.setHeader("Location", request.getRequestURI +"/") // Basis muss mit Schrägstrich enden
    } else
      normalService(request, response)
  }

  private def normalService(request: HttpServletRequest, response: HttpServletResponse): Unit = {
    val attributeName = classOf[CppServlet].getName

    def startOperation(): Unit = {
      val operation = new Operation(request, response, schedulerHttpService, cppProxyRegister, schedulerIsClosed)
      try operation.start()
      catch { case x: Throwable => operation.tryClose(); throw x }
      if (!operation.isClosed) request.setAttribute(attributeName, operation)
    }

    def continueOperation(operation: Operation): Unit = {
      try operation.continue()
      catch { case x: Throwable => operation.tryClose(); throw x }
      finally if (operation.isClosed) request.removeAttribute(attributeName)
    }

    try request.getAttribute(attributeName) match {
      case null => startOperation()
      case o: Operation => continueOperation(o)
    }
    catch {
      case x: InterruptedException => logger.debug(x.toString)
      case x: RuntimeException if x.getCause.isInstanceOf[InterruptedException] => logger.debug(x.toString)
      case x: CppProxyInvalidatedException => logger.debug(x.toString)
    }
  }
}

object CppServlet {
  private val logger = Logger(getClass)
}
