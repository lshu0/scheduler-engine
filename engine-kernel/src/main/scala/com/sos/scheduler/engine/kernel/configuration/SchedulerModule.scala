package com.sos.scheduler.engine.kernel.configuration

import akka.actor.{ActorRefFactory, ActorSystem}
import com.google.common.base.Splitter
import com.google.inject.Scopes.SINGLETON
import com.google.inject.{Injector, Provides}
import com.sos.scheduler.engine.base.utils.ScalaUtils
import com.sos.scheduler.engine.base.utils.ScalaUtils.implicitClass
import com.sos.scheduler.engine.common.akkautils.DeadLetterActor
import com.sos.scheduler.engine.common.async.StandardCallQueue
import com.sos.scheduler.engine.common.configutils.Configs
import com.sos.scheduler.engine.common.guice.ScalaAbstractModule
import com.sos.scheduler.engine.common.scalautil.Closers.implicits._
import com.sos.scheduler.engine.common.scalautil.FileUtils.implicits._
import com.sos.scheduler.engine.common.scalautil.HasCloser
import com.sos.scheduler.engine.common.soslicense.LicenseKeyString
import com.sos.scheduler.engine.common.time.timer.TimerService
import com.sos.scheduler.engine.cplusplus.runtime.DisposableCppProxyRegister
import com.sos.scheduler.engine.data.scheduler.{ClusterMemberId, SchedulerClusterMemberKey, SchedulerId}
import com.sos.scheduler.engine.eventbus.{EventBus, SchedulerEventBus}
import com.sos.scheduler.engine.kernel.async.SchedulerThreadCallQueue
import com.sos.scheduler.engine.kernel.command.{CommandHandler, CommandSubsystem, HasCommandHandlers}
import com.sos.scheduler.engine.kernel.configuration.SchedulerModule._
import com.sos.scheduler.engine.kernel.cppproxy._
import com.sos.scheduler.engine.kernel.database.DatabaseSubsystem
import com.sos.scheduler.engine.kernel.filebased.FileBasedSubsystem
import com.sos.scheduler.engine.kernel.folder.FolderSubsystem
import com.sos.scheduler.engine.kernel.job.JobSubsystem
import com.sos.scheduler.engine.kernel.lock.LockSubsystem
import com.sos.scheduler.engine.kernel.log.PrefixLog
import com.sos.scheduler.engine.kernel.messagecode.MessageCodeHandler
import com.sos.scheduler.engine.kernel.order.{OrderSubsystem, StandingOrderSubsystem}
import com.sos.scheduler.engine.kernel.plugin.PluginSubsystem
import com.sos.scheduler.engine.kernel.processclass.ProcessClassSubsystem
import com.sos.scheduler.engine.kernel.schedule.ScheduleSubsystem
import com.sos.scheduler.engine.kernel.scheduler._
import com.sos.scheduler.engine.kernel.variable.VariableSet
import com.sos.scheduler.engine.main.SchedulerControllerBridge
import com.typesafe.config.Config
import java.time.ZoneId
import java.util.UUID.randomUUID
import javax.inject.Singleton
import javax.persistence.EntityManagerFactory
import scala.collection.JavaConversions._
import scala.collection.{immutable, mutable}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.reflect.ClassTag

