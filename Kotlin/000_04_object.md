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
+ [Kotlin 中的伴生对象和静态成员 - 掘金](https://juejin.cn/post/6844903617934147597)

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

或者如如下的例子：

```kotlin
Thread(object : Runnable {
 override fun run() {
 println("Thread is running")
 }
}).start()
```

> 由于Kotlin完全舍弃了`new`关键字，因此创建匿名类实例的时候就不能再使用`new`了，而是改用了`object`关键字

由于`Thread`类的构造方法是符合Java函数式API的使用条件的，所以可以精简

```kotlin
Thread(Runnable {
 println("Thread is running")
}).start()
```

> 因为`Runnable`类中只有一个待实现方法，即使这里没有显式地重写`run()`方法，Kotlin也能自动明白`Runnable`后面的Lambda表达式就是要在`run()`方法中实现的内容

如果一个Java方法的参数列表中有且仅有一个Java单抽象方法接口参数，我们还可以将 接口名进行省略

```kotlin
Thread({
 println("Thread is running")
}).start()
```

当Lambda表达式是方法的最后一个参数时，可以将Lambda表达式移到方法括号的外面

```kotlin
Thread {
 println("Thread is running")
}.start()
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

## Singleton

可以用作单例的实现

```kotlin
object MySingletion {
    fun doMyStuff(data: String) {
        println("This is my data $data")
    }
    val myConstant = "This is my constant"
}

fun main(args: Array<String>) {

    MySingletion.doMyStuff("Hello")

}
```

这些对象可以有超类型

```kotlin
object DefaultListener : MouseAdapter() {
    override fun mouseClicked(e: MouseEvent) { …… }

    override fun mouseEntered(e: MouseEvent) { …… }
}
```

不能嵌套在其他object中或者`non-inner classes`


























