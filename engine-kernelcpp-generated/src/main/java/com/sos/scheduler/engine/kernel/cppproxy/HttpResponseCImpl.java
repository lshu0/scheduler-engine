// *** Generated by com.sos.scheduler.engine.cplusplus.generator ***

package com.sos.scheduler.engine.kernel.cppproxy;

@javax.annotation.Generated("C++/Java-Generator - SOS GmbH Berlin")
@SuppressWarnings({"unchecked", "rawtypes"})
final class HttpResponseCImpl
extends com.sos.scheduler.engine.cplusplus.runtime.CppProxyImpl<com.sos.scheduler.engine.cplusplus.runtime.Sister>
implements com.sos.scheduler.engine.kernel.cppproxy.HttpResponseC {

    // <editor-fold defaultstate="collapsed" desc="Generated code - DO NOT EDIT">

    private HttpResponseCImpl(com.sos.scheduler.engine.cplusplus.runtime.Sister context) { // Nur für JNI zugänglich
        requireContextIsNull(context);
    }

    @Override public void Release() {
        com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.lock();
        try {
            Release__native(cppReference());
        }
        catch (Exception x) { throw com.sos.scheduler.engine.cplusplus.runtime.CppProxies.propagateCppException(x, this); }
        finally {
            com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.unlock();
        }
    }

    private static native void Release__native(long cppReference);


    @Override public com.sos.scheduler.engine.kernel.cppproxy.HttpChunkReaderC chunk_reader() {
        com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.lock();
        try {
            com.sos.scheduler.engine.kernel.cppproxy.HttpChunkReaderC result = chunk_reader__native(cppReference());
            checkIsNotReleased(com.sos.scheduler.engine.kernel.cppproxy.HttpChunkReaderC.class, result);
            return result;
        }
        catch (Exception x) { throw com.sos.scheduler.engine.cplusplus.runtime.CppProxies.propagateCppException(x, this); }
        finally {
            com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.unlock();
        }
    }

    private static native com.sos.scheduler.engine.kernel.cppproxy.HttpChunkReaderC chunk_reader__native(long cppReference);


    @Override public void close() {
        com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.lock();
        try {
            close__native(cppReference());
        }
        catch (Exception x) { throw com.sos.scheduler.engine.cplusplus.runtime.CppProxies.propagateCppException(x, this); }
        finally {
            com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.unlock();
        }
    }

    private static native void close__native(long cppReference);


    @Override public java.lang.String header_string() {
        com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.lock();
        try {
            java.lang.String result = header_string__native(cppReference());
            checkIsNotReleased(java.lang.String.class, result);
            return result;
        }
        catch (Exception x) { throw com.sos.scheduler.engine.cplusplus.runtime.CppProxies.propagateCppException(x, this); }
        finally {
            com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.unlock();
        }
    }

    private static native java.lang.String header_string__native(long cppReference);


    @Override public int status() {
        com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.lock();
        try {
            return status__native(cppReference());
        }
        catch (Exception x) { throw com.sos.scheduler.engine.cplusplus.runtime.CppProxies.propagateCppException(x, this); }
        finally {
            com.sos.scheduler.engine.cplusplus.runtime.CppProxy.threadLock.unlock();
        }
    }

    private static native int status__native(long cppReference);


    // </editor-fold>
}
