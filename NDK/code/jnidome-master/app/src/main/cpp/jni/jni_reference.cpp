#include <jni.h>
#include <base.h>
#include <cstdio>


/**
 * 局部引用
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xingkong_myapplication_jni_JNIReference_errorCacheLocalReference(JNIEnv *env,jobject thiz) {
   jclass localRefs=env->FindClass("java/lang/String");
   jmethodID  mid=env->GetMethodID(localRefs,"<init>","(Ljava/lang/String;)V");
   jstring str=env->NewStringUTF("string");
   //局部引用如果引用过多程序是不能全部释放 需要手动及时释放
//    for (int i = 0; i <1000 ; ++i) {
//      jclass cls=env->FindClass("java/lang/String");
//
//      env->DeleteLocalRef(cls);
//
//    }
    return static_cast<jstring>(env->NewObject(localRefs, mid, str));
}
/**
 * 全局引用
 * 可以全局缓存
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xingkong_myapplication_jni_JNIReference_cacheWithGlocalReference(JNIEnv *env,jobject thiz) {
    static jclass stringClass= nullptr;
    if (stringClass== nullptr){
        jclass cls=env->FindClass("java/lang/String");
        stringClass= static_cast<jclass>(env->NewGlobalRef(cls));
        env->DeleteLocalRef(cls);
    } else{
        LOGD("USER CACHED");
    }
    jmethodID  mid=env->GetMethodID(stringClass,"<init>","(Ljava/lang/String;)V");
    jstring str=env->NewStringUTF("string");
    return static_cast<jstring>(env->NewObject(stringClass, mid, str));
}
/**
 * 弱引用
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xingkong_myapplication_jni_JNIReference_useWeakGlocalReference(JNIEnv *env, jobject thiz) {

    static jclass stringClass= nullptr;
    if (stringClass== nullptr){
        jclass cls=env->FindClass("java/lang/String");
        stringClass= static_cast<jclass>(env->NewWeakGlobalRef(cls));
        env->DeleteLocalRef(cls);
    } else{
        LOGD("USER CACHED");
    }
    jmethodID  mid=env->GetMethodID(stringClass,"<init>","(Ljava/lang/String;)V");
    jboolean isGc =env->IsSameObject(stringClass, nullptr);
    if(!isGc){
        jstring str=env->NewStringUTF("string");
        return static_cast<jstring>(env->NewObject(stringClass, mid, str));
    } else{
        jstring str=env->NewStringUTF("弱引用被释放");
        return static_cast<jstring>(env->NewObject(stringClass, mid, str));
    }
}