# Flow

如下的方法，是一次性返回多个值，而且是同步的

```kotlin
fun simpleList(): List<Int> = arrayListOf(1, 2, 3)
```

而如下的`Sequence`，序列返回多个值，每隔1s产生一个值，而且是阻塞的

```kotlin
    fun simpleSequence(): Sequence<Int> = sequence {
        for (i in 1..3) {
            Thread.sleep(1000)
            yield(i)
        }
    }
```

调用该方法

```kotlin
simpleSequence().forEach { value -> println(value) }
```

输出结果

```kotlin
2021-12-26 11:34:05.338 4174-4174/com.example.coroutine I/System.out: 1
2021-12-26 11:34:06.339 4174-4174/com.example.coroutine I/System.out: 2
2021-12-26 11:34:07.341 4174-4174/com.example.coroutine I/System.out: 3
```

如果给`simpleList()`方法添加`suspend`，如下变化挂起函数，这样做虽然返回了多个值，而且是异步的，但是它还是一次性返回了多个值

```kotlin
    suspend fun simpleList(): List<Int> {
        delay(1000)
        return arrayListOf(1, 2, 3)
    }

runBlocking<Unit> {
    simpleList().forEach { value -> println(value) }
}
```

**通过Flow异步返回多个值**

如下的例子，返回多个值，而且是异步的：

```kotlin
fun simpleFlow() = flow<Int> {
    for (i in 1..3) {
        delay(1000)
        emit(i) //发射并产生一个元素
    }
}


runBlocking<Unit> {
    launch {
        for (k in 1..3) {
            println("I'm not blocking $k")
            delay(1500)
        }
    }
    simpleFlow().collect {
        value -> println(value)
    }
}
```

> 1.通过`collect`收集发射的元素

此时，打印结果输出如下：

```kotlin
2021-12-26 11:54:33.726 4935-4935/com.example.coroutine I/System.out: I'm not blocking 1
2021-12-26 11:54:34.737 4935-4935/com.example.coroutine I/System.out: 1
2021-12-26 11:54:35.228 4935-4935/com.example.coroutine I/System.out: I'm not blocking 2
2021-12-26 11:54:35.738 4935-4935/com.example.coroutine I/System.out: 2
2021-12-26 11:54:36.731 4935-4935/com.example.coroutine I/System.out: I'm not blocking 3
2021-12-26 11:54:36.740 4935-4935/com.example.coroutine I/System.out: 3
```

说明：

1.通过flow构建器，构建

```kotlin
public fun <T> flow(@BuilderInference block: suspend FlowCollector<T>.() -> Unit): Flow<T> = SafeFlow(block)
```

2.`flow{...}`构建快中的代码可以挂起

3.函数`simpleFlow()`不再标有`suspend`修饰符

4.流使用`emit`函数发射值

5.流使用`collect`函数收集值

![](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/022.png)

