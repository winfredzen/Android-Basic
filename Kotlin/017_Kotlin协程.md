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






























