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

2中类型的字符串字面值

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



























