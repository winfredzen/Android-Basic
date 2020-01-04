# Class

使用*class*关键字来声明类

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



































