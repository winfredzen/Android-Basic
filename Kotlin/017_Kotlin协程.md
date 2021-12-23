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

```java
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

```java
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






























