// *** Generated by com.sos.scheduler.engine.cplusplus.generator ***

#include "_precompiled.h"

#include "com__sos__scheduler__engine__kernel__cppproxy__NodeC.h"
#include "java__lang__Object.h"
#include "java__lang__String.h"

namespace javaproxy { namespace com { namespace sos { namespace scheduler { namespace engine { namespace kernel { namespace cppproxy { 

struct NodeC__class : ::zschimmer::javabridge::Class
{
    NodeC__class(const string& class_name);
   ~NodeC__class();


    static const ::zschimmer::javabridge::class_factory< NodeC__class > class_factory;
};

const ::zschimmer::javabridge::class_factory< NodeC__class > NodeC__class::class_factory ("com.sos.scheduler.engine.kernel.cppproxy.NodeC");

NodeC__class::NodeC__class(const string& class_name) :
    ::zschimmer::javabridge::Class(class_name)
{}

NodeC__class::~NodeC__class() {}




NodeC::NodeC(jobject jo) { if (jo) assign_(jo); }

NodeC::NodeC(const NodeC& o) { assign_(o.get_jobject()); }

#ifdef Z_HAS_MOVE_CONSTRUCTOR
    NodeC::NodeC(NodeC&& o) { set_jobject(o.get_jobject());  o.set_jobject(NULL); }
#endif

NodeC::~NodeC() { assign_(NULL); }





::zschimmer::javabridge::Class* NodeC::java_object_class_() const { return _class.get(); }

::zschimmer::javabridge::Class* NodeC::java_class_() { return NodeC__class::class_factory.clas(); }


void NodeC::Lazy_class::initialize() const {
    _value = NodeC__class::class_factory.clas();
}


}}}}}}}