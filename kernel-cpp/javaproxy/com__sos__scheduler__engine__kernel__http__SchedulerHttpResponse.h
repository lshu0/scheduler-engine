// *** Generated by com.sos.scheduler.engine.cplusplus.generator ***

#ifndef _JAVAPROXY_COM_SOS_SCHEDULER_ENGINE_KERNEL_HTTP_SCHEDULERHTTPRESPONSE_H_
#define _JAVAPROXY_COM_SOS_SCHEDULER_ENGINE_KERNEL_HTTP_SCHEDULERHTTPRESPONSE_H_

#include "../zschimmer/zschimmer.h"
#include "../zschimmer/java.h"
#include "../zschimmer/Has_proxy.h"
#include "../zschimmer/javaproxy.h"
#include "../zschimmer/lazy.h"
#include "java__lang__Object.h"

namespace javaproxy { namespace java { namespace lang { struct Object; }}}
namespace javaproxy { namespace java { namespace lang { struct String; }}}


namespace javaproxy { namespace com { namespace sos { namespace scheduler { namespace engine { namespace kernel { namespace http { 


struct SchedulerHttpResponse__class;

struct SchedulerHttpResponse : ::zschimmer::javabridge::proxy_jobject< SchedulerHttpResponse >, ::javaproxy::java::lang::Object {
  private:
    static SchedulerHttpResponse new_instance();  // Not implemented
  public:

    SchedulerHttpResponse(jobject = NULL);

    SchedulerHttpResponse(const SchedulerHttpResponse&);

    #ifdef Z_HAS_MOVE_CONSTRUCTOR
        SchedulerHttpResponse(SchedulerHttpResponse&&);
    #endif

    ~SchedulerHttpResponse();

    SchedulerHttpResponse& operator=(jobject jo) { assign_(jo); return *this; }
    SchedulerHttpResponse& operator=(const SchedulerHttpResponse& o) { assign_(o.get_jobject()); return *this; }
    #ifdef Z_HAS_MOVE_CONSTRUCTOR
        SchedulerHttpResponse& operator=(SchedulerHttpResponse&& o) { set_jobject(o.get_jobject()); o.set_jobject(NULL); return *this; }
    #endif

    jobject get_jobject() const { return ::zschimmer::javabridge::proxy_jobject< SchedulerHttpResponse >::get_jobject(); }

  protected:
    void set_jobject(jobject jo) {
        ::zschimmer::javabridge::proxy_jobject< SchedulerHttpResponse >::set_jobject(jo);
        ::javaproxy::java::lang::Object::set_jobject(jo);
    }
  public:

    void onNextChunkIsReady() const;

    ::zschimmer::javabridge::Class* java_object_class_() const;

    static ::zschimmer::javabridge::Class* java_class_();


  private:
    struct Lazy_class : ::zschimmer::abstract_lazy<SchedulerHttpResponse__class*> {
        void initialize() const;
    };

    Lazy_class _class;
};


}}}}}}}

#endif