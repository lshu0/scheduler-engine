// *** Generated by com.sos.scheduler.engine.cplusplus.generator ***

#ifndef _JAVAPROXY_COM_SOS_SCHEDULER_ENGINE_KERNEL_ORDER_JOBCHAIN_SIMPLEJOBNODE_H_
#define _JAVAPROXY_COM_SOS_SCHEDULER_ENGINE_KERNEL_ORDER_JOBCHAIN_SIMPLEJOBNODE_H_

#include "../zschimmer/zschimmer.h"
#include "../zschimmer/java.h"
#include "../zschimmer/Has_proxy.h"
#include "../zschimmer/javaproxy.h"
#include "../zschimmer/lazy.h"
#include "com__sos__scheduler__engine__kernel__order__jobchain__JobNode.h"
#include "com__sos__scheduler__engine__kernel__order__jobchain__Node.h"
#include "com__sos__scheduler__engine__kernel__order__jobchain__OrderQueueNode.h"
#include "java__lang__Object.h"

namespace javaproxy { namespace com { namespace sos { namespace scheduler { namespace engine { namespace kernel { namespace order { namespace jobchain { struct JobNode; }}}}}}}}
namespace javaproxy { namespace java { namespace lang { struct String; }}}


namespace javaproxy { namespace com { namespace sos { namespace scheduler { namespace engine { namespace kernel { namespace order { namespace jobchain { 


struct SimpleJobNode__class;

struct SimpleJobNode : ::zschimmer::javabridge::proxy_jobject< SimpleJobNode >, ::javaproxy::com::sos::scheduler::engine::kernel::order::jobchain::JobNode {
  private:
    static SimpleJobNode new_instance();  // Not implemented
  public:

    SimpleJobNode(jobject = NULL);

    SimpleJobNode(const SimpleJobNode&);

    #ifdef Z_HAS_MOVE_CONSTRUCTOR
        SimpleJobNode(SimpleJobNode&&);
    #endif

    ~SimpleJobNode();

    SimpleJobNode& operator=(jobject jo) { assign_(jo); return *this; }
    SimpleJobNode& operator=(const SimpleJobNode& o) { assign_(o.get_jobject()); return *this; }
    #ifdef Z_HAS_MOVE_CONSTRUCTOR
        SimpleJobNode& operator=(SimpleJobNode&& o) { set_jobject(o.get_jobject()); o.set_jobject(NULL); return *this; }
    #endif

    jobject get_jobject() const { return ::zschimmer::javabridge::proxy_jobject< SimpleJobNode >::get_jobject(); }

  protected:
    void set_jobject(jobject jo) {
        ::zschimmer::javabridge::proxy_jobject< SimpleJobNode >::set_jobject(jo);
        ::javaproxy::com::sos::scheduler::engine::kernel::order::jobchain::JobNode::set_jobject(jo);
    }
  public:


    ::zschimmer::javabridge::Class* java_object_class_() const;

    static ::zschimmer::javabridge::Class* java_class_();


  private:
    struct Lazy_class : ::zschimmer::abstract_lazy<SimpleJobNode__class*> {
        void initialize() const;
    };

    Lazy_class _class;
};


}}}}}}}}

#endif
