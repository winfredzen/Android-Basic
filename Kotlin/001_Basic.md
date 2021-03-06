# Basic

大致浏览一下，某些方面与Swift非常相似

一些Core Concepts

+ Properties
+ Types(Numbers, Strings)
+ Functions
+ String to Number, Number to String
+ Flow Control
+ Null

**常量与变量**

使用`val`表示常量，只能为其赋值一次，使用`var`表示变量

## 区间与数列

调用 `kotlin.ranges` 包中的 [`rangeTo()`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/range-to.html) 函数及其操作符形式的 `..` 轻松地创建两个值的区间

通常，`rangeTo()` 会辅以 `in` 或 `!in` 函数

```kotlin
if (i in 1..4) {  // 等同于 1 <= i && i <= 4
    print(i)
}
```

整数类型区间（[`IntRange`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html)、[`LongRange`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-long-range/index.html)、[`CharRange`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-char-range/index.html)）还可以对其进行迭代

```kotlin
for (i in 1..4) print(i) //1234
```

 [`downTo`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/down-to.html) 反向数字迭代

```kotlin
for (i in 4 downTo 1) print(i) //4321
```

还可以通过 [`step`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/step.html) 函数指定步长

```kotlin
for (i in 1..8 step 2) print(i) //1357
```

 [`until`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/until.html) 函数，半闭区间

```kotlin
    //123456789
    for (i in 1 until 10) {       // i in [1, 10), 10被排除
        print(i)
    }
```



## null

可参考：

