package com.sos.scheduler.engine.kernel.order

import com.sos.scheduler.engine.common.guice.GuiceImplicits._
import com.sos.scheduler.engine.common.scalautil.Collections.emptyToNone
import com.sos.scheduler.engine.common.scalautil.Logger
import com.sos.scheduler.engine.cplusplus.runtime.annotation.ForCpp
import com.sos.scheduler.engine.cplusplus.runtime.{CppProxyInvalidatedException, Sister, SisterType}
import com.sos.scheduler.engine.data.configuration.SchedulerDataConstants.EternalCppMillis
import com.sos.scheduler.engine.data.filebased.FileBasedType
import com.sos.scheduler.engine.data.job.TaskId
import com.sos.scheduler.engine.data.jobchain.{JobChainPath, NodeKey}
import com.sos.scheduler.engine.data.order.{OrderId, OrderKey, OrderOverview, OrderSourceType, OrderState, QueryableOrder}
import com.sos.scheduler.engine.eventbus.HasUnmodifiableDelegate
import com.sos.scheduler.engine.kernel.async.CppCall
import com.sos.scheduler.engine.kernel.cppproxy.OrderC
import com.sos.scheduler.engine.kernel.filebased.FileBased
import com.sos.scheduler.engine.kernel.order.Order._
import com.sos.scheduler.engine.kernel.order.jobchain.JobChain
import com.sos.scheduler.engine.kernel.scheduler.SchedulerConstants.{FileOrderAgentUriVariableName, FileOrderPathVariableName}
import com.sos.scheduler.engine.kernel.scheduler.{HasInjector, SchedulerException}
import com.sos.scheduler.engine.kernel.time.CppTimeConversions.eternalCppMillisToNoneInstant
import com.sos.scheduler.engine.kernel.variable.VariableSet
import java.time.Instant
import scala.util.{Failure, Success}

@ForCpp
final class Order private(
  protected val cppProxy: OrderC,
  protected val subsystem: StandingOrderSubsystem)
extends FileBased
with QueryableOrder
with UnmodifiableOrder
with HasUnmodifiableDelegate[UnmodifiableOrder]
with OrderPersistence {

  import subsystem.agentClientFactory

  type ThisPath = OrderKey

  lazy val unmodifiableDelegate = new UnmodifiableOrderDelegate(this)

  def onCppProxyInvalidated(): Unit = {}

  def remove(): Unit = {
    cppProxy.java_remove()
  }

  @ForCpp
  def agentFileExists(cppCall: CppCall): Unit = {
    import subsystem.schedulerThreadCallQueue.implicits.executionContext
    val orderId = id
    val p = parameters
    val file = p("scheduler_file_path")
    require(file.nonEmpty, "Order variable scheduler_file_path must not be empty")
    val agentUri = p(FileOrderAgentUriVariableName)
    require(agentUri.nonEmpty, s"Order variable $FileOrderAgentUriVariableName must not be empty")
    agentClientFactory.apply(agentUri).fileExists(file) onComplete {
      case Success(exists) ⇒
        try cppCall.call(exists: java.lang.Boolean)
        catch { case t: CppProxyInvalidatedException ⇒ logger.trace(s"Order '$orderId' has vanished while agentFileExists returns $exists") }
      case Failure(t) ⇒ log.error(t.toString)
    }
  }

  override def overview: OrderOverview =
    OrderOverview(
      path = key,  // key because this.path is valid only for permanent orders
      fileBasedState,
      sourceType,
      orderState = state,
      nextStepAt = nextStepAt,
      setbackUntil = setbackUntil,
      taskId = taskId,
      isBlacklisted = isBlacklisted,
      isSuspended = isSuspended)

  def isSetback = setbackUntil.isDefined

  def stringToPath(o: String) = OrderKey(o)

  def fileBasedType = FileBasedType.order

  def sourceType: OrderSourceType =
    if (cppProxy.is_file_order) OrderSourceType.fileOrderSource
    else if (cppProxy.has_base_file) OrderSourceType.fileBased
    else OrderSourceType.adHoc

  def key = orderKey

  def orderKey: OrderKey = jobChainPath orderKey id

  def id: OrderId =
    OrderId(cppProxy.string_id)

  def nodeKey = NodeKey(jobChainPath, state)

  def state: OrderState =
    OrderState(cppProxy.string_state)

  def initialState: OrderState =
    OrderState(cppProxy.initial_state_string)

  def endState: OrderState =
    OrderState(cppProxy.end_state_string)

  def endState_=(s: OrderState): Unit = {
    cppProxy.set_end_state(s.string)
  }

  private def nextStepAt: Option[Instant] =
    cppProxy.next_step_at_millis match {
      case EternalCppMillis ⇒ None
      case millis ⇒ Some(Instant ofEpochMilli millis)
    }

  private def setbackUntil: Option[Instant] =
    cppProxy.setback_millis match {
      case 0 ⇒ None
      case millis ⇒ Some(Instant ofEpochMilli millis)
    }

  def taskId: Option[TaskId] =
    cppProxy.task_id match {
      case 0 ⇒ None
      case o ⇒ Some(TaskId(o))
    }

  def priority: Int =
    cppProxy.priority

  def priority_=(o: Int): Unit = {
    cppProxy.set_priority(o)
  }

  def isSuspended: Boolean =
    cppProxy.suspended

  def isSuspended_=(b: Boolean): Unit = {
    cppProxy.set_suspended(b)
  }

  def isBlacklisted = cppProxy.is_on_blacklist()

  def title: String =
    cppProxy.title

  def title_=(o: String): Unit = {
    cppProxy.set_title(o)
  }

  def jobChainPath: JobChainPath =
    emptyToNone(cppProxy.job_chain_path_string) map JobChainPath.apply getOrElse throwNotInAJobChain()

  def jobChain: JobChain =
    jobChainOption getOrElse throwNotInAJobChain()

  def jobChainOption: Option[JobChain] =
    Option(cppProxy.job_chain) map { _.getSister }

  def filePath: String = cppProxy.params.get_string(FileOrderPathVariableName)

  def fileAgentUri: String = cppProxy.params.get_string(FileOrderAgentUriVariableName)

  def parameters: VariableSet =
    cppProxy.params.getSister

  def nextInstantOption: Option[Instant] =
    eternalCppMillisToNoneInstant(cppProxy.next_time_millis)

  def createdAtOption: Option[Instant] = throw new UnsupportedOperationException

  override def toString = {
    val result = getClass.getSimpleName
    if (cppProxy.cppReferenceIsValid) s"$result ${cppProxy.job_chain_path_string}:$id"
    else result
  }

  def blacklist(): Unit = cppProxy.set_on_blacklist()

  private def throwNotInAJobChain() = throw new SchedulerException(s"Order is not in a job chain: $toString")

  def setEndStateReached() = cppProxy.set_end_state_reached()
}


object Order {
  final class Type extends SisterType[Order, OrderC] {
    def sister(proxy: OrderC, context: Sister): Order = {
      val injector = context.asInstanceOf[HasInjector].injector
      new Order(proxy, injector.instance[StandingOrderSubsystem])
    }
  }

  private val logger = Logger(getClass)
}
