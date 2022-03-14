# Java与JNI交互

内容来自：

+ [Android CMake以及NDK实践基础](https://www.imooc.com/learn/1212)



参考如下的博客：

- [Android JNI 基本操作](https://glumes.com/post/android/android-jni-basic-operation/)
- [Android JNI 数组 操作](https://glumes.com/post/android/android-jni-array-operation/)
- [Android 通过 JNI 访问 Java 字段和方法调用](https://glumes.com/post/android/android-jni-access-field-and-method/)
- [Android 通过 JNI 调用 Java 类的构造方法和父类的方法](https://glumes.com/post/android/android-jni-invoke-constructor-method-and-super-method/)
- [Android JNI 调用时缓存字段和方法 ID](https://glumes.com/post/android/android-jni-cache-fieldid-and-methodid/)



## 在Java中调用JNI方法

![019](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/019.png)

现在ide，可以自动提示了，不用记住方面命名的规则了

![020](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/020.png)

```c++
extern "C"
JNIEXPORT jstring JNICALL
Java_com_wz_myapplication_MainActivity_getString(JNIEnv *env, jobject thiz) {
    // TODO: implement getString()
}
```

![021](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/021.png)

可以看到`jobject thiz`指的就是`MainActivity`

>     extern "C"的主要作用就是为了能够正确实现C++代码调用其他C语言代码。加上extern "C"后，会指示编译器这部分代码按C语言的进行编译，而不是C++的。由于C++支持函数重载，因此编译器编译函数的过程中会将函数的参数类型也加到编译后的代码中，而不仅仅是函数名；而C语言并不支持函数重载，因此编译C语言代码的函数时不会带上函数的参数类型，一般之包括函数名。



### 函数的动态注册

静态注册 - 上面的例子就是静态注册

动态注册 - 动态注册是在JNi层实现的，JAVA层不需要关心，因为在`system.load`时就会去调用`JNI_OnLoad`，有就注册，没有就不注册。动态注册的原理：JNI 允许我们提供一个函数映射表，注册给 JVM，这样 JVM 就可以用函数映射表来调用相应的函数， 而不必通过函数名来查找相关函数(这个查找效率很低，函数名超级长)流程更加清晰可控，效率更高.。
java层通过`System.loadLibrary()`方法可以加载一个动态库,此时虚拟机就会调用jni库中的`JNI_OnLoad()`函数

```c++
jint JNI_OnLoad(JavaVM* vm, void* reserved);
```

返回值代表，动态库需要的**jni版本**，如果虚拟机不能识别这个版本，那么就不可以加载这个动态库

动态注册基本流程

1. 编写Java端的相关native方法
2. 编写C/C++代码, 实现`JNI_Onload()`方法
3. 将Java 方法和 C/C++方法通过签名信息一一对应起来
4. 通过`JavaVM`获取`JNIEnv`, `JNIEnv`主要用于获取Java类和调用一些JNI提供的方法
5. 使用类名和对应起来的方法作为参数, 调用JNI提供的函数`RegisterNatives()`注册方法



`JNI_Onload()`方法，这样写

```c++
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved)
```

`RegisterNatives`方法

```c++
//注册本地方法,第一个是方法对应的类，第二个是方法映射，第三个是映射方法的个数
jint RegisterNatives(jclass clazz, const JNINativeMethod* methods,
        jint nMethods)
```

`JNINativeMethod`方法的说明

```c++
typedef struct {  
    const char* name;     // java层对应的方法名称  
    const char* signature;// 该方法的返回值类型和参数类型  
    void*       fnPtr;    // native中对应的函数指针  
} JNINativeMethod;  
```





通过视频中的例子来说明

1.定义Java类

```java
public class JNIDynamicLoad {
    static {
        System.loadLibrary("dynamic-lib");
    }
    public native int sum(int x, int y);

    public native String getNativeString();
}
```



2.cpp类，`jni_dynamic_load.cpp`

```c++
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
```



3.修改`CMakeLists.txt`

![022](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/022.png)



4.验证

调用`jniDynamicLoad.getNativeString()`

```java
tv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        JNIDynamicLoad jniDynamicLoad = new JNIDynamicLoad();
        tv.setText(jniDynamicLoad.getNativeString());
    }
});
```

![023](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/023.png)



### JNI基础数据类型的转换

对应基础类型字段的转换：

| Java 类型 | JNI 对应的描述转 |
| :-------- | :--------------- |
| boolean   | Z                |
| byte      | B                |
| char      | C                |
| short     | S                |
| int       | I                |
| long      | J                |
| float     | F                |
| double    | D                |

`JNIBasicType.java`

```java
public class JNIBasicType {
    static {
        System.loadLibrary("dynamic-lib");
    }

    public native int callNativeInt(int num);

    private native byte callNativeByte(byte b);

    private native char callNativeChar(char ch);

    private native short callNativeShort(short sh);

    private native long callNativeLong(long l);

    private native float callNativeFloat(float f);

    private native double callNativeDouble(double d);

    private native boolean callNativeBoolean(boolean value);
}

```

`jni_basic_type.cpp`

```java
extern "C"
JNIEXPORT jint JNICALL
Java_com_wz_myapplication_jni_JNIBasicType_callNativeInt(JNIEnv *env, jobject thiz,
                                                               jint num) {
//    LOGD("JAVA int value is %d", num);
    jint c_num=num *2;
    return c_num;
}

extern "C"
JNIEXPORT jbyte JNICALL
Java_com_wz_myapplication_jni_JNIBasicType_callNativeByte(JNIEnv *env, jobject thiz,
                                                                jbyte b) {
//    LOGD("java byte value is %d", b);
    jbyte c_byte = b + (jbyte) 10;
    return c_byte;
}


extern "C"
JNIEXPORT jchar JNICALL
Java_com_wz_myapplication_jni_JNIBasicType_callNativeChar(JNIEnv *env, jobject thiz,
                                                                jchar ch) {

//    LOGD("java char value is %c", ch);
    jchar c_char = ch + (jchar) 3;
    return c_char;
}

extern "C"
JNIEXPORT jshort JNICALL
Java_com_wz_myapplication_jni_JNIBasicType_callNativeShort(JNIEnv *env, jobject thiz,
                                                                 jshort sh) {
//    LOGD("java char value is %d", sh);
    jshort c_short = sh + (jshort) 10;
    return c_short;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wz_myapplication_jni_JNIBasicType_callNativeLong(JNIEnv *env, jobject thiz,
                                                                jlong l) {
//    LOGD("java long value is %ld", l);
    jlong c_long = l + 100;
    return c_long;
}


extern "C"
JNIEXPORT jfloat JNICALL
Java_com_wz_myapplication_jni_JNIBasicType_callNativeFloat(JNIEnv *env, jobject thiz,
                                                                 jfloat f) {
//    LOGD("java float value is %f", f);
    jfloat c_float = f + (jfloat) 10.0;
    return c_float;
}


extern "C"
JNIEXPORT jdouble JNICALL
Java_com_wz_myapplication_jni_JNIBasicType_callNativeDouble(JNIEnv *env, jobject thiz,
                                                                  jdouble d) {
//    LOGD("java double value is %f", d);
    jdouble c_double = d + 20.0;
    return c_double;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_wz_myapplication_jni_JNIBasicType_callNativeBoolean(JNIEnv *env, jobject thiz,
                                                                   jboolean value) {
//    LOGD("java boolean value is %d", value);
    jboolean c_bool = (jboolean) !value;
    return c_bool;
}
```





























