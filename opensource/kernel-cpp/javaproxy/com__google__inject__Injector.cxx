// *** Generated by com.sos.scheduler.engine.cplusplus.generator ***

#include "_precompiled.h"

#include "com__google__inject__Injector.h"
#include "java__lang__Class.h"
#include "java__lang__Object.h"
#include "java__lang__String.h"

namespace javaproxy { namespace com { namespace google { namespace inject { 

struct Injector__class : ::zschimmer::javabridge::Class
{
    Injector__class(const string& class_name);
   ~Injector__class();

    ::zschimmer::javabridge::Method const _getInstance__Ljava_lang_Class_2__method;
    ::zschimmer::javabridge::Method const _getParent____method;
    ::zschimmer::javabridge::Method const _injectMembers__Ljava_lang_Object_2__method;

    static const ::zschimmer::javabridge::class_factory< Injector__class > class_factory;
};

const ::zschimmer::javabridge::class_factory< Injector__class > Injector__class::class_factory ("com.google.inject.Injector");

Injector__class::Injector__class(const string& class_name) :
    ::zschimmer::javabridge::Class(class_name)
    ,_getInstance__Ljava_lang_Class_2__method(this, "getInstance", "(Ljava/lang/Class;)Ljava/lang/Object;")
    ,_getParent____method(this, "getParent", "()Lcom/google/inject/Injector;")
    ,_injectMembers__Ljava_lang_Object_2__method(this, "injectMembers", "(Ljava/lang/Object;)V"){}

Injector__class::~Injector__class() {}




Injector::Injector(jobject jo) { if (jo) assign_(jo); }

Injector::Injector(const Injector& o) { assign_(o.get_jobject()); }

#ifdef Z_HAS_MOVE_CONSTRUCTOR
    Injector::Injector(Injector&& o) { set_jobject(o.get_jobject());  o.set_jobject(NULL); }
#endif

Injector::~Injector() { assign_(NULL); }




::javaproxy::java::lang::Object Injector::getInstance(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::Class >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Injector__class* cls = _class.get();
    ::javaproxy::java::lang::Object result;
    result.steal_local_ref(cls->_getInstance__Ljava_lang_Class_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::com::google::inject::Injector Injector::getParent() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Injector__class* cls = _class.get();
    ::javaproxy::com::google::inject::Injector result;
    result.steal_local_ref(cls->_getParent____method.jobject_call(get_jobject(), parameter_list));
    return result;
}

void Injector::injectMembers(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::Object >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Injector__class* cls = _class.get();
    cls->_injectMembers__Ljava_lang_Object_2__method.call(get_jobject(), parameter_list);
}


::zschimmer::javabridge::Class* Injector::java_object_class_() const { return _class.get(); }

::zschimmer::javabridge::Class* Injector::java_class_() { return Injector__class::class_factory.clas(); }


void Injector::Lazy_class::initialize() const {
    _value = Injector__class::class_factory.clas();
}


}}}}