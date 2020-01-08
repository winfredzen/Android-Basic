# Lambda中的return

参考：

+ [Return from Kotlin’s lambda](https://medium.com/@belokon.roman/return-from-kotlins-lambda-411171923756)
+ [返回和跳转](https://www.kotlincn.net/docs/reference/returns.html)

初见如下的代码，有点不理解为什么return后面要加上一个`@`

![010](https://github.com/winfredzen/Android-Basic/raw/master/Kotlin/images/010.png)

我删除`@OnNavigationItemSelectedListener`，会提示如下的错误

![011](https://github.com/winfredzen/Android-Basic/raw/master/Kotlin/images/010.png)

参考上面的教程，在lambda中直接返回，会提示如下的错误

![012](https://github.com/winfredzen/Android-Basic/raw/master/Kotlin/images/012.png)

![013](https://github.com/winfredzen/Android-Basic/raw/master/Kotlin/images/013.png)

该怎么处理呢？

1.不使用`return`，将返回值作为lambda中的最后一个表达式

![014](https://github.com/winfredzen/Android-Basic/raw/master/Kotlin/images/014.png)

2.使用`qualified return syntax`

```kotlin
fun main() {
    testWithAction {
        println("Some action")
        return@testWithAction 42
    }
}
```

这里使用function的名称作为label，也可以自己指定label

```kotlin
testWithAction marker@{
    ...
    return@marker 42
}
```



## inline

如果将函数声明为`inline`，如下，就可以调用`return`了：

```kotlin
inline fun testInline(action:() -> Int) {
    println("start testInline")
    val result = action()
    println("end testInline with $result")
}

fun someFunction() {
    println("start someFunction")
    testInline {
        println("do some action")
        return
    }
    println("end someFunction")
}

fun main() {
    someFunction()
}
```

此时控制台输出的结果为：

```tex
start someFunction
start testInline
do some action
```

`return`之后的`println`都没有输出，表示的`someFunction`函数返回了

> `inline` 修饰符影响函数本身和传给它的 lambda 表达式：所有这些都将内联到调用处。

> The rule for the inlined lambdas is next: “return” statement will return from the nearest enclosing function(in other words — will return from the nearest ***fun\***). This can lead to the unpredictable behavior and to the issues.
>
> inlined lambdas 规则是：return从最靠近的`fun`中返回。这样可能会导致很多问题

为避免发生这种问题，可使用**crossinline**，测试就不能在lambda中使用return了

![015](https://github.com/winfredzen/Android-Basic/raw/master/Kotlin/images/015.png)

此时的解决办法就跟上面一样了





