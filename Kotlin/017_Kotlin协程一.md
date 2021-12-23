# 协程

内容来自:

+ [你能听懂的Kotlin协程课，跟老司机学，不用自己瞎折腾](https://www.bilibili.com/video/BV1uo4y1y7ZF)

协程是基于线程，它是轻量级线程

解决什么问题？

+ 处理耗时任务
+ 保证主线程安全

通过网络请求示例来说明，使用之前配置`retrofit`等

```groovy
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")
implementation "com.squareup.retrofit2:converter-moshi:2.9.0"
// 协程核心库
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"
// 协程Android支持库
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3"
```

注意这里的`retrofit`版本，原来我使用的是2.0.X的版本，导致下面的例子出错了，网上有说

> Retrofit 2.6之后的版本本身支持了使用Kotlin的协程。使用起来更加简洁。

**使用异步任务来获取网络数据**

```kotlin
data class Todo (val userId: Int, val id: Int, val title: String, val completed: Boolean)

val userServiceApi: UserServiceApi by lazy {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY) //需要设置这个Level貌似才生效

    var okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    var retrofit = Retrofit.Builder().client(okHttpClient)
        .baseUrl("https://jsonplaceholder.typicode.com/").
        addConverterFactory(GsonConverterFactory.create())
        .build()
    retrofit.create(UserServiceApi::class.java)
}

interface UserServiceApi {
    @GET("/todos/{id}")
    fun getTodoById(@Path("id") id: Int): Call<Todo>
}
```

获取网络数据如下：

```kotlin
val submitButton = findViewById<Button>(R.id.submitButton).also {
    it.setOnClickListener {
        Log.d("TAG", "OnClickListener")
        object : AsyncTask<Void, Void, Todo>() {
            override fun doInBackground(vararg params: Void?): Todo? {
                return userServiceApi.getTodoById(1).execute().body()
            }
            override fun onPostExecute(result: Todo?) {
                super.onPostExecute(result)
                nameTextView.text = result?.title
            }
        }.execute()
    }
}
```

**使用协程的方式**

```java
@GET("/todos/{id}")
suspend fun retrieveTodoById(@Path("id") id: Int): Todo
```

这里的请求接口，使用了`suspend`关键字，然后返回结果，直接是数据类`Todo`

```kotlin
val submitButton = findViewById<Button>(R.id.submitButton).also {
    it.setOnClickListener {
        Log.d("TAG", "OnClickListener")
        GlobalScope.launch(Dispatchers.Main) {
            val todo = withContext(Dispatchers.IO) {//切换到子线程
                userServiceApi.retrieveTodoById(1)
            }
            nameTextView.text = todo.title
        }
    }
}
```

> **一些分析**
> 
> `launch`方法声明如下
> 
> ```kotlin
> public fun CoroutineScope.launch(
>     context: CoroutineContext = EmptyCoroutineContext,
>     start: CoroutineStart = CoroutineStart.DEFAULT,
>     block: suspend CoroutineScope.() -> Unit
> ): Job
> ```

> 方法说明：`Launches a new coroutine without blocking the current thread and returns a reference to the coroutine as a [Job]`

> `withContext`方法声明
> 
> Calls the specified suspending block with a given coroutine context, suspends until it completes, and returns the result.

> ```kotlin
> public suspend fun <T> withContext(
>     context: CoroutineContext,
>     block: suspend CoroutineScope.() -> T
> ): T
> ```

**协程是什么？**

+ 协程让异步逻辑同步化，杜绝回调地狱

+ 协程最核心的点就是，函数或一段程序能够被挂起，稍后再在挂起的时候恢复

重构上面的例子：

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameTextView = findViewById<TextView>(R.id.textView)
        nameTextView.text = "Jack"

        val submitButton = findViewById<Button>(R.id.submitButton).also {
            it.setOnClickListener {
                Log.d("TAG", "OnClickListener")
                GlobalScope.launch(Dispatchers.Main) {
                    getTodo()
                }
            }
        }

    }

    private suspend fun getTodo() {
        val todo = get()
        show(todo)
    }

    private suspend fun get() = withContext(Dispatchers.IO) {
        userServiceApi.retrieveTodoById(1)
    }

    private fun show(todo: Todo) {
        nameTextView.text = todo.title
    }
```

## 挂起函数

使用`suspend`关键字修饰的函数叫做挂起函数

挂起函数只能在**协程体内**或**其他挂起函数**内使用

```java
val submitButton = findViewById<Button>(R.id.submitButton).also {
    it.setOnClickListener {
        Log.d("TAG", "OnClickListener")
        GlobalScope.launch(Dispatchers.Main) {
            //挂起
            delay(10000)
            Log.d("MainActivity04", "delay " + "${Thread.currentThread().name}")
        }
        //阻塞
        Thread.sleep(10000)
        Log.d("MainActivity04", "sleep " + "${Thread.currentThread().name}")
    }
}
```

打印输出如下：

```java
2021-12-23 14:19:55.082 25801-25801/com.example.coroutine D/MainActivity04: sleep main
2021-12-23 14:20:05.122 25801-25801/com.example.coroutine D/MainActivity04: delay main
2021-12-23 14:20:19.148 25801-25801/com.example.coroutine D/MainActivity04: sleep main
2021-12-23 14:20:29.179 25801-25801/com.example.coroutine D/MainActivity04: delay main
```

> 阻塞时，按下的按钮，一直处于被按下的状态

> `delay`方法的声明
> 
> Delays coroutine for a given time without blocking a thread and resumes it after a specified time.
> 
> ```kotlin
> public suspend fun delay(timeMillis: Long)
> ```

## 协程的两部分

Kotlin的协程实现分为2个层次

+ 基础设施层：标准库的协程API，主要对协程提供了概念和语义上最基本的支持

+ 业务框架层：协程的上层框架支持

```kotlin
//协程的挂起点通过continuation保存起来
val continuation = suspend {
    5
}.createCoroutine(object : Continuation<Int> {
    override val context: CoroutineContext = EmptyCoroutineContext
    override fun resumeWith(result: Result<Int>) {
        Log.d("MainActivity05", "Coroutine End")
    }
})
continuation.resume(Unit)
```

注意导入包的区别

```kotlin
import kotlinx.coroutines.*
import kotlin.coroutines.*
```

> `suspend`方法
> 
> ```kotlin
> public inline fun <R> suspend(noinline block: suspend () -> R): suspend () -> R = block
> ```

> `createCoroutine`方法
> 
> Creates a coroutine without a receiver and with result type T. This function creates a new, fresh instance of suspendable computation every time it is invoked.
> 
> ```kotlin
> public fun <T> (suspend () -> T).createCoroutine(
>     completion: Continuation<T>
> ): Continuation<Unit> 
> ```

### Continuation

`Continuation`是一个接口

```kotlin
/**
 * Interface representing a continuation after a suspension point that returns a value of type `T`.
 */
