# Companion Object

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

























