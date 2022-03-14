//
// Created by 王振 on 2022/3/14.
//

#include <base.h>
#include <jvm.h>
#include <jni.h>
#include <string>
//#include <android/log.h>

//类名字符串
#define JAVA_CLASS "com/wz/myapplication/load/JNIDynamicLoad"

//Java Native方法的实现
//前两个参数要是JNIEnv, jobject
jstring getString(JNIEnv *env, jobject jobj) {
    return env->NewStringUTF("this is msg");
}

jint plus(JNIEnv *env, jobject jobj, int x, int y) {
    return x + y;
}

/**
 * 此函数通过调用JNI中 RegisterNatives 方法来注册我们的函数
 * @param env
 * @param className
 * @param methods
 * @param nMethods
 * @return
 */
static int registerNativeMethods(JNIEnv *env, const char* className, const JNINativeMethod* methods, jint nMethods) {
    jclass clazz;
    //找到声明native方法的类
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    //返回成功就等于0
    if (env->RegisterNatives(clazz, methods, nMethods) < 0) {
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

/**
 * 需要注册的函数列表，放在JNINativeMethod类型的数组中，以后如果需要增加函数，只需在这里添加就行了
 * 参数：
 * 1、java代码中用native关键字声明的函数名字符串
 * 2、签名（传进来参数类型和返回值类型的说明）
 * 3、C/C++中对应函数的函数名（地址）
 */
static JNINativeMethod getMethods[] = {
        {"sum",    "(II)I",                  (void *) plus},
        {"getNativeString", "()Ljava/lang/String;", (void *) getString}
};

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    //获取JNIEnv
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_FALSE;
    }
    //注册
    registerNativeMethods(env, JAVA_CLASS, getMethods, 2);
    //这里打印有问题
//    LOGD("jni onload call");
//    std::string hello = "Hello from C++";
//    __android_log_print(ANDROID_LOG_INFO,"msg","patch path:%s","hello world");
    return JNI_VERSION_1_6;
}