@SinceKotlin("1.3")
public interface Continuation<in T> {
    /**
     * The context of the coroutine that corresponds to this continuation.
     */
    public val context: CoroutineContext

    /**
     * Resumes the execution of the corresponding coroutine passing a successful or failed [result] as the
     * return value of the last suspension point.
     */
    public fun resumeWith(result: Result<T>)
}
```

## 调度器

![018](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/018.png)

## CoroutineScope

协程作用域(`CoroutineScope`)是协程运行的作用范围，它会跟踪所有协程，还可以取消由它所启动的所有协程

+ GlobalScope - 生命周期是process级别的，即使Activity或者Fragment已近被销毁，协程任然执行

+ MainScope - 在Activity中使用，可以在`onDestroy()`中取消协程

+ viewModelScope - 只能在ViewModel中使用，绑定ViewModel的生命周期

+ lifeCycleScope - 只能在Activity、Fragment中使用，会绑定Activity、Fragment的生命周期

添加相关的依赖，可参考[KTX 扩展程序列表](https://developer.android.com/kotlin/ktx/extensions-list?hl=zh-cn#androidxlifecycle)

```groovy
    implementation "androidx.activity:activity-ktx:1.4.0"
    implementation "androidx.lifecycle:lifecycle-livedata-core-ktx:2.4.0"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.4.0"
    implementation "androidx.lifecycle:lifecycle-reactivestreams-ktx:2.4.0"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"
```

`CoroutineScope`是一个接口

```kotlin
public interface CoroutineScope {
    /**
     * The context of this scope.
     * Context is encapsulated by the scope and used for implementation of coroutine builders that are extensions on the scope.
     * Accessing this property in general code is not recommended for any purposes except accessing the [Job] instance for advanced usages.
     *
     * By convention, should contain an instance of a [job][Job] to enforce structured concurrency.
     */
    public val coroutineContext: CoroutineContext
}
```

### MainScope

```kotlin
/**
 * Creates the main [CoroutineScope] for UI components.
 *
 * Example of use:
 * ```
 * class MyAndroidActivity {
 *     private val scope = MainScope()
 *
 *     override fun onDestroy() {
 *         super.onDestroy()
 *         scope.cancel()
 *     }
 * }
 * ```
 *
 * The resulting scope has [SupervisorJob] and [Dispatchers.Main] context elements.
 * If you want to append additional elements to the main scope, use [CoroutineScope.plus] operator:
 * `val scope = MainScope() + CoroutineName("MyActivity")`.
 */
@Suppress("FunctionName")
public fun MainScope(): CoroutineScope = ContextScope(SupervisorJob() + Dispatchers.Main)
```

如下的网路请求

```kotlin
private val mainScope = MainScope()


val submitButton = find
ViewById<Button>(R.id.submitButton).also {
    it.setOnClickListener {
        mainScope.launch {
            val todo = userServiceApi.retrieveTodoById(1)
            nameTextView.text = todo.title
        }
    }
}

override fun onDestroy() {
    super.onDestroy()
    mainScope.cancel()
}
```

mainScope取消的时候，如下模拟一个耗时的任务，按返回键取消它

```kotlin
        val submitButton = findViewById<Button>(R.id.submitButton).also {
            it.setOnClickListener {
                mainScope.launch {
//                    val todo = userServiceApi.retrieveTodoById(1)
//                    nameTextView.text = todo.title
                    try {
                        delay(10000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
```

抛出如下的异常`kotlinx.coroutines.JobCancellationException: Job was cancelled; job=SupervisorJobImpl{Cancelling}@663af6a`

还有这样的写法，效果是一样的

```kotlin
class MainActivity06 : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var nameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameTextView = findViewById<TextView>(R.id.textView)
        nameTextView.text = "Jack"

        val submitButton = findViewById<Button>(R.id.submitButton).also {
            it.setOnClickListener {
                launch {
                    val todo = userServiceApi.retrieveTodoById(1)
                    nameTextView.text = todo.title
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}
```



## 协程+Retrofit+ViewModel+LiveData+DataBinding

为什么使用ViewModel？

ViewModel会保存界面上的状态

在前面的例子上，如果在获取网络数据后，旋转屏幕后，会发现界面显示就不一样了

![019]()

![020](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/020.png)

先启动dataBinding的支持

```groovy
    dataBinding {
        enabled = true
    }
```




























