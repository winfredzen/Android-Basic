# 第一行代码Kotlin记录一

Kotlin完全抛弃了Java中的基本数据类型，全部使用了对象数据类型

永远优先使用`val`来声明一个变量，而当`val`没有办法满足需求时再使用`var`



## 函数的简化

一个标准的函数形式如下：

```kotlin
fun largerNumber(num1: Int, num2: Int): Int {
    return max(num1, num2)
}
```

1.当一个函数中只有一行代码时，不必编写函数体，在函数的尾部，使用`=`连接

2.`return`关键字也可以省略

3.kotlin可以推导出返回值类型，所以不用显式声明返回值类型

```kotlin
fun largerNumber(num1: Int, num2: Int) = max(num1, num2)
```





## 流程控制



### if

如下的一个Java形式的if语句：

```kotlin
fun largerNumber(num1: Int, num2: Int): Int {
    var value = 0
    if (num1 > num2) {
        value = num1
    } else {
        value = num2
    }
    return value
}
```

Kotlin中的if语句相比于Java有一个额外的功能，它是**可以有返回值**的，返回值就是if语句每一个条件中最后一行代码的返回值。

```kotlin
fun largerNumber(num1: Int, num2: Int): Int {
    val value = if (num1 > num2) {
        num1
    } else {
        num2
    }
    return value
}
```

> **注意**：这里`value`的类型为`val`

value是一个多余的变量，可以继续简化

```kotlin
fun largerNumber(num1: Int, num2: Int): Int {
    return if (num1 > num2) {
        num1
    } else {
        num2
    }
}
```

再参考上面的**函数的简化**，*当一个函数只有一行代码时，可以省略函数体部分，直接将这一行代码使用等号串连在函数定义的尾部*

```kotlin
fun largerNumber(num1: Int, num2: Int) = if (num1 > num2) num1 else num2
```



### when

Kotlin中的`when`语句有点类似于Java中的`switch`语句，但它又远比`switch`语句强大得多

如下的方法，根据名字查询成绩：

```kotlin
fun getScore(name: String) = if (name == "Tom") {
    86
} else if (name == "Jim") {
    77
} else if (name == "Jack") {
    95
} else if (name == "Lily") {
    100
} else {
    0
}
```

当判断条件非常多时，考虑使用`when`

```kotlin
fun getScore(name: String) = when (name) {
    "Tom" -> 86
    "Jim" -> 77
    "Jack" -> 95
    "Lily" -> 100
    else -> 0
}
```

> when的格式：
>
> ```kotlin
> 匹配值 -> { 执行逻辑 }
> ```
>
> 执行逻辑只有一行代码时，`{ }`可以省略

**类型匹配**

```kotlin
fun checkNumber(num: Number) {
    when (num) {
        is Int -> println("number is Int")
        is Double -> println("number is Double")
        else -> println("number not support")
    }
}
```

> `is`关键字就是类型匹配的核心，它相当于Java中的`instanceof`关键字

**不带参数的用法**

```kotlin
fun getScore(name: String) = when {
    name == "Tom" -> 86
    name == "Jim" -> 77
    name == "Jack" -> 95
    name == "Lily" -> 100
    else -> 0
}
```

> 用法是将判断的表达式完整地写在`when`的结构体当中。
>
> **注意**，Kotlin中判断字符串或对象是否相等可以直接使用`==`关键字，而不用像Java那样调用`equals()`方法。

```kotlin
fun getScore(name: String) = when {
 name.startsWith("Tom") -> 86
 name == "Jim" -> 77
 name == "Jack" -> 95
 name == "Lily" -> 100
 else -> 0
}
```



### 循环

**区间**

1.两端闭区间

```kotlin
val range = 0..10
```

2.左闭右开区间

```kotlin
val range = 0 until 10
```

循环时，跳过某些元素，使用`step`关键字

```kotlin
for (i in 0 until 10 step 2) {
    println(i)
}
```

> 类似于`i = i + 2`

3.降序区间，使用`downTo`关键字

```kotlin
for (i in 10 downTo 1) {
    println(i)
}
```

> 创建了一个`[10, 1]`的降序区间



## 类与对象































