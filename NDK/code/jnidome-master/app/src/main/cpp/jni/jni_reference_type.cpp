//
// Created by jia76 on 2020/11/10.
//


//java类型	Native类型	符号属性	字长
//boolean	jboolean	无符号	8位
//byte	jbyte	有符号	8位
//char	jchar	无符号	16位
//short	jshort	有符号	16位
//int	jint	有符号	32位
//long	jlong	有符号	64位
//float	jfloat	有符号	32位
//double	jdouble	有符号	64位


//java类型	                      Native类型
// ALL Object                     jobject
//java.lang.Class	              jclass
//java.lang.Throwable	          jthrowable
//java.lang.String	              jstring
//java.lang.Object[]	          jobjectArray
//Boolean[]	                      jbooleanArray
//Byte[]	                      jbyteArray
//Char[]	                      jcharArray
//Short[]	                      jshortArray
//int[]	                          jintArray
//long[]	                      jlongArray
//float[]	                      jfloatArray
//double[]	                      jdoubleArray


#include <jni.h>
#include <base.h>
#include <cstdio>
extern "C"
JNIEXPORT jstring JNICALL
Java_com_xingkong_myapplication_jni_JNIReferenceType_callNativeStringArray(JNIEnv *env,jobject thiz,jobjectArray  str_array) {

    int  len=env->GetArrayLength(str_array); //这个方法对于所有的数组都是公用的
    LOGD( "len is %d ",len);
    jstring  firstStr= static_cast<jstring>(env->GetObjectArrayElement(str_array, 0));//拿到对象数组的首元素
//    const char* str:
//    意义：确保*str的内容不会改变，也就是用str这个指针无法改变str这个指针指向的地址的内容，
//    但是可以改变这个指针
//    char const str和const charstr含义一样
//    char* const str:
//    意义：确保str这个指针不会改变，但是这个指针里面的内容可以改变。
    const  char  *str=env->GetStringUTFChars(firstStr,0);
    LOGD( "len is %s ",str);
    env->ReleaseStringUTFChars(firstStr,str);
    return  env->NewStringUTF(str);
}

