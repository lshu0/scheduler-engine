// *** Generated by com.sos.scheduler.engine.cplusplus.generator ***

#include "_precompiled.h"

#include "com__sos__scheduler__engine__kernel__job__Job.h"
#include "com__sos__scheduler__engine__data__job__JobPersistentState.h"
#include "com__sos__scheduler__engine__kernel__filebased__FileBased.h"
#include "java__lang__Object.h"
#include "java__lang__String.h"
#include "scala__Option.h"

namespace javaproxy { namespace com { namespace sos { namespace scheduler { namespace engine { namespace kernel { namespace job { 

struct Job__class : ::zschimmer::javabridge::Class
{
    Job__class(const string& class_name);
   ~Job__class();

    ::zschimmer::javabridge::Method const _deletePersistedTask__I__method;
    ::zschimmer::javabridge::Method const _deletePersistentState____method;
    ::zschimmer::javabridge::Method const _loadPersistentTasks____method;
    ::zschimmer::javabridge::Method const _persistEnqueuedTask__IJJLjava_lang_String_2Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _persistState____method;
    ::zschimmer::javabridge::Method const _tryFetchAverageStepDuration____method;
    ::zschimmer::javabridge::Method const _tryFetchPersistentState____method;

    static const ::zschimmer::javabridge::class_factory< Job__class > class_factory;
};

const ::zschimmer::javabridge::class_factory< Job__class > Job__class::class_factory ("com.sos.scheduler.engine.kernel.job.Job");

Job__class::Job__class(const string& class_name) :
    ::zschimmer::javabridge::Class(class_name)
    ,_deletePersistedTask__I__method(this, "deletePersistedTask", "(I)V")
    ,_deletePersistentState____method(this, "deletePersistentState", "()V")
    ,_loadPersistentTasks____method(this, "loadPersistentTasks", "()V")
    ,_persistEnqueuedTask__IJJLjava_lang_String_2Ljava_lang_String_2__method(this, "persistEnqueuedTask", "(IJJLjava/lang/String;Ljava/lang/String;)V")
    ,_persistState____method(this, "persistState", "()V")
    ,_tryFetchAverageStepDuration____method(this, "tryFetchAverageStepDuration", "()Lscala/Option;")
    ,_tryFetchPersistentState____method(this, "tryFetchPersistentState", "()Lcom/sos/scheduler/engine/data/job/JobPersistentState;"){}

Job__class::~Job__class() {}




Job::Job(jobject jo) { if (jo) assign_(jo); }

Job::Job(const Job& o) { assign_(o.get_jobject()); }

#ifdef Z_HAS_MOVE_CONSTRUCTOR
    Job::Job(Job&& o) { set_jobject(o.get_jobject());  o.set_jobject(NULL); }
#endif

Job::~Job() { assign_(NULL); }




void Job::deletePersistedTask(jint p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].i = p0;
    Job__class* cls = _class.get();
    cls->_deletePersistedTask__I__method.call(get_jobject(), parameter_list);
}

void Job::deletePersistentState() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Job__class* cls = _class.get();
    cls->_deletePersistentState____method.call(get_jobject(), parameter_list);
}

void Job::loadPersistentTasks() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Job__class* cls = _class.get();
    cls->_loadPersistentTasks____method.call(get_jobject(), parameter_list);
}

void Job::persistEnqueuedTask(jint p0, jlong p1, jlong p2, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p3, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p4) const {
    ::zschimmer::javabridge::raw_parameter_list<5> parameter_list;
    parameter_list._jvalues[0].i = p0;
    parameter_list._jvalues[1].j = p1;
    parameter_list._jvalues[2].j = p2;
    parameter_list._jvalues[3].l = p3.get_jobject();
    parameter_list._jvalues[4].l = p4.get_jobject();
    Job__class* cls = _class.get();
    cls->_persistEnqueuedTask__IJJLjava_lang_String_2Ljava_lang_String_2__method.call(get_jobject(), parameter_list);
}

void Job::persistState() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Job__class* cls = _class.get();
    cls->_persistState____method.call(get_jobject(), parameter_list);
}

::javaproxy::scala::Option Job::tryFetchAverageStepDuration() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Job__class* cls = _class.get();
    ::javaproxy::scala::Option result;
    result.steal_local_ref(cls->_tryFetchAverageStepDuration____method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::com::sos::scheduler::engine::data::job::JobPersistentState Job::tryFetchPersistentState() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Job__class* cls = _class.get();
    ::javaproxy::com::sos::scheduler::engine::data::job::JobPersistentState result;
    result.steal_local_ref(cls->_tryFetchPersistentState____method.jobject_call(get_jobject(), parameter_list));
    return result;
}


::zschimmer::javabridge::Class* Job::java_object_class_() const { return _class.get(); }

::zschimmer::javabridge::Class* Job::java_class_() { return Job__class::class_factory.clas(); }


void Job::Lazy_class::initialize() const {
    _value = Job__class::class_factory.clas();
}


}}}}}}}