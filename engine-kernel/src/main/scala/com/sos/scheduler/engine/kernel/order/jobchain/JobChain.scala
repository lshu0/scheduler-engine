package com.sos.scheduler.engine.kernel.order.jobchain

import com.google.inject.Injector
import com.sos.scheduler.engine.base.utils.ScalaUtils
import com.sos.scheduler.engine.base.utils.ScalaUtils._
import com.sos.scheduler.engine.common.guice.GuiceImplicits._
import com.sos.scheduler.engine.common.scalautil.Collections.emptyToNone
import com.sos.scheduler.engine.cplusplus.runtime.annotation.ForCpp
import com.sos.scheduler.engine.cplusplus.runtime.{Sister, SisterType}
import com.sos.scheduler.engine.data.filebased.FileBasedType
import com.sos.scheduler.engine.data.jobchain.JobChainNodeAction.nextState
import com.sos.scheduler.engine.data.jobchain.{JobChainDetails, JobChainOverview, JobChainPath, JobChainPersistentState}
import com.sos.scheduler.engine.data.order.{OrderId, OrderState}
import com.sos.scheduler.engine.data.processclass.ProcessClassPath
import com.sos.scheduler.engine.kernel.cppproxy.Job_chainC
import com.sos.scheduler.engine.kernel.filebased.FileBased
import com.sos.scheduler.engine.kernel.job.Job
import com.sos.scheduler.engine.kernel.order.jobchain.JobChain._
import com.sos.scheduler.engine.kernel.order.{Order, OrderSubsystem}
import com.sos.scheduler.engine.kernel.persistence.hibernate.ScalaHibernate._
import com.sos.scheduler.engine.kernel.persistence.hibernate.{HibernateJobChainNodeStore, HibernateJobChainStore}
import com.sos.scheduler.engine.kernel.scheduler.HasInjector
import javax.annotation.Nullable
import javax.persistence.EntityManagerFactory
import scala.annotation.tailrec
import scala.collection.JavaConversions._
import scala.collection.{immutable, mutable}

@ForCpp
final class JobChain(
  protected[this] val cppProxy: Job_chainC,
  protected val subsystem: OrderSubsystem,
  injector: Injector)
extends FileBased
with UnmodifiableJobChain {

  type ThisPath = JobChainPath

  private object cppPredecessors {
    private var _edgeSet: Set[(OrderState, OrderState)] = null
    private val predecessorsMap = mutable.Map[String, java.util.ArrayList[String]]() withDefault { orderStateString ⇒
      if (_edgeSet == null) {
        _edgeSet = (nodeMap.values filter { _.action == nextState } map { o ⇒ o.orderState → o.nextState }).toSet
      }
      val result = new java.util.ArrayList[String]
      result.addAll(allPredecessors(_edgeSet, OrderState(orderStateString)) map { _.string } )
      result
    }

    def apply(orderStateString: String) = predecessorsMap(orderStateString)

    def invalidate(): Unit = {
      predecessorsMap.clear()
      _edgeSet = null
    }
  }

  override def overview = JobChainOverview(
    path = path,
    fileBasedState = fileBasedState)

  def stringToPath(o: String) =
    JobChainPath(o)

  def fileBasedType =
    FileBasedType.jobChain

  def onCppProxyInvalidated(): Unit = {}

  private implicit def entityManagerFactory: EntityManagerFactory =
    injector.getInstance(classOf[EntityManagerFactory])

  @ForCpp
  private def loadPersistentState(): Unit = {
    transaction { implicit entityManager =>
      for (persistentState <- nodeStore.fetchAll(path); node <- nodeMap.get(persistentState.state)) {
        node.action = persistentState.action
      }
      for (persistentState <- persistentStateStore.tryFetch(path)) {
        isStopped = persistentState.isStopped
      }
    }
  }

  @ForCpp private def persistState(): Unit = {
    transaction { implicit entityManager =>
      persistentStateStore.store(persistentState)
    }
  }

  @ForCpp private def deletePersistentState(): Unit = {
    transaction { implicit entityManager =>
      persistentStateStore.delete(path)
      nodeStore.deleteAll(path)
    }
  }

  private def persistentState =
    JobChainPersistentState(path, isStopped)

  private def persistentStateStore =
    injector.getInstance(classOf[HibernateJobChainStore])

  private def nodeStore =
    injector.getInstance(classOf[HibernateJobChainNodeStore])

  @ForCpp
  private def onNextStateActionChanged(): Unit = cppPredecessors.invalidate()

  /** All OrderState, which are skipped to given orderStateString */
  @ForCpp
  private def cppSkippedStates(orderStateString: String): java.util.ArrayList[String] = cppPredecessors(orderStateString)

  override def details = {
    val d = super.details
    JobChainDetails(
      d.path.asInstanceOf[JobChainPath],
      d.fileBasedState,
      d.file,
      d.fileModifiedAt,
      d.sourceXml,
      nodes = nodes map { _.overview }
    )
  }

  def refersToJob(job: Job): Boolean = nodes exists {
    case n: SimpleJobNode => n.getJob eq job
    case _ => false
  }

  def jobNodes: immutable.Seq[SimpleJobNode] =
    nodes collect { case o: SimpleJobNode => o }

  def node(o: OrderState): Node =
    nodeMap(o)

  lazy val nodeMap: Map[OrderState, Node] =
    (nodes map { n => n.orderState -> n }).toMap

  lazy val nodes: immutable.Seq[Node] =
    immutable.Seq() ++ cppProxy.java_nodes

  def order(id: OrderId) =
    orderOption(id) getOrElse sys.error(s"$toString does not contain order '$id'")

  @Nullable def orderOrNull(id: OrderId): Order =
    orderOption(id).orNull

  def orderOption(id: OrderId): Option[Order] =
    Option(cppProxy.order_or_null(id.string)) map { _.getSister }

  def orders: Seq[Order] = cppProxy.java_orders

  def isStopped =
    cppProxy.is_stopped

  def isStopped_=(o: Boolean): Unit = {
    cppProxy.set_stopped(o)
  }

  def orderLimitOption: Option[Int] = someUnless(orderLimit, none = Int.MaxValue)

  /**
   * @return Int.MaxValue, when unlimited
   */
  def orderLimit: Int = cppProxy.max_orders

  private[order] def remove(): Unit = {
    cppProxy.remove()
  }

  def isDistributed: Boolean = cppProxy.is_distributed()

  def processClassPathOption = emptyToNone(cppProxy.default_process_class_path) map ProcessClassPath.apply

  def fileWatchingProcessClassPathOption = emptyToNone(cppProxy.file_watching_process_class_path) map ProcessClassPath.apply
}

object JobChain {
  final class Type extends SisterType[JobChain, Job_chainC] {
    def sister(proxy: Job_chainC, context: Sister) = {
      val injector = context.asInstanceOf[HasInjector].injector
      new JobChain(proxy, injector.instance[OrderSubsystem], injector)
    }
  }

  /** All transitive predecessors of a graph described by edges. */
  private[jobchain] def allPredecessors[A](edges: Iterable[(A, A)], from: A) = {
    val nodeToPredecessors: Map[A, Iterable[A]] = (
      edges groupBy { _._2 }
      mapValues { edges ⇒ (edges map { _._1 }).toVector }
      withDefaultValue Nil)
    @tailrec
    def f(intermediateResult: Set[A]): Set[A] = {
      val preds = for (node ← intermediateResult; pred ← nodeToPredecessors(node)) yield pred
      val result = intermediateResult ++ preds
      if (result.size > intermediateResult.size) f(result) else result
    }
    f(Set(from)) - from
  }
}
