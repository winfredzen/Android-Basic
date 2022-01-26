# viewModelScope

内容来自：

+ [Easy Coroutines in Android: viewModelScope](https://medium.com/androiddevelopers/easy-coroutines-in-android-viewmodelscope-25bffb605471)

在ViewModel销毁的时候，其中所有的异步工作都必须停止。否则的话，可能会有资源的浪费和内存的泄漏

> If you consider that certain asynchronous work should persist after ViewModel destruction, it is because it should be done in a lower layer of your app’s architecture.
>
> 如果你想要在ViewModel销毁后，异步工作能够继续存在，考虑在更低的层面来使用它

通常ViewModel的样板代码如下：

```kotlin
class MyViewModel : ViewModel() {

    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     * job可以用来取消所有的协程
     */
    private val viewModelJob = SupervisorJob()
    
    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     * Since we pass viewModelJob, you can cancel all coroutines 
     * launched by uiScope by calling viewModelJob.cancel()
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    
    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    
    /**
     * Heavy operation that cannot be done in the Main Thread
     */
    fun launchDataLoad() {
        uiScope.launch {
            sortList() // happens on the background
            // Modify UI
        }
    }
    
    // Move the execution off the main thread using withContext(Dispatchers.Default)
    suspend fun sortList() = withContext(Dispatchers.Default) {
        // Heavy work
    }
}
```

如上，添加了一个`CoroutineScope`到ViewModel中，使用的是`SupervisorJob`

当ViewModel销毁的时候，由特定`uiScope`启动的协程，后台任务会被取消



## viewModelScope减少模板代码

[AndroidX lifecycle v2.1.0](https://developer.android.com/jetpack/androidx/releases/lifecycle)引入了viewModelScope，和上面的内容基本类似，简化成如下的内容:

```kotlin
class MyViewModel : ViewModel() {
  
    /**
     * Heavy operation that cannot be done in the Main Thread
     */
    fun launchDataLoad() {
        viewModelScope.launch {
            sortList()
            // Modify UI
        }
    }
  
    suspend fun sortList() = withContext(Dispatchers.Default) {
        // Heavy work
    }
}
```

CoroutineScope的创建和取消都已经做好了。在要使用时，在`build.gradle`中引入

```kotlin
implementation "androidx.lifecycle.lifecycle-viewmodel-ktx$lifecycle_version"
```



## 深入viewModelScope

viewModelScope的实现如下：

```kotlin
private const val JOB_KEY = "androidx.lifecycle.ViewModelCoroutineScope.JOB_KEY"

val ViewModel.viewModelScope: CoroutineScope
    get() {
        val scope: CoroutineScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        return setTagIfAbsent(JOB_KEY,
            CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate))
    }
```

当ViewModel被清除时，在调用`onCleared()`方法之前，会先执行`clear()`方法

```kotlin
@MainThread
final void clear() {
    mCleared = true;
    // Since clear() is final, this method is still called on mock 
    // objects and in those cases, mBagOfTags is null. It'll always 
    // be empty though because setTagIfAbsent and getTag are not 
    // final so we can skip clearing it
    if (mBagOfTags != null) {
        for (Object value : mBagOfTags.values()) {
            // see comment for the similar call in setTagIfAbsent
            closeWithRuntimeException(value);
        }
    }
    onCleared();
}
```



## 默认为Dispatchers.Main

viewModelScope默认的[CoroutineDispatcher](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-coroutine-dispatcher/index.html) 是`Dispatchers.Main.immediate`

```kotlin
val scope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
```

ViewModel的概念是和UI相关联的，所以是`Dispatchers.Main`

