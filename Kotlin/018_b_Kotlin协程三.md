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



**Job**

`Job`是协程的句柄。使用 `launch` 或 `async` 创建的每个协程都会返回一个 `Job` 实例，该实例是相应协程的唯一标识并管理其生命周期。还可以将 `Job` 传递给 `CoroutineScope` 以进一步管理其生命周期

`Job`有如下的状态，New, Active, Completing, Completed, Cancelling 和 Cancelled

![026](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/026.png)



## Cancellation in coroutines

内容来自：[Cancellation in coroutines](https://medium.com/androiddevelopers/cancellation-in-coroutines-aa6b90163629)

可以取消启动协程的整个作用域，因为这将取消所有创建的子协程：

```kotlin
// assume we have a scope defined for this layer of the app
val job1 = scope.launch { … }
val job2 = scope.launch { … }
scope.cancel()
```

也可以取消单个协程，取消单个协程，并不会影响其他的兄弟协程

```kotlin
// assume we have a scope defined for this layer of the app
val job1 = scope.launch { … }
val job2 = scope.launch { … }
// First coroutine will be cancelled and the other one won’t be affected
job1.cancel()
```

cancel的定义如下

```kotlin
public fun cancel(cause: CancellationException? = null)
```

> Once you cancel a scope, you won’t be able to launch new coroutines in the cancelled scope
>
> 取消scope后，将无法在取消的scope内启动新的协程

如果你使用了androidx KTX库，在绝大数情况下，你不创建自己的scope，你就不必负责取消他们

> If you’re working in the scope of a `ViewModel`, using `viewModelScope` or, if you want to launch coroutines tied to a lifecycle scope, you would use the `lifecycleScope`. Both `viewModelScope` and `lifecycleScope` are `CoroutineScope` objects that get cancelled at the right time. For example, [when the ViewModel is cleared](https://medium.com/androiddevelopers/easy-coroutines-in-android-viewmodelscope-25bffb605471), it cancels the coroutines launched in its scope.
>
> 如果使用了`viewModelScope`和`lifecycleScope`，在合适的时间，它们会自动取消

**为什么协程work不停止？**

如果仅仅是调用cancel，并不意味着协程work会停止

如下的例子：

```kotlin
runBlocking<Unit> {
    val startTime = System.currentTimeMillis()
    val job = launch (Dispatchers.Default) {
        println("current thread = " + Thread.currentThread().name)
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            // print a message twice a second
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("Hello ${i++}")
                nextPrintTime += 500L
            }
        }
    }
    delay(1000L)
    println("Cancel!")
    job.cancel()
    println("Done!")
}
```

打印输出如下：

```kotlin
2022-01-25 14:23:31.522 17909-17971/com.example.kotlincoroutinedemo I/System.out: Hello 0
2022-01-25 14:23:31.994 17909-17971/com.example.kotlincoroutinedemo I/System.out: Hello 1
2022-01-25 14:23:32.494 17909-17971/com.example.kotlincoroutinedemo I/System.out: Hello 2
2022-01-25 14:23:32.526 17909-17909/com.example.kotlincoroutinedemo I/System.out: Cancel!
2022-01-25 14:23:32.528 17909-17909/com.example.kotlincoroutinedemo I/System.out: Done!
2022-01-25 14:23:32.994 17909-17971/com.example.kotlincoroutinedemo I/System.out: Hello 3
2022-01-25 14:23:33.494 17909-17971/com.example.kotlincoroutinedemo I/System.out: Hello 4
```

会发现取消后，还在继续输出

> 可见取消后，协程work并没有停止。我们须有周期性的检查协程是否是active

`kotlinx.coroutines` 中的所有挂起函数都是可取消的：`withContext`、`delay` 等

有两种方式，解决上面的问题

1.通过`job.isActive`或者`ensureActive()`

2.使用`yield()`

**检查job的活动状态**

```kotlin
// Since we're in the launch block, we have access to job.isActive
while (i < 5 && isActive)
```

`ensureActive()`方法的实现是：

```kotlin
fun Job.ensureActive(): Unit {
    if (!isActive) {
         throw getCancellationException()
    }
}
```

使用方式如下：

```kotlin
while (i < 5) {
    ensureActive()
    …
}
```



**Job.join vs Deferred.await 取消**

有两种方式从协程中等待结果：`launch`返回的job可以调用`join`，`async`返回的`Deferred`（a type of `Job`）可使用`await`

`Job.join`会挂起协程直至work完成，结合`job.cancel`使用时有两种情况

+ 先调用`job.cancel`再调用`job.join`，协程会挂起直至完成
+ 在调用`job.join`再调用`job.cancel`没有影响，此时job已经完成了



如果对协程的结果感兴趣，可使用`Deferred`，当协程完成时，结果通过`Deferred.await`返回，`Deferred`也是一种`Job`，也可以被取消

在已cancelled的deferred上调用`await`，会抛出`JobCancellationException`

```kotlin
val deferred = async { … }
deferred.cancel()
val result = deferred.await() // throws JobCancellationException!
```



## Exceptions in coroutines

内容来自:[Exceptions in coroutines](https://medium.com/androiddevelopers/exceptions-in-coroutines-ce8da1ec060c)

当协程因异常而失败时，它将将该异常传播到其父级！ 然后，父级将 1) 取消其余子级，2) 取消自身 3) 将异常传播到其父级

