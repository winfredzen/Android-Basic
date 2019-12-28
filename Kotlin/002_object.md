# object

[object](https://www.kotlincn.net/docs/reference/object-declarations.html)是一个关键字

> Sometimes we need to create an object of a slight modification of some class, without explicitly declaring a new subclass for it. Kotlin handles this case with *object expressions* and *object declarations*.
>
> 有时，我们需要创建一个对某些类稍加修改的对象，而无需为其显式声明一个新的子类。 Kotlin通过对象表达式和对象声明来处理这种情况。

这是说了2个：*object expressions* 和 *object declarations*

与java的匿名内部类相似

参考：

+ [对象表达式与对象声明](https://www.kotlincn.net/docs/reference/object-declarations.html)
+ [Kotlin中的object 与companion object的区别](https://www.jianshu.com/p/14db81e1576a)

## Object expressions

创建一个继承自某个（或某些）类型的匿名类的对象

```kotlin
val textView = findViewById<TextView>(R.id.tv)
textView.setOnClickListener(object : OnClickListener {
        override fun onClick(p0: View?) {
            Toast.makeText(this@TestActivity, "点击事件生效", Toast.LENGTH_LONG)
        }

})
```

## Object declarations

用`object` 修饰的类为静态类，里面的方法和变量都为`静态`的。

**直接声明类**

```kotlin
object DemoManager {
    private val TAG = "DemoManager"
        
    fun a() {
        Log.e(TAG,"此时 object 表示 声明静态内部类")
    }
    
}
```

**声明静态内部类**

类内部的对象声明，没有被`inner` 修饰的内部类都是静态的

```kotlin
class DemoManager{
    object MyObject {
        fun a() {
            Log.e(TAG,"此时 object 表示 直接声明类")
        }
    }
}
```

如果需要调用 `a()`方法

1.kotlin中调用

```kotlin
fun init() {
    MyObject.a()
}
```

2.java中调用

```java
 MyObject.INSTANCE.a();
```



## Companion Object

`companion object` 修饰为伴生对象,伴生对象在类中只能存在一个，类似于java中的静态方法 Java 中使用类访问静态成员，静态方法。





























