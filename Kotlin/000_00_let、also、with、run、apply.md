# let、also、with、run、apply

在kotlin中经常看到let、also、with、run、apply的使用，它们的区别及使用场景是什么呢？

参考[Scope functions](https://kotlinlang.org/docs/scope-functions.html)中的说法：

> The Kotlin standard library contains several functions whose sole purpose is to execute a block of code within the context of an object. When you call such a function on an object with a [lambda expression](https://kotlinlang.org/docs/lambdas.html) provided, it forms a temporary scope. In this scope, you can access the object without its name. Such functions are called *scope functions*. There are five of them: `let`, `run`, `with`, `apply`, and `also`.
>
> Kotlin 标准库包含几个函数，其唯一目的是在对象的上下文中执行代码块。 当您在提供了 lambda 表达式的对象上调用此类函数时，它会形成一个临时范围。 在此范围内，您可以访问不带名称的对象。 这样的函数称为作用域函数。 其中有五个：let、run、with、apply 和 also。



作用域函数没有引入任何新的技术能力，但它们可以让你的代码更加简洁易读



**区别**

主要的区别在如下的两个方面：

1.应用上下文对象的方式（The way to refer to the context object）- `this` 还是 `it`

2.返回值（The return value）- 返回的是上下文对象（context object）还是lambda表达式的结果



## let

> **The context object** is available as an argument (`it`). **The return value** is the lambda result.
>
> 上下文对象可用作参数 (it)。 返回值是 lambda 结果。

1.`let` can be used to invoke one or more functions on results of call chains

`let` 可用于在调用链的结果上调用一个或多个函数

如：

```kotlin
val numbers = mutableListOf("one", "two", "three", "four", "five")
val resultList = numbers.map { it.length }.filter { it > 3 }
println(resultList)   
```

可以替换为：

```kotlin
val numbers = mutableListOf("one", "two", "three", "four", "five")
numbers.map { it.length }.filter { it > 3 }.let { 
    println(it)
    // and more function calls if needed
} 
```

如果代码块只包含一个以`it`作为参数的函数，则可以使用方法引用 (`::`) 而不是 lambda：

```kotlin
val numbers = mutableListOf("one", "two", "three", "four", "five")
numbers.map { it.length }.filter { it > 3 }.let(::println)
```

2.`let` 通常用于执行仅具有非空值的代码块

```kotlin
val str: String? = "Hello"   
//processNonNullString(str)       // compilation error: str can be null
val length = str?.let { 
    println("let() called on $it")        
    processNonNullString(it)      // OK: 'it' is not null inside '?.let { }'
    it.length
}
```

3.使用 `let` 的另一种情况是引入具有有限范围的局部变量以提高代码可读性。

```kotlin
val numbers = listOf("one", "two", "three", "four")
val modifiedFirstItem = numbers.first().let { firstItem ->
    println("The first item of the list is '$firstItem'")
    if (firstItem.length >= 5) firstItem else "!" + firstItem + "!"
}.uppercase()
println("First item after modifications: '$modifiedFirstItem'")
```



## with

> A non-extension function: the context object is passed as an argument, but inside the lambda, it's available as a receiver (this). The return value is the lambda result.
>
> 非扩展方法，上下文对象是`this`，返回值是 lambda 结果

`with` 可以理解为“使用此对象，执行以下操作”

```kotlin
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
    val builder = StringBuilder()
    builder.append("Start eating fruits.\n")
    for (fruit in list) {
        builder.append(fruit).append("\n")
    }
    builder.append("Ate all fruits.")
    val result = builder.toString()
    println(result)
```

使用`with`后

```kotlin
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
    val result = with(StringBuilder()) {
        append("Start eating fruits.\n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("Ate all fruits.")
        toString()
    }
    println(result)
```

Lambda表达式的最后一行代码会作为`with`函数的返回值返回



## run

> **The context object** is available as a receiver (`this`). **The return value** is the lambda result.
>
> 上下文对象是`this`，返回值是 lambda 结果

`run` does the same as `with` but invokes as `let` - as an extension function of the context object.

> run 的作用与 with 相同，但调用为 let - 作为上下文对象的扩展函数。

当你的 lambda 同时包含对象初始化和返回值的计算时，`run` 很有用。

```kotlin
val service = MultiportService("https://example.kotlinlang.org", 80)

val result = service.run {
    port = 8080
    query(prepareRequest() + " to port $port")
}

// the same code written with let() function:
val letResult = service.let {
    it.port = 8080
    it.query(it.prepareRequest() + " to port ${it.port}")
}
```

除了在接收器对象上调用 `run` 之外，您还可以将其用作非扩展函数。 非扩展运行允许您在需要表达式的情况下执行由多个语句组成的块

```kotlin
val hexNumberRegex = run {
    val digits = "0-9"
    val hexDigits = "A-Fa-f"
    val sign = "+-"

    Regex("[$sign]?[$digits$hexDigits]+")
}

for (match in hexNumberRegex.findAll("+123 -FFFF !%*& 88 XYZ")) {
    println(match.value)
}
```



## apply﻿

> **The context object** is available as a receiver (`this`). **The return value** is the object itself.
>
> 上下文对象是`this`，返回值是自身

`apply`函数无法指定返回值，而是会自动返回调用对象本身

`apply` 的常见情况是对象配置

```kotlin
val adam = Person("Adam").apply {
    age = 32
    city = "London"        
}
println(adam)
```



## also﻿

> **The context object** is available as an argument (`it`). **The return value** is the object itself.

> When you see `also` in the code, you can read it as “ *and also do the following with the object.*”
>
> also可以理解为- object也做如下的操作

```kotlin
val numbers = mutableListOf("one", "two", "three")
numbers
    .also { println("The list elements before adding new one: $it") }
    .add("four")
```

























