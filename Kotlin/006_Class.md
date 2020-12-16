# Class

使用*class*关键字来声明类，参考：

+ [类与继承](https://www.kotlincn.net/docs/reference/classes.html)

## 构造函数

类的属性可以放到构造函数中(声明属性以及从主构造函数初始化属性)，如下的类：

```kotlin
class Person(val firstName: String, val lastName: String) {

}

fun main(args: Array<String>) {

    val person = Person("wang", "zen")
    println(person.firstName + " " + person.lastName)

}
```

上面的这个构造函数是**主构造函数**

构造函数也可以有默认参数

```kotlin
class Person(var firstName: String, var lastName: String = "zen") {
    
}
```

### 次构造函数

次构造函数要直接或者间接的委托给主构造函数，委托到另一个构造函数使用`this`关键字

```kotlin
class Person(var firstName: String) {

    var lastName: String? = null
    constructor(firstName: String, lastName: String) : this(firstName) {
        this.lastName = lastName
    }

}
```



如果构造函数有注解或可见性修饰符，这个 *constructor* 关键字是必需的，并且这些修饰符在它前面：

```kotlin
class Customer public @Inject constructor(name: String) { /*……*/ }
```



## init

主构造函数不能包含任何的代码。初始化的代码可以放到以 `init` 关键字作为前缀的**初始化块（initializer blocks）**中

可以包含多个`init`，初始化块按照它们出现在类体中的顺序执行

```kotlin
fun main(args: Array<String>) {

    class Person {
        init {
            println("Init 1")
        }
        init {
            println("Init 2")
        }
    }

    val person = Person()

}
```

```tex
Init 1
Init 2
```

```kotlin
fun main(args: Array<String>) {

    class Person(var firstName: String, var lastName: String) {
        var fullName: String
        init {
            fullName = firstName + " " + lastName
        }
    }

    val person = Person("wang", "zen")

}
```



## 继承

在 Kotlin 中所有类都有一个共同的超类 `Any`，这对于没有超类型声明的类是默认超类：

```kotlin
class Example // 从 Any 隐式继承
```

显式声明一个超类型，要报超类型放在冒号之后，如下的例子`Student`继承`Person`

![005](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/005.png)

注意，上面的写法提示出错

![006](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/006.png)

移除`firstName`、`lastName`前面的`var`

![007](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/007.png)

还是有错误提示

![008](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/008.png)

将`Person`声明为`open`

![009](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/009.png)



如果派生类没有主构造函数，那么每个次构造函数必须使用 `super` 关键字初始化其基类型，或委托给另一个构造函数做到这一点

```kotlin
class MyView : View {
    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
}
```



## open

参考：

+ [Kotlin中的open关键字](https://juejin.im/post/5d08ddf75188254e8728a6a8)

kotlin中它所有的类默认都是`final`的，那么就意味着不能被继承，而且在类中所有的方法也是默认是`final`的

+ 为类增加open，class就可以被继承了
+ 为方法增加open，那么方法就可以被重写了



## 数据类

参考：

+ [数据类](https://www.kotlincn.net/docs/reference/data-classes.html)

> 我们经常创建一些只保存数据的类。 在这些类中，一些标准函数往往是从数据机械推导而来的。在 Kotlin 中，这叫做 *数据类* 并标记为 `data`

在Java中，会创建Model类，仅仅用来存储数据，有许多变量和getter setter方法，在Java中，也需要创建`equal`方法，或者`hashCode`方法

数据类，会自动的创建如下的成员：

+ `equals()`/`hashCode()`
+ `toString()`
+ Getter Setter

数据类必须满足以下要求

+ 主构造函数需要至少有一个参数
+ 主构造函数的所有参数需要标记为 `val` 或 `var`
+ 数据类不能是抽象、开放、密封或者内部的

```kotlin
    data class Podcast(val title: String, val description: String, val url: String)

    val podcast = Podcast("title", "description", "url")
    val podcast2 = podcast.copy(title = "copy title")

    val (title, description, url) = podcast2
    println(title + " " + description + " " + url) //copy title description url
```



## 密封类

参考：

+ [密封类](https://www.kotlincn.net/docs/reference/sealed-classes.html)
+ [面向对象高级：密封类](https://zhuanlan.zhihu.com/p/26761603)

要声明一个密封类，需要在类名前面添加 `sealed` 修饰符。虽然密封类也可以有子类，但是所有子类都必须在与密封类自身相同的文件中声明。

特点：

+ 密封类是为 **继承** 设计的，是一个抽象类
+ 密封类的子类是确定的，除了已经定义好的子类外，它不能再有其他子类

```kotlin
sealed class Person(val name: String, var age: Int) {
    class Adult(name: String, age: Int) : Person(name, age)
    class Child(name: String, age: Int) : Person(name, age)
}
```

需注意：

1.**因为密封类是一个抽象类，所以不能用 data 等非抽象类的修饰符来修饰它，也不用加 open 关键字。**

2.**密封类的子类，要么写在密封类内部，要么写在父类同一个文件里，不能出现在其他地方。**但子类的子类可以出现在其他地方。

接着反编译大法登场：

```java
// 为方便阅读，对反编译代码进行了优化
public abstract class Person {
  private final String name;
  private int age;
  // 省略 getter() 和 setter()
  
  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }
  
  public static final class Adult extends Person {
    public Adult(@NotNull String name, int age) {
      super(name, age);
    }
  }
  
  public static final class Child extends Person {
    public Child(@NotNull String name, int age) {
      super(name, age);
    }
  }
}
```

这里 Adult 类和 Child 类被编译为 Person 类的**静态内部类**，是因为它们定义在密封类的内部；如果放在同一个文件里定义，则不会被编译为内部类。

**密封类的使用**

密封类的使用与一般抽象类并无不同，也就是说不能使用密封类实例化对象，只能用它的子类实例化对象。

密封类功能更多在于限制继承，起到划分子类的作用。将抽象类定义为密封类，可以 **禁止外部继承**，对于一些只划分为固定类型的数据，可以保证安全。

除此以外，密封类唯一的不同之处在于 when 语句对它有一个优化：因为密封类的子类型是确定的，所以在用 when 语句遍历密封类的子类时，可以不加 else 语句。



使用密封类的关键好处在于使用 [`when` 表达式](https://www.kotlincn.net/docs/reference/control-flow.html#when-表达式) 的时候，如果能够验证语句覆盖了所有情况，就不需要为该语句再添加一个 `else` 子句了。当然，这只有当你用 `when` 作为表达式（使用结果）而不是作为语句时才有用。

```kotlin
sealed class Expression

data class Num(val number: Double) : Expression()
data class Sum(val e1: Expression, val e2: Expression) : Expression()
object NotAnNumber : Expression()

fun eval(expr: Expression) : Double = when(expr) {
    is Num -> expr.number
    is Sum -> eval(expr.e1) + eval(expr.e2)
    NotAnNumber -> Double.NaN
}

fun main(args: Array<String>) {

    val num1 = Num(5.5)
    val num2 = Num(10.0)
    println("The sum of 5.5 and 10.0 is ${eval(Sum(num1, num2))}") //The sum of 5.5 and 10.0 is 15.5

}
```



## Getters Setters

声明一个属性的完整语法是

```kotlin
var <propertyName>[: <PropertyType>] [= <property_initializer>]
    [<getter>]
    [<setter>]
```

其初始器（initializer）、getter 和 setter 都是可选的

属性类型如果可以从初始器中推断出来，可以省略

```kotlin
var allByDefault: Int? // 错误：需要显式初始化器，隐含默认 getter 和 setter
var initialized = 1 // 类型 Int、默认 getter 和 setter
```



自定义getter

```kotlin
val isEmpty: Boolean
    get() = this.size == 0
```



自定义的 setter，setter 参数的名称是 `value`

```kotlin
var stringRepresentation: String
    get() = this.toString()
    set(value) {
        setDataFromString(value) // 解析字符串并赋值给其他属性
    }
```



如果想修改可见性，可使用`private set` 

```kotlin
var setterVisibility: String = "abc"
    private set // 此 setter 是私有的并且有默认实现
```



## 可见性

参考：

+ [可见性修饰符](https://www.kotlincn.net/docs/reference/visibility-modifiers.html)

类、对象、接口、构造函数、方法、属性和它们的 setter 都可以有 *可见性修饰符*。 （getter 总是与属性有着相同的可见性。） 在 Kotlin 中有这四个可见性修饰符：`private`、 `protected`、 `internal` 和 `public`。 如果没有显式指定修饰符的话，默认可见性是 `public`。



## 嵌套类与内部类

参考：

+ [嵌套类与内部类](https://www.kotlincn.net/docs/reference/nested-classes.html)



## this

参考：

+ [This 表达式](https://www.kotlincn.net/docs/reference/this-expressions.html)

要访问来自外部作用域的*this*，我们使用`this@label`，其中 `@label` 是一个代指 *this* 来源的标签

如下的例子：

```kotlin
class Person {
    var firstName = ""
    var child = Child()

    inner class Child {
        var firstName = ""
        fun printParentage() {
            println("Child ${this@Child.firstName} with parent ${this@Person.firstName}")
        }
    }
}
fun String.lastChar() : Char = this.get(this.length-1)

fun main(args: Array<String>) {
    val person = Person()
    person.firstName = "Sam"
    person.child.firstName = "Suzy"
    person.child.printParentage() //Child Suzy with parent Sam

    println("Hello there ${"Sammy".lastChar()}") //Hello there y
}
```



## lateinit

参考：

+ [延迟初始化属性与变量](https://www.kotlincn.net/docs/reference/properties.html#延迟初始化属性与变量)

> 一般地，属性声明为非空类型必须在构造函数中初始化。 然而，这经常不方便。例如：属性可以通过依赖注入来初始化， 或者在单元测试的 setup 方法中初始化。 这种情况下，你不能在构造函数内提供一个非空初始器。 但你仍然想在类体中引用该属性时避免空检测。

为处理这种情况，你可以用 `lateinit` 修饰符标记该属性：

要检测一个 `lateinit var` 是否已经初始化过，请在[该属性的引用](https://www.kotlincn.net/docs/reference/reflection.html#属性引用)上使用 `.isInitialized`

```kotlin
class Person(var firstName: String, var lastName : String) {
    lateinit var fullName : String
    init {
        fullName = firstName + " " + lastName
    }
    fun printFullName() {
        if (!this::fullName.isInitialized) {
            fullName = firstName + " " + lastName
        }
        println(fullName)
    }

}
fun main(args: Array<String>) {
    Person("Sam", "Gamgee").printFullName()
}
```









