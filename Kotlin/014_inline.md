# inline

参考：

+ [内联函数](https://www.kotlincn.net/docs/reference/inline-functions.html)

什么是内联函数，参考维基百科[内联函数](https://zh.wikipedia.org/wiki/内联函数)

> 也就是说建议编译器将指定的函数体插入并取代每一处调用该函数的地方（[上下文](https://zh.wikipedia.org/wiki/上下文)），从而节省了每次调用函数带来的额外时间开支。



## inline函数

参考：

+ [Kotlin 内联函数 inline](https://www.jianshu.com/p/8a0d5bae9cdf)

> 当一个函数被内联 `inline` 标注后，在调用它的地方，会把这个函数方法体中的所以代码移动到调用的地方，而不是通过方法间压栈进栈的方式。
>
> **换句话说：在编译时期，把调用这个函数的地方用这个函数的方法体进行替换。**



**什么时候使用inline？**

> 也就是说 `inline` 在一般的方法是标注，是不会起到很大作用的，`inline` 能带来的性能提升，往往是在参数是 `lambda` 的函数上。



**`inline` 提高效率的原因**

> 在 `kotlin` 中，因为出现了大量的 `高阶函数` -- 「高阶函数是将函数用作参数或返回值的函数」，使得越来越多的地方出现 `函数参数` 不断传递的现象，每一个函数参数都会被编译成一个对象， 使得内存分配（对于函数对象和类）和虚拟调用会增加运行时间开销。所以才会出现 `inline` 内联函数。可以通过 `inline` 的标注，把原本需要生成一个类的开销节省了， 同时也少了一层方法栈的调用。



**支持 `return` 退出函数**

```kotlin
fun foo(body:()->Unit) {
    ordinaryFunction {
        println("zc_testlabama 表达式退出")
        return
    }
    println("zc_test --->foo() end")
} 
fun ordinaryFunction(block: () -> Unit) {
    println("hahha")
    block.invoke()
    println("hahha233333")
}
```

> 如果在 `ordinaryFunction` 这个方法没有 `inline` 的标注，编译器会在 `return` 的位置出错，`return is not allowed here`.

解决上述错误的方式，可以为 `return` 添加标签，例如 `return@ordinaryFunction`, 但是这样的话，方法执行只会退出 `lambda` 表达式，后面的代码 `println("zc_test --->foo() end")` 还是会走到的。

当我们添加上 `inline` 时，正确的代码如下：

```kotlin
fun foo(body:()->Unit) {
    ordinaryFunction {
        // 因为标识为 inline 的函数会被插入到调用出，此时 return 肯定是 return 到该整个方法
        println("zc_testlabama 表达式退出")
        return
    }
    println("zc_test --->foo() end")
}
// 如果不使用 inline， 上面代码会被报错。因为「不允许这么做」
inline fun ordinaryFunction(block: () -> Unit) {
    println("hahha")
    block.invoke()
    println("hahha233333")
}
```

当我们添加了 `inline` 标志后，在 `ordinaryFunction{}` 的 `return` 时就会退出整个 `foo()` 函数，因此结尾的 `println("zc_test --->foo() end")` 是不会被调用的。



## **`noinline`**

什么时候我们会需要 `noinline` 呢？

```kotlin
inline fun foo(testName:String, body:()->Unit) {
    // 这里会报错。。。
    ordinaryFunction(body)
    println("zc_test --->foo() end")
} 
fun ordinaryFunction(block: () -> Unit) {
    println("hahha")
    block.invoke()
    println("hahha233333")
}
```

如果 `ordinaryFunction()` 不使用 `inline` 标注，是一般的函数，这里是不允许把内联函数 `foo()` 的函数参数 `body` 传递给 `ordinaryFunction()`。

即：**内联函数的「函数参数」 不允许作为参数传递给非内联的函数**，
 如果我们想要实现上述的调用，便可以使用 `noinline` 标注内联函数 `foo()` 的 `body` 参数

```kotlin
inline fun foo(testName:String, noinline body:()->Unit) {
    ...
}
```







