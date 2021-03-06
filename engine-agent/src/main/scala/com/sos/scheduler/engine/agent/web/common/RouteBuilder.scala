package com.sos.scheduler.engine.agent.web.common

import akka.actor.ActorRefFactory
import com.sos.scheduler.engine.agent.configuration.AgentConfiguration.InvalidAuthenticationDelay
import com.sos.scheduler.engine.agent.web.common.RouteBuilder._
import com.sos.scheduler.engine.common.auth.Account
import com.sos.scheduler.engine.common.scalautil.Collections.implicits.RichSeq
import com.sos.scheduler.engine.common.scalautil.Logger
import com.sos.scheduler.engine.common.time.ScalaTime._
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, blocking}
import spray.routing.AuthenticationFailedRejection.CredentialsRejected
import spray.routing.Directives._
import spray.routing.authentication.{BasicAuth, _}
import spray.routing.{AuthenticationFailedRejection, RejectionHandler, Route}

/**
  * @author Joacim Zschimmer
  */
final class RouteBuilder extends Mutable {
  private val otherRoutes = mutable.Buffer[CallersRoute]()
  private val prefixedRoutes = mutable.Buffer[CallersRoute]()
  private val jobschedulerStandardRoutes = mutable.Buffer[CallersRoute]()
  private val apiRoutes = mutable.Buffer[CallersRoute]()
  private val unauthenticatedApiRoutes = mutable.Buffer[CallersRoute]()

  def addRootRoute(route: ⇒ Route): Unit =
    otherRoutes += CallersRoute.of("addRootRoute", route)

  /**
    * Routes prefixed with uriPathPrefix.
    */
  def addPrefixedRoute(route: ⇒ Route): Unit =
    prefixedRoutes += CallersRoute.of("addPrefixedRoute", route)

  /**
    * Routes for non-authenticated /jobscheduler.
    */
  def addJobschedulerRoute(route: ⇒ Route): Unit =
    jobschedulerStandardRoutes += CallersRoute.of("addJobschedulerRoute", route)

  /**
    * Routes for authenticated /jobscheduler/agent/api.
    */
  def addApiRoute(route: ⇒ Route): Unit =
    apiRoutes += CallersRoute.of("addApiRoute", route)

  /**
    * Routes for non-authenticated /jobscheduler/agent/api.
    */
  def addUnauthenticatedApiRoute(route: ⇒ Route): Unit =
    unauthenticatedApiRoutes += CallersRoute.of("addUnauthenticatedApiRoute", route)

  private def logAllEntries() {
    val all = otherRoutes ++ prefixedRoutes ++ jobschedulerStandardRoutes ++ apiRoutes ++ unauthenticatedApiRoutes
    for (e ← all) logger.trace(s"Using route of ${e.callerName}")
  }

  def ++=(o: RouteBuilder): Unit = {
    otherRoutes ++= o.otherRoutes
    prefixedRoutes ++= o.prefixedRoutes
    jobschedulerStandardRoutes ++= o.jobschedulerStandardRoutes
    apiRoutes ++= o.apiRoutes
    unauthenticatedApiRoutes ++= o.unauthenticatedApiRoutes
  }

  def buildRoute(authenticator: UserPassAuthenticator[Account], uriPathPrefix: String)
    (implicit actorRefFactory: ActorRefFactory): Route =
  {
    implicit val executionContext = actorRefFactory.dispatcher
    val apiRoute =
      handleRejections(failIfCredentialsRejected) {
        authenticate(BasicAuth(authenticator, realm = Realm)) { _ ⇒
          toRoute(apiRoutes)
        }
      } ~
        toRoute(unauthenticatedApiRoutes)

    logAllEntries()

    (decompressRequest() & compressResponseIfRequested(())) {
      possiblyEmptyPathPrefix(uriPathPrefix) {
        pathPrefix("jobscheduler") {
          pathPrefix("agent" / "api") {
            apiRoute
          } ~
            toRoute(jobschedulerStandardRoutes)
        } ~
          toRoute(prefixedRoutes)
      } ~
        toRoute(otherRoutes)
    }
  }
}

object RouteBuilder {
  private val logger = Logger(getClass)
  private val Realm = "JobScheduler Agent"
  private val PackageName = getClass.getPackage.getName

  private def possiblyEmptyPathPrefix(uriPathPrefix: String) = if (uriPathPrefix.isEmpty) pass else pathPrefix(separateOnSlashes(uriPathPrefix))

  private def failIfCredentialsRejected(implicit ec: ExecutionContext) = RejectionHandler {
    case rejections @ AuthenticationFailedRejection(CredentialsRejected, headers) :: _ ⇒
      detach(()) {
        logger.warn(s"HTTP request with invalid authentication rejected")
        blocking {
          sleep(InvalidAuthenticationDelay)
        }
        RejectionHandler.Default(rejections)
      }
  }

  private def callerMethodString(methodName: String): String =
    new Exception().getStackTrace.toIterator.map { _.toString }
      .dropWhile { o ⇒ (o contains PackageName) || (o contains s".$methodName(") }
      .find { _ ⇒ true }   // Like headOption
      .getOrElse("?")

  private case class CallersRoute(callerName: String, route: () ⇒ Route)

  private object CallersRoute {
    def of(methodName: String, rawRoute: ⇒ Route) = new CallersRoute(callerMethodString(methodName), () ⇒ rawRoute)
  }

  private def toRoute(entries: Seq[CallersRoute]): Route = entries.map(_.route()).foldFast(reject)(_ ~ _)
}
