//
// Created by jia76 on 2020/11/10.
//

#include <jni.h>
#include <base.h>


//
// Created by huangyi on 2020/2/10.
//

#include <base.h>

/**
 * |Java    |JNI
 * |boolean |Z
 * |byte    |B
 * |char    |C
 * |short   |S
 * |int     |I
 * |long    |J
 * |float   |F
 * |double  |D
 */


/**
 * |Java      |JNI
 * |String    |Ljava/lang/String;
 * |Class     |Ljava/lang/Class;
 * |Throwable |Ljava/lang/Throwable;
 * |int[]     |[I
 * |Object[]  |[Ljava/lang/Object;
 */

/**
 * 通过 javap -s Animal.class 可以查看要回调方法的返回类型
 * D:\BaiduNetdiskDownload\work\MyApplication\app\build\intermediates\javac\debug\classes\com\xingkong\myapplication\model>javap -s Animal.class
Compiled from "Animal.java"
public class com.xingkong.myapplication.model.Animal {
  protected java.lang.String name;
    descriptor: Ljava/lang/String;
  public static int num;
    descriptor: I
  public com.xingkong.myapplication.model.Animal(java.lang.String);
    descriptor: (Ljava/lang/String;)V

  public java.lang.String getName();
    descriptor: ()Ljava/lang/String;

  public int getNum();
    descriptor: ()I

  public void callInstanceMethod(int);
    descriptor: (I)V

  public static java.lang.String callStaticMethod(java.lang.String);
    descriptor: (Ljava/lang/String;)Ljava/lang/String;

  public static java.lang.String callStaticMethod(java.lang.String[], int);
    descriptor: ([Ljava/lang/String;I)Ljava/lang/String;

  public static void callStaticVoidMethod();
    descriptor: ()V

  static {};
    descriptor: ()V
}

 *
 */


extern "C"
JNIEXPORT jint JNICALL
Java_com_xingkong_myapplication_jni_JNIAccessField_accessInstanceFiled(JNIEnv *env, jobject thiz,
                                                                       jobject animal) {

    //找到这个类
    jclass cls = env->GetObjectClass(animal);
    jfieldID fid = env->GetFieldID(cls, "name", "Ljava/lang/String;");//找到实例字段
    jstring str = env->NewStringUTF("this is new name");
    //重新设值
    env->SetObjectField(animal, fid, str);
    return JNI_OK;
}


extern "C"
JNIEXPORT jint JNICALL
Java_com_xingkong_myapplication_jni_JNIAccessField_accessStaticField(JNIEnv *env, jobject thiz,
                                                                     jobject animal) {
    jclass cls = env->GetObjectClass(animal);//找到类
    jfieldID fid = env->GetStaticFieldID(cls, "num", "I");//找到静态字段
    int num = env->GetStaticIntField(cls, fid);
    //重新设值
    env->SetStaticIntField(cls, fid, num + 1);
    return JNI_OK;
}




extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIAccessField_staticAccessInstanceField(JNIEnv *env,
                                                                             jclass clazz) {

    jfieldID fid = env->GetStaticFieldID(clazz, "num", "I");
    int num = env->GetStaticIntField(clazz, fid);
    //重新设值
    env->SetStaticIntField(clazz, fid, num + 1);
}


// Native 访问 Java 的类实例方法
extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIAccessField_callInstanceMethod(JNIEnv *env, jobject thiz,
                                                                      jobject animal) {


    jclass cls = env->GetObjectClass(animal);
    jmethodID mid = env->GetMethodID(cls, "callInstanceMethod", "(I)V");
    if (mid == NULL) {
    }
    env->CallVoidMethod(animal, mid, 2);


}




// Native 访问 Java 的静态方法
extern "C"
JNIEXPORT void JNICALL
Java_com_xingkong_myapplication_jni_JNIAccessField_callStaticMethod(JNIEnv *env, jobject thiz,
                                                                    jobject animal) {

    jclass cls = env->GetObjectClass(animal);
    jmethodID argsmid= nullptr;
//
//    jmethodID argsmid = env->GetStaticMethodID(cls, "callStaticMethod",
//                                               "(Ljava/lang/String;)Ljava/lang/String;");
//
//    if (argsmid == NULL) {
//    }
//
//    jstring jstr = env->NewStringUTF("jstring");
//
//    env->CallStaticObjectMethod(cls, argsmid, jstr);

    // ----

    argsmid = env->GetStaticMethodID(cls, "callStaticMethod",
                                     "([Ljava/lang/String;I)Ljava/lang/String;");
    if (argsmid == NULL) {
    }

    jobjectArray objArray;

    jclass objclass = env->FindClass("java/lang/String");

    if (objclass == NULL) {
    }

    int size = 100;
    objArray = env->NewObjectArray(size, objclass, nullptr);

    if (objArray == NULL) {
    }
    jstring strElement;
    for (int i = 0; i < size; ++i) {
        strElement = env->NewStringUTF("str in c");
        env->SetObjectArrayElement(objArray, i, strElement);
    }

    env->CallStaticObjectMethod(cls, argsmid, objArray, size);

}