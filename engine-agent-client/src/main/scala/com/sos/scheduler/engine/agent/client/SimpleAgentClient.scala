package com.sos.scheduler.engine.agent.client

import akka.actor.ActorSystem

/**
 * Simple client for JobScheduler Agent.
 * <p>
 * Should be closed after use, to close all remaining HTTP connections.
 *
 * @author Joacim Zschimmer
 */
final class SimpleAgentClient private(protected[client] val agentUri: String) extends AgentClient with AutoCloseable {

  protected val licenseKeys = Nil
  protected val actorRefFactory = ActorSystem("SimpleAgentClient")

  def close() = actorRefFactory.shutdown()
}

object SimpleAgentClient {
  def apply(agentUri: String) = new SimpleAgentClient(agentUri)
}
