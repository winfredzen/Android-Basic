//
// Created by jia76 on 2020/11/10.
//
#include <jni.h>
#include <base.h>
#include <cstdio>
#include <pthread.h>
extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIException_doit(JNIEnv *env, jobject thiz) {
    jthrowable exc;
    jobject obj;
    jclass cls=env->FindClass("com/xingkong/myapplication/jni/JNIException");
    jmethodID initMid = env->GetMethodID(cls, "<init>", "()V");
    obj = env->NewObject(cls, initMid);
    jmethodID mid = env->GetMethodID(cls, "callback", "()V");

    if (mid == NULL) {
        return;
    }
    env->CallVoidMethod(obj, mid);
    exc = env->ExceptionOccurred();
    if (exc) {
        env->ExceptionDescribe();
        env->ExceptionClear();
        // 发生异常了要记得释放资源
        env->DeleteLocalRef(cls);
        env->DeleteLocalRef(obj);

        //清空异常
        jclass newExcCls;
        newExcCls = env->FindClass("java/lang/IllegalArgumentException");
        if (newExcCls == NULL) {
            return;
        }
        env->ThrowNew(newExcCls, "Thrown from C++ code");
    }


}



extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIException_nativeThrowException(JNIEnv *env, jobject thiz) {

    jclass cls=env->FindClass("java/lang/IllegalArgumentException");
    env->ThrowNew(cls,"native throw exception");

    //  throwByName(env, "java/lang/IllegalArgumentException", "native throw exception");
}

/**
 * Native 代码调用 Java 的函数，引发了异常
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIException_nativeInvokeJavaException(JNIEnv *env,jobject thiz) {

    jclass cls=env->FindClass("com/xingkong/myapplication/jni/JNIException");
    jmethodID mid=env->GetMethodID(cls,"operation", "()I");
    jmethodID mid2 =env->GetMethodID(cls,"<init>", "()V");
    jobject obj=env->NewObject(cls,mid2);
    env->CallIntMethod(obj,mid);
    //检查是否发生了异常
    jthrowable  exc=env->ExceptionOccurred();
    if (exc){
        // 打印日志
        env->ExceptionDescribe();
        // 这代码才是关键不让应用崩溃的代码
        env->ExceptionClear();
        // 发生异常了要记得释放资源
        env->DeleteLocalRef(cls);
        env->DeleteLocalRef(obj);
    }

}