final class SchedulerModule(cppProxy: SpoolerC, controllerBridge: SchedulerControllerBridge, schedulerThread: Thread)
extends ScalaAbstractModule
with HasCloser {

  private val lateBoundCppSingletons = mutable.Buffer[Class[_]]()
  private lazy val _zoneId = {
    val state = cppProxy.state_name
    if (Set("none", "loading")(state)) throw new IllegalStateException(s"ZoneId while state=$state")
    ZoneId of cppProxy.time_zone_name
  }

  def configure(): Unit = {
    bind(classOf[DependencyInjectionCloser]) toInstance DependencyInjectionCloser(closer)
    bindInstance(cppProxy)
    bindInstance(controllerBridge)
    bind(classOf[EventBus]) to classOf[SchedulerEventBus] in SINGLETON
    provideSingleton[SchedulerThreadCallQueue] { new SchedulerThreadCallQueue(new StandardCallQueue, cppProxy, schedulerThread) }
    bindInstance(controllerBridge.getEventBus: SchedulerEventBus)
    bind(classOf[SchedulerConfiguration]) toProvider classOf[SchedulerConfiguration.InjectProvider]
    provideSingleton { new SchedulerInstanceId(randomUUID.toString) }
    provideSingleton { new DisposableCppProxyRegister }
    bindInstance(cppProxy.log.getSister: PrefixLog )
    provideCppSingleton { new SchedulerId(cppProxy.id) }
    provideCppSingleton { new ClusterMemberId(cppProxy.cluster_member_id) }
    provideCppSingleton { new DatabaseSubsystem(cppProxy.db) }
    provideCppSingleton { cppProxy.variables.getSister: VariableSet }
    bindSubsystems()
    bindInstance(LateBoundCppSingletons(lateBoundCppSingletons.toVector))
  }

  private def bindSubsystems(): Unit = {
    provideCppSingleton[Folder_subsystemC] { cppProxy.folder_subsystem }
    provideCppSingleton[Job_subsystemC] { cppProxy.job_subsystem }
    provideCppSingleton[Lock_subsystemC] { cppProxy.lock_subsystem }
    provideCppSingleton[Order_subsystemC] { cppProxy.order_subsystem }
    provideCppSingleton[Process_class_subsystemC] { cppProxy.process_class_subsystem }
    provideCppSingleton[Schedule_subsystemC] { cppProxy.schedule_subsystem }
    provideCppSingleton[Task_subsystemC] { cppProxy.task_subsystem }
    provideCppSingleton[Standing_order_subsystemC] { cppProxy.standing_order_subsystem }
  }

  private def provideCppSingleton[A <: AnyRef : ClassTag](provider: ⇒ A) = {
    lateBoundCppSingletons += implicitClass[A]
    provideSingleton(provider)
  }

  @Provides @Singleton
  private def provideFileBasedSubsystemRegister(injector: Injector): FileBasedSubsystem.Register =
    FileBasedSubsystem.Register(injector, List(
      FolderSubsystem,
      JobSubsystem,
      LockSubsystem,
      OrderSubsystem,
      ProcessClassSubsystem,
      ScheduleSubsystem,
      StandingOrderSubsystem))

  @Provides @Singleton
  private def provideEntityManagerFactory(databaseSubsystem: DatabaseSubsystem): EntityManagerFactory =
    databaseSubsystem.entityManagerFactory

  @Provides @Singleton
  private def provideSchedulerClusterMemberKey(schedulerId: SchedulerId, clusterMemberId: ClusterMemberId) =
    new SchedulerClusterMemberKey(schedulerId, clusterMemberId)

  @Provides @Singleton
  private def provideCommandSubsystem(pluginSubsystem: PluginSubsystem) =
    new CommandSubsystem(asJavaIterable(commandHandlers(List(pluginSubsystem))))

  @Provides @Singleton
  private def provideMessageCodeHandler(spoolerC: SpoolerC): MessageCodeHandler =
    MessageCodeHandler.fromCodeAndTextStrings(spoolerC.settings.messageTexts)

  @Provides @Singleton
  private def licenseKeyStrings(spoolerC: SpoolerC): immutable.Iterable[LicenseKeyString] =
    Splitter.on(' ').omitEmptyStrings.splitToList(spoolerC.settings.installed_licence_keys_string).toVector.distinct map LicenseKeyString.apply

  @Provides @Singleton
  private def zoneId: ZoneId = _zoneId

  @Provides @Singleton
  private def actorSystem(config: Config): ActorSystem = {
    val actorSystem = ActorSystem("Engine", config)
    closer.onClose {
      actorSystem.shutdown()
      actorSystem.awaitTermination(30.seconds)
    }
    DeadLetterActor.subscribe(actorSystem)
    actorSystem
  }

  @Provides @Singleton
  private def config(conf: SchedulerConfiguration): Config =
    Configs.parseConfigIfExists(conf.mainConfigurationDirectory / "private/private.conf") withFallback
      SchedulerConfiguration.DefaultConfig

  @Provides @Singleton
  private def executionContext(actorSystem: ActorSystem): ExecutionContext = actorSystem.dispatcher

  @Provides @Singleton
  private def actorRefFactory(actorSystem: ActorSystem): ActorRefFactory = actorSystem

  @Provides @Singleton
  private def timerService(implicit executionContext: ExecutionContext): TimerService = TimerService().closeWithCloser
}

object SchedulerModule {
  private def commandHandlers(objects: Iterable[AnyRef]): Iterable[CommandHandler] =
    (objects collect { case o: HasCommandHandlers ⇒ o.commandHandlers: Iterable[CommandHandler] }).flatten

  final case class LateBoundCppSingletons(interfaces: Vector[Class[_]])
}
