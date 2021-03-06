// *** Generated by com.sos.scheduler.engine.cplusplus.generator ***

#include "_precompiled.h"

#include "org__w3c__dom__Document.h"
#include "java__lang__Object.h"
#include "java__lang__String.h"
#include "org__w3c__dom__Attr.h"
#include "org__w3c__dom__CDATASection.h"
#include "org__w3c__dom__Comment.h"
#include "org__w3c__dom__Element.h"
#include "org__w3c__dom__Node.h"
#include "org__w3c__dom__NodeList.h"
#include "org__w3c__dom__Text.h"

namespace javaproxy { namespace org { namespace w3c { namespace dom { 

struct Document__class : ::zschimmer::javabridge::Class
{
    Document__class(const string& class_name);
   ~Document__class();

    ::zschimmer::javabridge::Method const _adoptNode__Lorg_w3c_dom_Node_2__method;
    ::zschimmer::javabridge::Method const _createAttribute__Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _createAttributeNS__Ljava_lang_String_2Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _createCDATASection__Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _createComment__Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _createElement__Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _createElementNS__Ljava_lang_String_2Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _createTextNode__Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _getDocumentElement____method;
    ::zschimmer::javabridge::Method const _getDocumentURI____method;
    ::zschimmer::javabridge::Method const _getElementById__Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _getElementsByTagName__Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _getElementsByTagNameNS__Ljava_lang_String_2Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _getInputEncoding____method;
    ::zschimmer::javabridge::Method const _getStrictErrorChecking____method;
    ::zschimmer::javabridge::Method const _getXmlEncoding____method;
    ::zschimmer::javabridge::Method const _getXmlStandalone____method;
    ::zschimmer::javabridge::Method const _getXmlVersion____method;
    ::zschimmer::javabridge::Method const _importNode__Lorg_w3c_dom_Node_2Z__method;
    ::zschimmer::javabridge::Method const _normalizeDocument____method;
    ::zschimmer::javabridge::Method const _renameNode__Lorg_w3c_dom_Node_2Ljava_lang_String_2Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _setDocumentURI__Ljava_lang_String_2__method;
    ::zschimmer::javabridge::Method const _setStrictErrorChecking__Z__method;
    ::zschimmer::javabridge::Method const _setXmlStandalone__Z__method;
    ::zschimmer::javabridge::Method const _setXmlVersion__Ljava_lang_String_2__method;

