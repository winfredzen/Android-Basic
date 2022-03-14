#include <jni.h>
#include <string>
//#include "people/People.h"
#include <People.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_wz_myapplication_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    People people;
    return env->NewStringUTF(people.getString().c_str());
//    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_wz_myapplication_MainActivity_getString(JNIEnv *env, jobject thiz) {
    // TODO: implement getString()
}