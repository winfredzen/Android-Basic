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
val numbers = mutableListOf("one", "two", "three")
with(numbers) {
    println("'with' is called with argument $this")
    println("It contains $size elements")
}
```

> 'with' is called with argument [one, two, three]
>
>  It contains 3 elements

`with` 的另一个用例是引入一个辅助对象，其属性或函数将用于计算值

```kotlin
val numbers = mutableListOf("one", "two", "three")
val firstAndLast = with(numbers) {
    "The first element is ${first()}," +
    " the last element is ${last()}"
}
println(firstAndLast)
```

> The first element is one, the last element is three



## run



























