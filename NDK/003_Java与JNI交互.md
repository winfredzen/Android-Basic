# Java与JNI交互 二

内容来自：

+ [Android CMake以及NDK实践基础](https://www.imooc.com/learn/1212)



参考如下的博客：

- [Android JNI 基本操作](https://glumes.com/post/android/android-jni-basic-operation/)
- [Android JNI 数组 操作](https://glumes.com/post/android/android-jni-array-operation/)
- [Android 通过 JNI 访问 Java 字段和方法调用](https://glumes.com/post/android/android-jni-access-field-and-method/)
- [Android 通过 JNI 调用 Java 类的构造方法和父类的方法](https://glumes.com/post/android/android-jni-invoke-constructor-method-and-super-method/)
- [Android JNI 调用时缓存字段和方法 ID](https://glumes.com/post/android/android-jni-cache-fieldid-and-methodid/)



## JNI访问Java

内容来自：

+ [Android 通过 JNI 访问 Java 字段和方法调用](https://glumes.com/post/android/android-jni-access-field-and-method/)

### 访问字段

Native 方法访问 Java 的字段有两种形式，分别是访问类的实例字段和访问类的静态字段。

不管哪种操作，首先要定义一个具体的 Java 类型，其中，有实例的字段类型和方法，也有静态的字段类型和方法。

```java
public class Animal {
    protected String name;
    public static int num = 0;
    public Animal(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    public int getNum() {
        return num;
    }
}
```

#### 访问类的实例字段

访问 Java 类的字段，大致步骤如下：

1. 获取 Java 对象的类
2. 获取对应字段的 id
3. 获取具体的字段值



以访问以上 Animal 类的 name 字段，并将其修改为例：

```java
private native void accessInstanceFiled(Animal animal);
```

对应的 C++ 代码如下：

```c++
extern "C"
JNIEXPORT void JNICALL
Java_com_glumes_cppso_jnioperations_FieldAndMethodOps_accessInstanceFiled(JNIEnv *env,jobject instance, jobject animal) {
    jfieldID fid; // 想要获取的字段 id
    jstring jstr; // 字段对应的具体的值
    const char *str; // 将 Java 的字符串转换为 Native 的字符串
    jclass cls = env->GetObjectClass(animal); // 获取 Java 对象的类
    fid = env->GetFieldID(cls, "name", "Ljava/lang/String;"); // 获取对应字段的 id
    if (fid == NULL) { // 如果字段为 NULL ，直接退出，查找失败
        return;
    }
    jstr = (jstring) env->GetObjectField(animal, fid); // 获取字段对应的值
    str = env->GetStringUTFChars(jstr, NULL);
    if (str == NULL) {
        return;
    }
    LOGD("name is %s", str);
    env->ReleaseStringUTFChars(jstr, str);
    jstr = env->NewStringUTF("replaced name");
    if (jstr == NULL) {
        return;
    }
    env->SetObjectField(animal, fid, jstr); // 修改字段对应的值 
}
```



在上面的代码中，首先通过 `GetObjectClass` 函数获取对应的 Java 类，其参数就是要获得的对象类型 jobject ，然后得到的结果就是一个 jclass 类型的值，代表 Java 的 Class 类型。

其次是通过 `GetFieldID` 方法获得 Java 类型对应的字段 id 。其中，第一个参数就是之前获得的 Java 类型，第二个参数就是在 Java 中字段的具体名字，第三个参数就是字段对应的具体类型，这个类型的签名描述要转换成 Native 的表示形式，也就是之前提到的 Java 和 Native 的签名转换。

得到了 Java 类型和字段的 id 后，就可以通过 `GetObjectField` 方法来获取具体的值，它的两个参数分别是之前获得的 Java 类型和字段 id 。

`GetObjectField` 方法有很多形态，对于字段值是引用类型的，统一是 `GetObjectField`，然后得到的结果转型为想要的类型。对于基础类型，则有则对应的方法，比如 `GetBooleanField`、`GetIntField`、`GetDoubleField` 等等。

得到了字段的值之后，就可以进行想要的操作了。

最后，还可以通过 `SetObjectField` 方法来修改字段对应的值。它的前两个参数也是对应的 Java 类型和字段 id，最后的参数则是具体的值，此方法也是针对于字段类型是引用类型，而对于基础类型，也有着对应的方法，比如 `SetBooleanField`、`SetCharField`、`SetDoubleField`。



#### 访问类的静态字段

访问类的静态字段，大致步骤和类的实例字段类似：

```java
private native void accessStaticField(Animal animal);
```

对应的 C++ 代码如下：

```cpp
extern "C"
JNIEXPORT void JNICALL
Java_com_glumes_cppso_jnioperations_FieldAndMethodOps_accessStaticField(JNIEnv *env, jobject instance,jobject animal) {
    jfieldID fid;
    jint num;
    jclass cls = env->GetObjectClass(animal);
    fid = env->GetStaticFieldID(cls, "num", "I");
    if (fid == NULL) {
        return;
    }
    num = env->GetStaticIntField(cls, fid);
    LOGD("get static field num is %d", num);
    env->SetStaticIntField(cls, fid, ++num);
}
```



类的静态和实例字段的访问最大不同就在于，JNI 调用对应的方法不同。对于类的静态字段，JNI 的方法多了 `Static` 的标志来表明这个对应于类的静态字段访问。



### 方法调用

JNI 调用 Java 方法和 JNI 访问 Java 字段的步骤也大致相同，

1. 获取 Java 对象的类
2. 获取对应方法的 id
3. 调用具体的方法

以调用类的实例方法和静态方法为例：

#### 调用类的实例方法

JNI 调用 Java 类的实例方法

```java
    private native void callInstanceMethod(Animal animal);
```

对应 C++ 代码如下：

```cpp
// Native 访问 Java 的类实例方法
extern "C"
JNIEXPORT void JNICALL
Java_com_glumes_cppso_jnioperations_FieldAndMethodOps_callInstanceMethod(JNIEnv *env, jobject instance,jobject animal) {
    jclass cls = env->GetObjectClass(animal); // 获得具体的类
    jmethodID mid = env->GetMethodID(cls, "callInstanceMethod", "(I)V"); // 获得具体的方法 id
    if (mid == NULL) {
        return;
    }
    env->CallVoidMethod(animal, mid, 2); // 调用方法
}
```



与访问字段不同的是，`GetFieldID` 方法换成了 `GetMethodID` 方法，另外由 `CallVoidMethod` 函数来调用具体的方法，前面两个参数是获得的类和方法 id，最后的参数是具体调用方法的参数。

`GetMethodID` 方法的第一个参数就是具体的 Java 类型，第二个参数是该 Java 类的对应实例方法的名称，第三个参数就是该方法对应的返回类型和参数签名转换成 Native 对应的描述。

对于不需要返回值的函数，调用 `CallVoidMethod` 即可，对于返回值为引用类型的，调用 `CallObjectMethod` 方法，对于返回基础类型的方法，则有各自对应的方法调用，比如：`CallBooleanMethod`、`CallShortMethod`、`CallDoubleMethod` 等等。



#### 调用类的静态方法

对于调用类的静态方法和调用类的实例方法类似：

```java
    private native void callStaticMethod(Animal animal);
```

对应 C++ 代码如下：

```cpp
// Native 访问 Java 的静态方法
extern "C"
JNIEXPORT void JNICALL
Java_com_glumes_cppso_jnioperations_FieldAndMethodOps_callStaticMethod(JNIEnv *env,jobject instance, jobject animal) {
    jclass cls = env->GetObjectClass(animal);
    jmethodID argsmid = env->GetStaticMethodID(cls, "callStaticMethod",
                                               "(Ljava/lang/String;)Ljava/lang/String;");
    if (argsmid == NULL) {
        return;
    }
    jstring jstr = env->NewStringUTF("jstring");
    env->CallStaticObjectMethod(cls, argsmid, jstr);
```



调用类的静态方法 `callStaticMethod`，该方法需要传递一个 String 字符串参数，同时返回一个字符串参数。

具体的调用过程和调用类的实例方法类似，差别也只是在于调用方法名多加了一个 Static 的标识。





## JNI 调用 Java 类的构造方法和父类的方法

内容来自：

+ [Android 通过 JNI 调用 Java 类的构造方法和父类的方法](https://glumes.com/post/android/android-jni-invoke-constructor-method-and-super-method/)



Android 还可以通过 JNI 来调用 Java 一个类的构造方法，从而创建一个 Java 类。



### 调用构造方法

调用构造方法的步骤和之前调用类的实例方法步骤类似，也需要获得对应的类和方法 id。

对于类，通过 FindClass 可以找到对应的 Java 类型。

对于构造方法，它的方法 id 还是通过 `GetMethodID` 方法来获得，但是构造方法对应的名称为`<init>` ，返回值类型是 void 类型的。

完成了以上准备条件后，就可以通过 `NewObject` 来调用构造方法，从而创建具体的类。

下面以 String 的某个构造方法为例

```java
public String(char value[]) // Java String 类的其中一个构造方法
```

对应的 C++ 代码：

```c++
extern "C"
JNIEXPORT jstring JNICALL
Java_com_glumes_cppso_jnioperations_InvokeConstructorOps_invokeStringConstructors(JNIEnv *env, jobject instance) {

    jclass stringClass;
    jmethodID cid;
    jcharArray elemArr;
    jstring result;

    // 由 C++ 字符串创建一个 Java 字符串
    jstring temp = env->NewStringUTF("this is char array");
    // 再从 Java 字符串获得一个字符数组指针，作为 String 构造函数的参数
    const jchar *chars = env->GetStringChars(temp, NULL);
    int len = 10;

    stringClass = env->FindClass("java/lang/String"); // 找到具体的 String 类
    if (stringClass == NULL) {
        return NULL;
    }
    // 找到具体的方法，([C)V 表示选择 String 的 String(char value[]) 构造方法
    cid = env->GetMethodID(stringClass, "<init>", "([C)V");
    if (cid == NULL) {
        return NULL;
    }
    // 字符串数组作为参数
    elemArr = env->NewCharArray(len);
    if (elemArr == NULL) {
        return NULL;
    }
    // 给字符串数组赋值
    env->SetCharArrayRegion(elemArr, 0, len, chars);
    // 创建类
    result = (jstring) env->NewObject(stringClass, cid, elemArr);
    env->DeleteLocalRef(elemArr);
    env->DeleteLocalRef(stringClass);
    return result;
}
```

由于 String 的构造函数需要传递一个字符数组，就先构造好了字符数组并赋值，得到对应的类和方法 id 之后，直接通过 `NewObject` 方法调用即可。

再来看一个调用自定义类的构造方法的示例，还是之前的 Animal 类，它的构造方法有一个 String 类型的参数。

```cpp
/**
 * 创建一个 Java 的 Animal 类并返回
 */
extern "C"
JNIEXPORT jobject JNICALL
Java_com_glumes_cppso_jnioperations_InvokeConstructorOps_invokeAnimalConstructors(JNIEnv *env, jobject instance) {
    jclass animalClass;
    jmethodID mid;
    jobject result;
    animalClass = env->FindClass("com/glumes/cppso/model/Animal");
    if (animalClass == NULL) {
        return NULL;
    }
    mid = env->GetMethodID(animalClass, "<init>", "(Ljava/lang/String;)V");
    if (mid == NULL) {
        return NULL;
    }
    jstring args = env->NewStringUTF("this animal name");
    result = env->NewObject(animalClass, mid, args);
    env->DeleteLocalRef(animalClass);
    return result;
}
```

可以看到，整个调用流程只要按照步骤来，就可以了。

除了 `NewObject` 方法之外，JNI 还提供了 `AllocObject` 方法来创建对象，以同样调用 Animal 类构造方法为例：

```cpp
/**
 * 通过 AllocObject 方法来创建一个类
 */
extern "C"
JNIEXPORT jobject JNICALL
Java_com_glumes_cppso_jnioperations_InvokeConstructorOps_allocObjectConstructor(JNIEnv *env, jobject instance) {
    jclass animalClass;
    jobject result;
    jmethodID mid;
    // 获得对应的 类
    animalClass = env->FindClass("com/glumes/cppso/model/Animal");
    if (animalClass == NULL) {
        return NULL;
    }
    // 获得构造方法 id
    mid = env->GetMethodID(animalClass, "<init>", "(Ljava/lang/String;)V");
    if (mid == NULL) {
        return NULL;
    }
    // 构造方法的参数
    jstring args = env->NewStringUTF("use AllocObject");
    // 创建对象，此时创建的对象未初始化的对象
    result = env->AllocObject(animalClass);
    if (result == NULL) {
        return NULL;
    }
    // 调用 CallNonvirtualVoidMethod 方法去调用类的构造方法
    env->CallNonvirtualVoidMethod(result, animalClass, mid, args);
    if (env->ExceptionCheck()) {
        env->DeleteLocalRef(result);
        return NULL;
    }
    return result;
}
```

同样的，要先准备必要的东西。获得对应类的类型、方法 id、构造方法的参数。

然后通过 `AllocObject` 方法创建对象，但要注意的是，此时创建的对象是未被初始化的，不同于 `NewObject` 方法创建的对象直接就是初始化了，在一定程度上，可以说 `AllocObject` 方法是延迟初始化的。

接下来是要通过 `CallNonvirtualVoidMethod` 来调用对应的构造方法。此处传入的一个参数不再是 jclass 类型，而是创建的未被初始化的类 jobject 。

通过这种方法，同样可以创建一个 Java 中的类。



### 调用父类的方法

可以通过 JNI 来调用父类的实例方法。

在子类中通过调用 `CallNonvirtual<Type>Method` 方法来调用父类的方法。

首先，构造一个相应的子类，然后获得父类的 类型和方法 id，以及准备对应的参数，根据父类方法的返回值选择调用不同的 `CallNonvirtual<Type>Method` 函数。

对于引用类型的，调用 `CallNonvirtualObjectMethod` 方法；对于基础类型的，调用 `CallNonvirtualBooleanMethod`、`CallNonvirtualIntMethod` 等等；对于无返回值类型的，调用 `CallNonvirtualVoidMethod` 方法。

具体看代码：

```cpp
/**
 * 调用父类的方法
 * 创建一个子类，由子类去调用父类的方法
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_glumes_cppso_jnioperations_InvokeConstructorOps_callSuperMethod(JNIEnv *env, jobject instance) {
    jclass cat_cls; // Cat 类的类型
    jmethodID cat_cid; // Cat 类的构造方法 id
    jstring cat_name; // Cat 类的构造方法参数
    jobject cat;
    // 获得对应的 类
    cat_cls = env->FindClass("com/glumes/cppso/model/Cat");
    if (cat_cls == NULL) {
        return;
    }
    // 获得构造方法 id
    cat_cid = env->GetMethodID(cat_cls, "<init>", "(Ljava/lang/String;)V");
    if (cat_cid == NULL) {
        return;
    }
    // 准备构造方法的参数
    cat_name = env->NewStringUTF("this is cat name");
    // 创建 Cat 类
    cat = env->NewObject(cat_cls, cat_cid, cat_name);
    if (cat == NULL) {
        return;
    }
    //调用父类的 getName 参数
    jclass animal_cls; // 父类的类型
    jmethodID animal_mid; // 被调用的父类的方法 id
    // 获得父类对应的类
    animal_cls = env->FindClass("com/glumes/cppso/model/Animal");
    if (animal_cls == NULL) {
        return;
    }
    // 获得父类被调用的方法 id
    animal_mid = env->GetMethodID(animal_cls, "getName", "()Ljava/lang/String;");
    if (animal_mid == NULL) {
        return;
    }
    jstring name = (jstring) env->CallNonvirtualObjectMethod(cat, animal_cls, animal_mid);
    if (name == NULL) {
        return;
    }
    LOGD("getName method value is %s", env->GetStringUTFChars(name, NULL));

    // 调用父类的其他方法
    animal_mid = env->GetMethodID(animal_cls, "callInstanceMethod", "(I)V");
    if (animal_mid == NULL) {
        return;
    }
    env->CallNonvirtualVoidMethod(cat, animal_cls, animal_mid);
}
```

Cat 类作为 Animal 类的子类，首先由 NewObject 方法创建 Cat 类，然后调用它的父类的方法。



























































