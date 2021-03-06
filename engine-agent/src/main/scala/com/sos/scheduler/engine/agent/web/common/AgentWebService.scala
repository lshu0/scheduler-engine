package com.sos.scheduler.engine.agent.web.common

import akka.actor.ActorRefFactory
import com.sos.scheduler.engine.common.auth.Account
import spray.http.Uri.Path
import spray.routing._
import spray.routing.authentication._

/**
 * Standard trait for Agent web services.
 * To be able to mix-in multiple web services, use `addRootRoute` to add a `Route`.
 * Method `route` returns the combined `Route`.
 *
 * @author Joacim Zschimmer
 */
trait AgentWebService extends AgentExceptionHandler {
  protected def uriPathPrefix: String   // Same value for every AgentWebService

  protected final def prefixPath = Path(if (uriPathPrefix.isEmpty) "" else s"/$uriPathPrefix")
  protected final def jobschedulerPath = Path(s"$prefixPath/jobscheduler")
  protected final def agentPath = Path(s"$prefixPath/jobscheduler/agent")
  protected final val routeBuilder = new RouteBuilder

  final def buildRoute(authenticator: UserPassAuthenticator[Account])(implicit actorRefFactory: ActorRefFactory): Route =
    routeBuilder.buildRoute(authenticator, uriPathPrefix = uriPathPrefix)
}