异常将到达层次结构的root，并且 `CoroutineScope` 启动的所有协程也将被取消

![027](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/027.gif)

> 协程中异常会通过协程的层级传播

想象一下，假设一个UI相关`CoroutineScope`来处理用户交互，如果一个子协程抛出了一个异常，则UI scope就会被取消，整个UI组件都变的不响应，由于取消的协程不能再启动更多的协程

这种情况肯定不是我们想要的。所以，使用`SupervisorJob`

使用 `SupervisorJob`，一个child的失败不会影响其他child。 `SupervisorJob` 不会取消自己或其他子项。 此外，`SupervisorJob` 也不会传播异常，而是让子协程处理它

通过`val uiScope = CoroutineScope(SupervisorJob())`方式创建协程，以在协程失败时不传播取消

![028](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/028.png)

> *A SupervisorJob won’t cancel itself or the rest of its children*
>
> SupervisorJob不会取消自己或其他子项



如果异常没出处理，而且`CoroutineContext`没有一个`CoroutineExceptionHandler`，它将到达默认线程的 `ExceptionHandler`。 在JVM中，异常会被记录到控制台； 在 Android 中，无论发生在哪个 Dispatcher 上，它都会使您的应用程序崩溃

这种行为有适用于scope构造器`coroutineScope`和`supervisorScope`，会创建一个sub-scope（with a Job or a SupervisorJob accordingly as a parent）。可以使用该sub-scope对协程进行逻辑分组（例如，如果您想要进行并行计算，或者您希望它们相互影响或不受影响）

***Warning\***: A `SupervisorJob` **only** works as described when it’s part of a scope: either created using `supervisorScope` or `CoroutineScope(SupervisorJob())`.

> 使用`supervisorScope`或者使用 `CoroutineScope(SupervisorJob())`创建`SupervisorJob`



### 处理异常

协程使用Kotlin语法来处理异常：`try/catch`，或者内置的帮助方法[runCatching](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/run-catching.html)

我们之前说过，未捕获的异常总是会被抛出。 但是，不同的协程构建器以不同的方式处理异常。

**Launch**

对launch来说，异常发生时就会被抛出来，所以可使用`try/catch`来捕获，如下的例子：

```kotlin
scope.launch {
    try {
        codeThatCanThrowExceptions()
    } catch(e: Exception) {
        // Handle exception
    }
}
```





















