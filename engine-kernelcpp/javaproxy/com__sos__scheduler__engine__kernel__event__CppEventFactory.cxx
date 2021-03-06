// *** Generated by com.sos.scheduler.engine.cplusplus.generator ***

#include "_precompiled.h"

#include "com__sos__scheduler__engine__kernel__event__CppEventFactory.h"
#include "com__sos__scheduler__engine__data__event__AbstractEvent.h"
#include "java__lang__Object.h"
#include "java__lang__String.h"

namespace javaproxy { namespace com { namespace sos { namespace scheduler { namespace engine { namespace kernel { namespace event { 

struct CppEventFactory__class : ::zschimmer::javabridge::Class
{
    CppEventFactory__class(const string& class_name);
   ~CppEventFactory__class();

    ::zschimmer::javabridge::Static_method const _newLogEvent__ILjava_lang_String_2__method;
    ::zschimmer::javabridge::Static_method const _newOrderStateChangedEvent__Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Static_method const _newOrderStepEndedEvent__Ljava_lang_String_2Ljava_lang_String_2J__method;
    ::zschimmer::javabridge::Static_method const _newTaskEndedEvent__ILjava_lang_String_2I__method;

    static const ::zschimmer::javabridge::class_factory< CppEventFactory__class > class_factory;
};

const ::zschimmer::javabridge::class_factory< CppEventFactory__class > CppEventFactory__class::class_factory ("com.sos.scheduler.engine.kernel.event.CppEventFactory");

CppEventFactory__class::CppEventFactory__class(const string& class_name) :
    ::zschimmer::javabridge::Class(class_name)
    ,_newLogEvent__ILjava_lang_String_2__method(this, "newLogEvent", "(ILjava/lang/String;)Lcom/sos/scheduler/engine/data/event/AbstractEvent;")
    ,_newOrderStateChangedEvent__Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2__method(this, "newOrderStateChangedEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/sos/scheduler/engine/data/event/AbstractEvent;")
    ,_newOrderStepEndedEvent__Ljava_lang_String_2Ljava_lang_String_2J__method(this, "newOrderStepEndedEvent", "(Ljava/lang/String;Ljava/lang/String;J)Lcom/sos/scheduler/engine/data/event/AbstractEvent;")
    ,_newTaskEndedEvent__ILjava_lang_String_2I__method(this, "newTaskEndedEvent", "(ILjava/lang/String;I)Lcom/sos/scheduler/engine/data/event/AbstractEvent;"){}

CppEventFactory__class::~CppEventFactory__class() {}




CppEventFactory::CppEventFactory(jobject jo) { if (jo) assign_(jo); }

CppEventFactory::CppEventFactory(const CppEventFactory& o) { assign_(o.get_jobject()); }

#ifdef Z_HAS_MOVE_CONSTRUCTOR
    CppEventFactory::CppEventFactory(CppEventFactory&& o) { set_jobject(o.get_jobject());  o.set_jobject(NULL); }
#endif

CppEventFactory::~CppEventFactory() { assign_(NULL); }




::javaproxy::com::sos::scheduler::engine::data::event::AbstractEvent CppEventFactory::newLogEvent(jint p0, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p1) {
    ::zschimmer::javabridge::raw_parameter_list<2> parameter_list;
    parameter_list._jvalues[0].i = p0;
    parameter_list._jvalues[1].l = p1.get_jobject();
    CppEventFactory__class* cls = CppEventFactory__class::class_factory.clas();
    ::javaproxy::com::sos::scheduler::engine::data::event::AbstractEvent result;
    result.steal_local_ref(cls->_newLogEvent__ILjava_lang_String_2__method.jobject_call(cls->get_jclass(), parameter_list));
    return result;
}

::javaproxy::com::sos::scheduler::engine::data::event::AbstractEvent CppEventFactory::newOrderStateChangedEvent(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p1, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p2, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p3) {
    ::zschimmer::javabridge::raw_parameter_list<4> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    parameter_list._jvalues[1].l = p1.get_jobject();
    parameter_list._jvalues[2].l = p2.get_jobject();
    parameter_list._jvalues[3].l = p3.get_jobject();
    CppEventFactory__class* cls = CppEventFactory__class::class_factory.clas();
    ::javaproxy::com::sos::scheduler::engine::data::event::AbstractEvent result;
    result.steal_local_ref(cls->_newOrderStateChangedEvent__Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2Ljava_lang_String_2__method.jobject_call(cls->get_jclass(), parameter_list));
    return result;
}

::javaproxy::com::sos::scheduler::engine::data::event::AbstractEvent CppEventFactory::newOrderStepEndedEvent(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p1, jlong p2) {
    ::zschimmer::javabridge::raw_parameter_list<3> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    parameter_list._jvalues[1].l = p1.get_jobject();
    parameter_list._jvalues[2].j = p2;
    CppEventFactory__class* cls = CppEventFactory__class::class_factory.clas();
    ::javaproxy::com::sos::scheduler::engine::data::event::AbstractEvent result;
    result.steal_local_ref(cls->_newOrderStepEndedEvent__Ljava_lang_String_2Ljava_lang_String_2J__method.jobject_call(cls->get_jclass(), parameter_list));
    return result;
}

::javaproxy::com::sos::scheduler::engine::data::event::AbstractEvent CppEventFactory::newTaskEndedEvent(jint p0, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p1, jint p2) {
    ::zschimmer::javabridge::raw_parameter_list<3> parameter_list;
    parameter_list._jvalues[0].i = p0;
    parameter_list._jvalues[1].l = p1.get_jobject();
    parameter_list._jvalues[2].i = p2;
    CppEventFactory__class* cls = CppEventFactory__class::class_factory.clas();
    ::javaproxy::com::sos::scheduler::engine::data::event::AbstractEvent result;
    result.steal_local_ref(cls->_newTaskEndedEvent__ILjava_lang_String_2I__method.jobject_call(cls->get_jclass(), parameter_list));
    return result;
}


::zschimmer::javabridge::Class* CppEventFactory::java_object_class_() const { return _class.get(); }

::zschimmer::javabridge::Class* CppEventFactory::java_class_() { return CppEventFactory__class::class_factory.clas(); }


void CppEventFactory::Lazy_class::initialize() const {
    _value = CppEventFactory__class::class_factory.clas();
}


}}}}}}}
