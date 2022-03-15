//
// Created by jia76 on 2020/11/9.
//

#include <jni.h>
#include <base.h>


extern "C"
JNIEXPORT jint JNICALL
Java_com_xingkong_myapplication_jni_JNIBasicType_callNativeInt(JNIEnv *env, jobject thiz,
                                                               jint num) {
    LOGD("JAVA int value is %d",num);
    int c_num=num *2;
    return c_num;
}

extern "C"
JNIEXPORT jbyte JNICALL
Java_com_xingkong_myapplication_jni_JNIBasicType_callNativeByte(JNIEnv *env, jobject thiz,
                                                                jbyte b) {
    LOGD("java byte value is %d", b);
    jbyte c_byte = b + (jbyte) 10;
    return c_byte;
}


extern "C"
JNIEXPORT jchar JNICALL
Java_com_xingkong_myapplication_jni_JNIBasicType_callNativeChar(JNIEnv *env, jobject thiz,
                                                                jchar ch) {

    LOGD("java char value is %c", ch);
    jchar c_char = ch + (jchar) 3;
    return c_char;
}

extern "C"
JNIEXPORT jshort JNICALL
Java_com_xingkong_myapplication_jni_JNIBasicType_callNativeShort(JNIEnv *env, jobject thiz,
                                                                 jshort sh) {
    LOGD("java char value is %d", sh);
    jshort c_short = sh + (jshort) 10;
    return c_short;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_xingkong_myapplication_jni_JNIBasicType_callNativeLong(JNIEnv *env, jobject thiz,
                                                                jlong l) {
    LOGD("java long value is %ld", l);
    jlong c_long = l + 100;
    return c_long;
}


extern "C"
JNIEXPORT jfloat JNICALL
Java_com_xingkong_myapplication_jni_JNIBasicType_callNativeFloat(JNIEnv *env, jobject thiz,
                                                                 jfloat f) {
    LOGD("java float value is %f", f);
    jfloat c_float = f + (jfloat) 10.0;
    return c_float;
}


extern "C"
JNIEXPORT jdouble JNICALL
Java_com_xingkong_myapplication_jni_JNIBasicType_callNativeDouble(JNIEnv *env, jobject thiz,
                                                                  jdouble d) {
    LOGD("java double value is %f", d);
    jdouble c_double = d + 20.0;
    return c_double;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_xingkong_myapplication_jni_JNIBasicType_callNativeBoolean(JNIEnv *env, jobject thiz,
                                                                   jboolean value) {
    LOGD("java boolean value is %d", value);
    jboolean c_bool = (jboolean) !value;
    return c_bool;
}

