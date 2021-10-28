# lambda

参考：

+ [高阶函数与 lambda 表达式](https://www.kotlincn.net/docs/reference/lambdas.html)

有时候一个函数以另一个函数作为参数

## Higher-Order VS lambda

Higher-Order function是一个

> 高阶函数是将函数用作参数或返回值的函数



## lambda

`lambda`的定义

```xml
(<arg1>, <arg2>) -> <return>
```

如下的简单例子：

```kotlin
fun main(args: Array<String>) {

    fun handleInteger(myInt: Int, operation:(Int) -> Unit) {
        operation(myInt)
    }

    handleInteger(5, { println("My Result is ${it * 10}")})
    handleInteger(5, { myType -> println("My Result is ${myType * 10}")})
    handleInteger(5, { _ -> println("My Result is 10")})
    
}
```

一些说明：

1.`it`表示的是单个参数的隐式名称。一个 lambda 表达式只有一个参数是很常见的。

如果编译器自己可以识别出签名，也可以不用声明唯一的参数并忽略 `->`。 该参数会隐式声明为 `it`：



如下的内容来自[Kotlin 的 Lambda 表达式，大多数人学得连皮毛都不算](https://rengwuxian.com/kotlin-lambda/)

> 如果 Lambda 是函数的最后一个参数，你可以把 Lambda 写在括号的外面：
>
> ```kotlin
> view.setOnClickListener() { v: View -&gt;
>   switchToNextPage()
> }
> ```
>
> 而如果 Lambda 是函数唯一的参数，你还可以直接把括号去了：
>
> ```kotlin
> view.setOnClickListener { v: View -&gt;
>   switchToNextPage()
> }
> ```
>
> 另外，如果这个 Lambda 是单参数的，它的这个参数也省略掉不写：
>
> ```kotlin
> view.setOnClickListener {
>   switchToNextPage()
> }
> ```





























