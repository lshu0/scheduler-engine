package com.sos.scheduler.engine.kernel.job

import com.sos.scheduler.engine.common.guice.GuiceImplicits._
import com.sos.scheduler.engine.common.scalautil.Collections.emptyToNone
import com.sos.scheduler.engine.cplusplus.runtime.annotation.ForCpp
import com.sos.scheduler.engine.cplusplus.runtime.{Sister, SisterType}
import com.sos.scheduler.engine.data.filebased.FileBasedType
import com.sos.scheduler.engine.data.job.{JobOverview, JobPath, JobState, TaskPersistentState}
import com.sos.scheduler.engine.data.processclass.ProcessClassPath
import com.sos.scheduler.engine.kernel.async.SchedulerThreadFutures.schedulerThreadFuture
import com.sos.scheduler.engine.kernel.cppproxy.JobC
import com.sos.scheduler.engine.kernel.filebased.FileBased
import com.sos.scheduler.engine.kernel.scheduler.HasInjector
import com.sos.scheduler.engine.kernel.time.CppTimeConversions._
import java.time.Instant

@ForCpp
final class Job(protected[this] val cppProxy: JobC, protected val subsystem: JobSubsystem)
extends FileBased
with Sister
with UnmodifiableJob
with JobPersistence {

  type ThisPath = JobPath

  def onCppProxyInvalidated(): Unit = {}

  def fileBasedType = FileBasedType.job

  def stringToPath(o: String) = JobPath(o)

  override def overview = JobOverview(path, fileBasedState, defaultProcessClassPathOption, state,
    isInPeriod = isInPeriod, taskLimit = taskMaximum, usedTaskCount = runningTasksCount)

  def defaultProcessClassPathOption = emptyToNone(cppProxy.default_process_class_path) map ProcessClassPath.apply

  def isInPeriod = cppProxy.is_in_period

  def taskMaximum = cppProxy.max_tasks

  def runningTasksCount = cppProxy.running_tasks_count

  def title: String = cppProxy.title

  def description: String = cppProxy.description

  def scriptText: String = cppProxy.script_text()

  def state = JobState.valueOf(cppProxy.state_name)

  def stateText = cppProxy.state_text

  def needsProcess: Boolean = cppProxy.waiting_for_process

  protected def nextStartInstantOption: Option[Instant] =
    eternalCppMillisToNoneInstant(cppProxy.next_start_time_millis)

  def endTasks(): Unit = {
    setStateCommand(JobStateCommand.endTasks)
  }

  def setStateCommand(c: JobStateCommand): Unit = {
    schedulerThreadFuture { cppProxy.set_state_cmd(c.cppValue) } (schedulerThreadCallQueue)
  }

  def isPermanentlyStopped =
    cppProxy.is_permanently_stopped

  private[job] def enqueueTaskPersistentState(t: TaskPersistentState): Unit = {
    cppProxy.enqueue_taskPersistentState(t)
  }
}


object Job {
  final class Type extends SisterType[Job, JobC] {
    def sister(proxy: JobC, context: Sister) = {
      val injector = context.asInstanceOf[HasInjector].injector
      new Job(proxy, injector.instance[JobSubsystem])
    }
  }
}
