//
// Created by jia76 on 2020/11/9.
//

#include <jni.h>
#include <base.h>
#include <cstdio>
#include <jvm.h>

#define JAVA_CLASS "com/xingkong/myapplication/jni/JNIDynamicLoad"
jstring  getMessage(JNIEnv *env, jobject job){
    return env->NewStringUTF("THIS IS MAG");
}

jint plus(JNIEnv *env, jobject job,int x,int y){
    return x+y;
}

extern "C"
jstring Java_Package_MyTester_NMethod
        (JNIEnv *env, jobject obj) {
    jint number = 23456;
    char str[20];
    sprintf(str, "%d",number);
    return env->NewStringUTF(str);
}

static JNINativeMethod gMethods[]={
        {"getNativeString", "()Ljava/lang/String;", (void*)getMessage},
        {"sum", "(II)I", (void*)plus},
        };

int registerNativeMethods(JNIEnv *env, const char* name,
                          const JNINativeMethod* methods,
                          jint nMethods
                       ){
    jclass clazz;
    clazz=env->FindClass(name);
    if (clazz == nullptr){
        return JNI_FALSE;
    }

    if (env->RegisterNatives(clazz,methods,nMethods) < 0){
        return JNI_FALSE;
    }
    return JNI_OK;

}

JNIEXPORT int JNICALL JNI_OnLoad (JavaVM *vm , void *reserved){
   JNIEnv *env;
   if(vm ->GetEnv(reinterpret_cast<void **>(&env),JNI_VERSION_1_6)!=JNI_OK){
       return JNI_FALSE;
   }
    setJvm(vm);
   //sizeof(gMethods)/sizeof(gMethods[0])
    registerNativeMethods(env,JAVA_CLASS,gMethods,sizeof(gMethods)/sizeof(gMethods[0]));



    LOGD("jni onLoad call" );
    return JNI_VERSION_1_6;
}