+ [空安全](https://www.kotlincn.net/docs/reference/null-safety.html)

总体感觉跟Swift的*可选类型*非常的相似

```kotlin
    var a: String = "abc"
    a = null // 编译错误

    var b: String? = "abc"
    b = null // ok
    print(b)
```

安全调用使用`?.`安全调用操作符

```kotlin
    var b: String? = "abc"
    b = null // ok
    println(b?.length)
    println(a?.length) // 无需安全调用
```

如果要只对非空值执行某个操作，安全调用操作符可以与 [`let`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/let.html) 一起使用：

```kotlin
val listWithNulls: List<String?> = listOf("Kotlin", null)
for (item in listWithNulls) {
    item?.let { println(it) } // 输出 Kotlin 并忽略 null
}
```

```kotlin
    var authorName: String? = "zen"
    //authorName = null
    authorName?.let { name ->
        println(name)
    }
    authorName?.let {//可以省略参数名称，默认参数名为it
        println("$it")
    }
```



如下的例子：

```kotlin
    var result: Int? = 40
    println(result)
    //Error:(61, 20) Kotlin: Operator call corresponds to a dot-qualified call 'result.plus(1)' which is not allowed on a nullable receiver 'result'.
    println(result + 1)
```

提示：`'result.plus(1)' which is not allowed on a nullable receiver 'result'`

我们可以这样调用

```kotlin
var newResult = result?.plus(1)
```

或者使用`!!` 操作符

#### `!!` 操作符

非空断言运算符（`!!`）将任何值转换为非空类型，若该值为空则抛出异常

```kotlin
    println(result!! + 1)
```

#### Elvis 操作符

```kotlin
val l: Int = if (b != null) b.length else -1
```

等同于

```kotlin
val l = b?.length ?: -1
```

也可以这样使用

```kotlin
    var myFarSong: String? = "500 Miles"
    myFarSong = null
    myFarSong?.let { println("$it") } ?: println("NO song")
```



## 基本类型

### 数字

对整型有如下的几种类型：

+ Byte
+ Short
+ Int
+ Long

所有以未超出 `Int` 最大值的整型值初始化的变量都会推断为 `Int` 类型。如果初始值超过了其最大值，那么推断为 `Long` 类型。 如需显式指定 `Long` 型值，请在该值后追加 `L` 后缀

```kotlin
val oneLong = 1L // Long
```

浮点数

+ Float
+ Double

对于小数，编译器默认推断为`Double`类型，如果想显示的指定为`Float`类型，添加`f` 或 `F`后缀

注意，Kotlin 中的数字没有**隐式拓宽转换**

```kotlin
fun main() {
    fun printDouble(d: Double) { print(d) }

    val i = 1    
    val d = 1.1
    val f = 1.1f 

    printDouble(d)
//    printDouble(i) // 错误：类型不匹配
//    printDouble(f) // 错误：类型不匹配
}
```

### 显示转换

较小的类型**不能**隐式转换为较大的类型

```kotlin
val b: Byte = 1 // OK, 字面值是静态检测的
val i: Int = b // 错误

val i: Int = b.toInt() // OK：显式拓宽
```

每个数字类型支持如下的转换:

- `toByte(): Byte`
- `toShort(): Short`
- `toInt(): Int`
- `toLong(): Long`
- `toFloat(): Float`
- `toDouble(): Double`
- `toChar(): Char`



### 字符串

字符串用 `String` 类型表示

字符串可遍历，也可使用索引来访问`s[i]`

```kotlin
    val str = "abcd"
    for (c in str) {
        println(c)
    }
```

可以使用 `+` 操作符来连接字符串，也适用于连接字符串与其他类型的值

#### 字符串字面值

2种类型的字符串字面值

1.转义字符串

可以包含转义字符

```kotlin
val s = "Hello, world!\n"
```

2.原始字符串

原始字符串可以包含换行以及任意文本(与Swift很类似啊)

```kotlin
val text = """
    for (c in "foo")
        print(c)
"""
```

#### 字符串模板

模板表达式以美元符（`$`）开头

```kotlin
    val i = 10
    println("i = $i") // 输出“i = 10”
```

或者用花括号括起来

```kotlin
val s = "abc"
println("$s.length is ${s.length}") // 输出“abc.length is 3”
```

如果需要在原始字符串中表示字面值 `$` 字符（它不支持反斜杠转义），可以用下列语法：

```kotlin
val price = """
${'$'}9.99
"""
```



#### 字符串转换

转为int

```kotlin
    var intValue = "42".toInt()
    println("intValue = $intValue")
```

int转为string

```kotlin
    val intString = 42.toString()
    println("intString = $intString")
```



### Pair

[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)表示的是：Represents a generic pair of two values. 表示2个值

```kotlin
data class Pair<out A, out B> : Serializable
```

```kotlin
    val coordinates = Pair(1, 3)
    val (x, y) = coordinates
    println("x = $x, y = $y"); //x = 1, y = 3
    println("x = ${coordinates.first}, y = ${coordinates.second}"); //x = 1, y = 3
```



### Triple

[Triple](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-triple/index.html)表示的是：Represents a triad of values，表示三个值

```kotlin
data class Triple<out A, out B, out C> : Serializable
```

```kotlin
    val coordinates3D = Triple(3, 2, 1)
    val (x1, y1, z1) = coordinates3D
    println("x1 = ${x1}, y1 = ${y1}, z1 = ${z1}") //x1 = 3, y1 = 2, z1 = 1

    val (x2, y2, _) = coordinates3D
    println("x2 = ${x2}, y2 = ${y2}")
```



## 流程控制

### Loops

*while* 与 *do*..*while*与别的语言基本一致



### when

`when`取代了`switch`操作，`else`分支是必须的，除非编译器能够检测出所有的可能情况都已经覆盖了

可以检测一个值在（`in`）或者不在（`!in`）一个[区间](https://www.kotlincn.net/docs/reference/ranges.html)或者集合中

```kotlin
    val fahrenheit = 42
    when (fahrenheit) {
        in 0..30 -> println("really cold")
        in 31..40 -> println("getting cold")
        in 41..50 -> println("kind of cold")
        in 51..60 -> println("nippy")
        else -> {
            println("none")
        }
    }
```

检测一个值是（`is`）或者不是（`!is`）一个特定类型的值

```kotlin
fun hasPrefix(x: Any) = when(x) {
    is String -> x.startsWith("prefix")
    else -> false
}
```























