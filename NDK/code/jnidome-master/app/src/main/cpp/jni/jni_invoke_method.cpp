//
// Created by jia76 on 2020/11/10.
//
#include <jni.h>
#include <base.h>
#include <cstdio>
#include <pthread.h>
#include <jvm.h>

static jclass threadClazz;
static jmethodID threadMethod;
static jobject threadObject;

extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIInvokeMethod_NativeCallback(JNIEnv *env, jobject instance,
        jobject callback) {
     LOGD("nativeCallback");
     jclass  callbackClazz= env->GetObjectClass(callback);
     jmethodID callbackMethod=env->GetMethodID(callbackClazz,"Callback","()V");
     env->CallVoidMethod(callback,callbackMethod);
}

void *threadCallback(void *){
    //env 是不能跨线程
    JavaVM *gVM=getJvm();
    JNIEnv *env= nullptr;
    if (gVM->AttachCurrentThread(&env, nullptr)==0){
        env->CallVoidMethod(threadObject,threadMethod);
        gVM->DetachCurrentThread();
    }
    return JNI_OK;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIInvokeMethod_NativeThreadCallback(JNIEnv *env, jobject instance,
                                                                         jobject callback) {
   //env 是不能跨线程的
   threadClazz =env->GetObjectClass(callback);
   threadMethod=env->GetMethodID(threadClazz,"Callback","()V");
   threadObject =env->NewGlobalRef(callback);
   pthread_t handle;
   pthread_create(&handle, nullptr, threadCallback, nullptr);
}
