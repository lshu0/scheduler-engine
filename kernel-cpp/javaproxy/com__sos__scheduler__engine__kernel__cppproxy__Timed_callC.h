// *** Generated by com.sos.scheduler.engine.cplusplus.generator ***

#ifndef _JAVAPROXY_COM_SOS_SCHEDULER_ENGINE_KERNEL_CPPPROXY_TIMED_CALLC_H_
#define _JAVAPROXY_COM_SOS_SCHEDULER_ENGINE_KERNEL_CPPPROXY_TIMED_CALLC_H_

#include "../zschimmer/zschimmer.h"
#include "../zschimmer/java.h"
#include "../zschimmer/Has_proxy.h"
#include "../zschimmer/javaproxy.h"
#include "../zschimmer/lazy.h"
#include "java__lang__Object.h"

namespace javaproxy { namespace java { namespace lang { struct Object; }}}
namespace javaproxy { namespace java { namespace lang { struct String; }}}


namespace javaproxy { namespace com { namespace sos { namespace scheduler { namespace engine { namespace kernel { namespace cppproxy { 


struct Timed_callC__class;

struct Timed_callC : ::zschimmer::javabridge::proxy_jobject< Timed_callC >, ::javaproxy::java::lang::Object {
  private:
    static Timed_callC new_instance();  // Not implemented
  public:

    Timed_callC(jobject = NULL);

    Timed_callC(const Timed_callC&);

    #ifdef Z_HAS_MOVE_CONSTRUCTOR
        Timed_callC(Timed_callC&&);
    #endif

    ~Timed_callC();

    Timed_callC& operator=(jobject jo) { assign_(jo); return *this; }
    Timed_callC& operator=(const Timed_callC& o) { assign_(o.get_jobject()); return *this; }
    #ifdef Z_HAS_MOVE_CONSTRUCTOR
        Timed_callC& operator=(Timed_callC&& o) { set_jobject(o.get_jobject()); o.set_jobject(NULL); return *this; }
    #endif

    jobject get_jobject() const { return ::zschimmer::javabridge::proxy_jobject< Timed_callC >::get_jobject(); }

  protected:
    void set_jobject(jobject jo) {
        ::zschimmer::javabridge::proxy_jobject< Timed_callC >::set_jobject(jo);
        ::javaproxy::java::lang::Object::set_jobject(jo);
    }
  public:


    ::zschimmer::javabridge::Class* java_object_class_() const;

    static ::zschimmer::javabridge::Class* java_class_();


  private:
    struct Lazy_class : ::zschimmer::abstract_lazy<Timed_callC__class*> {
        void initialize() const;
    };

    Lazy_class _class;
};


}}}}}}}

#endif