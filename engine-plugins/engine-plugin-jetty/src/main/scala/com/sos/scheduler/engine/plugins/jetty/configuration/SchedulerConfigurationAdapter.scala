package com.sos.scheduler.engine.plugins.jetty.configuration

import com.google.common.base.Splitter
import com.sos.scheduler.engine.common.scalautil.Collections.implicits._
import com.sos.scheduler.engine.common.scalautil.FileUtils.implicits._
import com.sos.scheduler.engine.common.scalautil.xmls.ScalaStax.domElementToStaxSource
import com.sos.scheduler.engine.common.scalautil.xmls.ScalaXMLEventReader
import com.sos.scheduler.engine.kernel.scheduler.SchedulerConfiguration
import com.sos.scheduler.engine.plugins.jetty.configuration.JettyConfiguration.{FixedTcpPortNumber, LazyRandomTcpPortNumber, TcpPortNumber, WebAppContextConfiguration}
import com.sos.scheduler.engine.plugins.jetty.configuration.PluginLoginService.Login
import java.io.File
import java.util.regex.Pattern
import org.eclipse.jetty.security.LoginService
import org.eclipse.jetty.util.security.Password
import org.w3c.dom.Element
import scala.collection.immutable

object SchedulerConfigurationAdapter {

  private val SpaceSplitter = Splitter.on(Pattern.compile(" +")).omitEmptyStrings

  def jettyConfiguration(pluginElement: Element, schedulerConfiguration: SchedulerConfiguration): JettyConfiguration =
    ScalaXMLEventReader.parseDocument(domElementToStaxSource(pluginElement))(parseWithReader(schedulerConfiguration))

  def parseWithReader(schedulerConfiguration: SchedulerConfiguration)(eventReader: ScalaXMLEventReader): JettyConfiguration = {
    import eventReader._

    def result =
      parseElement("plugin.config") {
        val portOption = port(attributeMap get "port", schedulerConfiguration.httpPortOption)
        val children = forEachStartElement {
          case "loginService" ⇒ parseLoginService()
        }
        JettyConfiguration(
          portOption = portOption,
          jettyXMLURLOption = configFileIfExists("jetty.xml") map { _.toURI.toURL },
          webAppContextConfigurationOption = Some(WebAppContextConfiguration(
            resourceBaseURL = schedulerConfiguration.webDirectoryUrlOption getOrElse Config.ResourceBaseResource.url,
            webXMLFileOption = configFileIfExists("web.xml"))),
          loginServiceOption = children.option[LoginService]("loginService"),
          accessLogFileOption = Some(new File(schedulerConfiguration.logDirectory, "http.log")))
      }

    def port(attributeOption: Option[String], schedulerOption: Option[Int]): Option[TcpPortNumber] =
      (attributeOption, schedulerOption) match {
        case (Some(a), Some(s)) ⇒ throw new IllegalArgumentException(s"Either use port='$a' or -http-port=$s")
        case (Some("TEST"), None) ⇒ Some(new LazyRandomTcpPortNumber)
        case (Some(a), None) ⇒ Some(FixedTcpPortNumber(a.toInt))
        case (None, Some(s)) ⇒ Some(FixedTcpPortNumber(s))
        case (None, None) ⇒ None
      }

    def parseLoginService(): LoginService =
      parseElement("loginService") {
        val children = forEachStartElement {
          case "logins" ⇒
            parseElement() {
              parseEachRepeatingElement("login") {
                Login(
                  attributeMap("name"),
                  new Password(attributeMap("password")),
                  SpaceSplitter.split(attributeMap("roles")).toImmutableSeq)
              }
          }
        }
        PluginLoginService(children.one[immutable.Seq[Login]]("logins"))
      }

    def configFileIfExists(filename: String) = Some(schedulerConfiguration.mainConfigurationDirectory / filename) filter { _.exists }

    result
  }
}
