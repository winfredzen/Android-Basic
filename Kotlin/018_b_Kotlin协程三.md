# Android官方对协程的解释

记录下官方文档对Kotlin的解释

## Coroutines: first things first

内容来自：[Coroutines: first things first](https://medium.com/androiddevelopers/coroutines-first-things-first-e6187bf3bb21)

`CoroutineScope`会一直跟踪使用`launch`或者`async`创建的协程。正在运行的协程可通过`scope.cancel()` 来取消

创建`CoroutineScope`，需要一个`CoroutineContext`作为构造函数的参数

`CoroutineContext`是一个元素集合，定义了协程的行为，由如下的部分组成：

+ `Job` - 控制协程的生命周期
+ `CoroutineDispatcher` - 分发work到适当的线程
+ `CoroutineName` - 协程的名称，debug时很有用
+ `CoroutineExceptionHandler` - 处理未捕获的异常

新创建的协程的`CoroutineContext`是什么呢？创建一个新的`Job`实例，可以允许我们控制它的生命周期，其余的element都是从其父类的`CoroutineContext`继承的

`CoroutineScope`可以创建协程，在协程的内部可以创建更多的协程，创建了一个隐式的task层级结构

```kotlin
val scope = CoroutineScope(Job() + Dispatchers.Main)
val job = scope.launch {
    // New coroutine that has CoroutineScope as a parent
    val result = async {
        // New coroutine that has the coroutine started by 
        // launch as a parent
    }.await()
}
```

层级的根通常是`CoroutineScope`，这种层次结构可视化如下：

![023](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/023.png)

**父类CoroutineContext的解释**

> However, the resulting parent `CoroutineContext` of a coroutine can be different from the `CoroutineContext` of the parent since it’s calculated based on this formula:
>
> > **Parent context** *= Defaults + inherited* `CoroutineContext` *+ arguments*

+ 有些元素有默认的值 - `CoroutineDispatcher`默认值为`Dispatchers.Default`，`CoroutineName`的默认值为`coroutine`
+ 继承的`CoroutineContext`是创建`CoroutineScope` or 协程的`CoroutineContext`
+ 在协程构建器中传递的参数将优先于继承上下文中的那些元素

**注意**：可以使用`+`运算符来组合`CoroutineContext`。由于`CoroutineContext`是一组元素，因此将创建一个新的 `CoroutineContext`，其中加号右侧的元素覆盖左侧的元素。如，`(Dispatchers.Main, “name”) + (Dispatchers.IO) = (Dispatchers.IO, “name”)`

![024](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/024.png)

> *CoroutineName*s是灰色的，表示它是默认值

所以，新的协程上下文是：

> **New coroutine context** *= parent* `CoroutineContext` *+* `Job()`

下面的形式：

```kotlin
val job = scope.launch(Dispatchers.IO) {
    // new coroutine
}
```

其父`CoroutineContext`和实际的`CoroutineContext`是什么呢？

![025](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/025.png)

> *The Job in the CoroutineContext and in the parent context will never be the same instance as a new coroutine always get a new instance of a Job*
>
> `CoroutineContext`和其父context的`Job`绝不会是同一个实例。原因是，一个新的协程总是会有一个新的`Job`实例

