    static const ::zschimmer::javabridge::class_factory< Document__class > class_factory;
};

const ::zschimmer::javabridge::class_factory< Document__class > Document__class::class_factory ("org.w3c.dom.Document");

Document__class::Document__class(const string& class_name) :
    ::zschimmer::javabridge::Class(class_name)
    ,_adoptNode__Lorg_w3c_dom_Node_2__method(this, "adoptNode", "(Lorg/w3c/dom/Node;)Lorg/w3c/dom/Node;")
    ,_createAttribute__Ljava_lang_String_2__method(this, "createAttribute", "(Ljava/lang/String;)Lorg/w3c/dom/Attr;")
    ,_createAttributeNS__Ljava_lang_String_2Ljava_lang_String_2__method(this, "createAttributeNS", "(Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/Attr;")
    ,_createCDATASection__Ljava_lang_String_2__method(this, "createCDATASection", "(Ljava/lang/String;)Lorg/w3c/dom/CDATASection;")
    ,_createComment__Ljava_lang_String_2__method(this, "createComment", "(Ljava/lang/String;)Lorg/w3c/dom/Comment;")
    ,_createElement__Ljava_lang_String_2__method(this, "createElement", "(Ljava/lang/String;)Lorg/w3c/dom/Element;")
    ,_createElementNS__Ljava_lang_String_2Ljava_lang_String_2__method(this, "createElementNS", "(Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/Element;")
    ,_createTextNode__Ljava_lang_String_2__method(this, "createTextNode", "(Ljava/lang/String;)Lorg/w3c/dom/Text;")
    ,_getDocumentElement____method(this, "getDocumentElement", "()Lorg/w3c/dom/Element;")
    ,_getDocumentURI____method(this, "getDocumentURI", "()Ljava/lang/String;")
    ,_getElementById__Ljava_lang_String_2__method(this, "getElementById", "(Ljava/lang/String;)Lorg/w3c/dom/Element;")
    ,_getElementsByTagName__Ljava_lang_String_2__method(this, "getElementsByTagName", "(Ljava/lang/String;)Lorg/w3c/dom/NodeList;")
    ,_getElementsByTagNameNS__Ljava_lang_String_2Ljava_lang_String_2__method(this, "getElementsByTagNameNS", "(Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/NodeList;")
    ,_getInputEncoding____method(this, "getInputEncoding", "()Ljava/lang/String;")
    ,_getStrictErrorChecking____method(this, "getStrictErrorChecking", "()Z")
    ,_getXmlEncoding____method(this, "getXmlEncoding", "()Ljava/lang/String;")
    ,_getXmlStandalone____method(this, "getXmlStandalone", "()Z")
    ,_getXmlVersion____method(this, "getXmlVersion", "()Ljava/lang/String;")
    ,_importNode__Lorg_w3c_dom_Node_2Z__method(this, "importNode", "(Lorg/w3c/dom/Node;Z)Lorg/w3c/dom/Node;")
    ,_normalizeDocument____method(this, "normalizeDocument", "()V")
    ,_renameNode__Lorg_w3c_dom_Node_2Ljava_lang_String_2Ljava_lang_String_2__method(this, "renameNode", "(Lorg/w3c/dom/Node;Ljava/lang/String;Ljava/lang/String;)Lorg/w3c/dom/Node;")
    ,_setDocumentURI__Ljava_lang_String_2__method(this, "setDocumentURI", "(Ljava/lang/String;)V")
    ,_setStrictErrorChecking__Z__method(this, "setStrictErrorChecking", "(Z)V")
    ,_setXmlStandalone__Z__method(this, "setXmlStandalone", "(Z)V")
    ,_setXmlVersion__Ljava_lang_String_2__method(this, "setXmlVersion", "(Ljava/lang/String;)V"){}

Document__class::~Document__class() {}




Document::Document(jobject jo) { if (jo) assign_(jo); }

Document::Document(const Document& o) { assign_(o.get_jobject()); }

#ifdef Z_HAS_MOVE_CONSTRUCTOR
    Document::Document(Document&& o) { set_jobject(o.get_jobject());  o.set_jobject(NULL); }
#endif

Document::~Document() { assign_(NULL); }




::javaproxy::org::w3c::dom::Node Document::adoptNode(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::org::w3c::dom::Node >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Node result;
    result.steal_local_ref(cls->_adoptNode__Lorg_w3c_dom_Node_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::Attr Document::createAttribute(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Attr result;
    result.steal_local_ref(cls->_createAttribute__Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::Attr Document::createAttributeNS(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p1) const {
    ::zschimmer::javabridge::raw_parameter_list<2> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    parameter_list._jvalues[1].l = p1.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Attr result;
    result.steal_local_ref(cls->_createAttributeNS__Ljava_lang_String_2Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::CDATASection Document::createCDATASection(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::CDATASection result;
    result.steal_local_ref(cls->_createCDATASection__Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::Comment Document::createComment(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Comment result;
    result.steal_local_ref(cls->_createComment__Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::Element Document::createElement(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Element result;
    result.steal_local_ref(cls->_createElement__Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::Element Document::createElementNS(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p1) const {
    ::zschimmer::javabridge::raw_parameter_list<2> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    parameter_list._jvalues[1].l = p1.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Element result;
    result.steal_local_ref(cls->_createElementNS__Ljava_lang_String_2Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::Text Document::createTextNode(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Text result;
    result.steal_local_ref(cls->_createTextNode__Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::Element Document::getDocumentElement() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Element result;
    result.steal_local_ref(cls->_getDocumentElement____method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::java::lang::String Document::getDocumentURI() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Document__class* cls = _class.get();
    ::javaproxy::java::lang::String result;
    result.steal_local_ref(cls->_getDocumentURI____method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::Element Document::getElementById(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Element result;
    result.steal_local_ref(cls->_getElementById__Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::NodeList Document::getElementsByTagName(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::NodeList result;
    result.steal_local_ref(cls->_getElementsByTagName__Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::NodeList Document::getElementsByTagNameNS(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p1) const {
    ::zschimmer::javabridge::raw_parameter_list<2> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    parameter_list._jvalues[1].l = p1.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::NodeList result;
    result.steal_local_ref(cls->_getElementsByTagNameNS__Ljava_lang_String_2Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::java::lang::String Document::getInputEncoding() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Document__class* cls = _class.get();
    ::javaproxy::java::lang::String result;
    result.steal_local_ref(cls->_getInputEncoding____method.jobject_call(get_jobject(), parameter_list));
    return result;
}

bool Document::getStrictErrorChecking() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Document__class* cls = _class.get();
    return 0 != cls->_getStrictErrorChecking____method.bool_call(get_jobject(), parameter_list);
}

::javaproxy::java::lang::String Document::getXmlEncoding() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Document__class* cls = _class.get();
    ::javaproxy::java::lang::String result;
    result.steal_local_ref(cls->_getXmlEncoding____method.jobject_call(get_jobject(), parameter_list));
    return result;
}

bool Document::getXmlStandalone() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Document__class* cls = _class.get();
    return 0 != cls->_getXmlStandalone____method.bool_call(get_jobject(), parameter_list);
}

::javaproxy::java::lang::String Document::getXmlVersion() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Document__class* cls = _class.get();
    ::javaproxy::java::lang::String result;
    result.steal_local_ref(cls->_getXmlVersion____method.jobject_call(get_jobject(), parameter_list));
    return result;
}

::javaproxy::org::w3c::dom::Node Document::importNode(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::org::w3c::dom::Node >& p0, jboolean p1) const {
    ::zschimmer::javabridge::raw_parameter_list<2> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    parameter_list._jvalues[1].z = p1;
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Node result;
    result.steal_local_ref(cls->_importNode__Lorg_w3c_dom_Node_2Z__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

void Document::normalizeDocument() const {
    ::zschimmer::javabridge::raw_parameter_list<0> parameter_list;
    Document__class* cls = _class.get();
    cls->_normalizeDocument____method.call(get_jobject(), parameter_list);
}

::javaproxy::org::w3c::dom::Node Document::renameNode(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::org::w3c::dom::Node >& p0, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p1, const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p2) const {
    ::zschimmer::javabridge::raw_parameter_list<3> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    parameter_list._jvalues[1].l = p1.get_jobject();
    parameter_list._jvalues[2].l = p2.get_jobject();
    Document__class* cls = _class.get();
    ::javaproxy::org::w3c::dom::Node result;
    result.steal_local_ref(cls->_renameNode__Lorg_w3c_dom_Node_2Ljava_lang_String_2Ljava_lang_String_2__method.jobject_call(get_jobject(), parameter_list));
    return result;
}

void Document::setDocumentURI(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    cls->_setDocumentURI__Ljava_lang_String_2__method.call(get_jobject(), parameter_list);
}

void Document::setStrictErrorChecking(jboolean p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].z = p0;
    Document__class* cls = _class.get();
    cls->_setStrictErrorChecking__Z__method.call(get_jobject(), parameter_list);
}

void Document::setXmlStandalone(jboolean p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].z = p0;
    Document__class* cls = _class.get();
    cls->_setXmlStandalone__Z__method.call(get_jobject(), parameter_list);
}

void Document::setXmlVersion(const ::zschimmer::javabridge::proxy_jobject< ::javaproxy::java::lang::String >& p0) const {
    ::zschimmer::javabridge::raw_parameter_list<1> parameter_list;
    parameter_list._jvalues[0].l = p0.get_jobject();
    Document__class* cls = _class.get();
    cls->_setXmlVersion__Ljava_lang_String_2__method.call(get_jobject(), parameter_list);
}


::zschimmer::javabridge::Class* Document::java_object_class_() const { return _class.get(); }

::zschimmer::javabridge::Class* Document::java_class_() { return Document__class::class_factory.clas(); }


void Document::Lazy_class::initialize() const {
    _value = Document__class::class_factory.clas();
}


}}}}
