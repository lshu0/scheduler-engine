package com.sos.scheduler.engine.agent

import com.sos.scheduler.engine.common.commandline.CommandLineArguments

/**
 * @author Joacim Zschimmer
 */
final case class AgentConfiguration(
  httpPort: Int,

  /**
   * The IP address of the only network interface, the Agent should listen to.
   * If empty, the Agent listens to all network interfaces.
   */
  httpInterfaceRestriction: Option[String] = None)

object AgentConfiguration {
  def apply(args: Seq[String]) = {
    val arguments = CommandLineArguments(args)
    val httpPort = arguments.int("-http-port=")
    arguments.requireNoMoreArguments()
    new AgentConfiguration(httpPort = httpPort)
  }
}