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

## Companion Object

我自己的理解类似一个static方法，但其实不是static，只是类似

> 请注意，即使伴生对象的成员看起来像其他语言的静态成员，在运行时他们仍然是真实对象的实例成员，而且，例如还可以实现接口

```kotlin
class MyClass {
    companion object Factory {
        fun create(): MyClass = MyClass()
    }
}

val instance = MyClass.create()
```

参考：

+ [面向对象高级：伴生对象](https://zhuanlan.zhihu.com/p/26713535)



如下的类：

```kotlin
class Person(val name: String) {
  companion object {
    val anonymousPerson = Person("Anonymous")
    fun sayHello() {
      println("Hello")
    }
  }
  
  var age = 0
  fun sayName() {
    println("My name is $name")
  }
}
```

反编译后，类的形式如下：

```java
public final class Person {
  private final int age;
  private final String name;
  private static final Person anonymousPerson = new Person("Anonymous");
  public static final Person.Companion Companion = new Person.Companion();
  
  public Person(String name) {
    this.name = name;
  }
  // getAge()、setAge()、getName()
  
  public final void sayName() {
    System.out.println("My name is " + this.name);
  }
  
  public static final class Companion {
    private Companion() {}
    
    public final Person getAnonymousPerson() {
      return Person.anonymousPerson;
    }
    
    public final void sayHello() {
      System.out.println("Hello")
    }
  }
}
```

**使用伴生对象实际上是在这个类内部创建了一个名为 Companion 的静态单例内部类**


























