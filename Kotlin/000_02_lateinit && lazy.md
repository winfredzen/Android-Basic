# lateinit && lazy



## lateinit

`lateinit`用来延迟初始化，**lateinit 只用于变量 var**

```kotlin
private lateinit var textView: TextView
```

使用`lateinit`有什么好处？以《第一行代码》中的例子来说：

```kotlin
private var adapter: MsgAdapter? = null
```

在使用时需要做非空判断，否则编译可能不会通过

```kotlin
adapter?.notifyItemInserted(msgList.size - 1)
```

如果代码中有很多这样的变量时，这个问题就会变得越来越明显。**必须编写大量额外的判空处理代码，只是为了满足Kotlin编译器的要求**

延迟初始化使用的是`lateinit`关键字，它可以告诉Kotlin编译器，我会在晚些时候对这个变量进行初始化，这样就不用在一开始的时候将它赋值为`null`了

```kotlin
private lateinit var adapter: MsgAdapter

adapter.notifyItemInserted(msgList.size - 1)
```

如果我们在`adapter`变量还没有初始化的 情况下就直接使用它，那么程序就一定会崩溃，并且抛出一个`UninitializedPropertyAccessException`异常

另外，我们还可以通过代码来判断一个全局变量是否已经完成了初始化

```kotlin
 if (!::adapter.isInitialized) {
 		adapter = MsgAdapter(msgList)
 }
```

`::adapter.isInitialized`可用于判断`adapter`变量是否已经初始化







## lazy

懒加载，即初始化方式已确定，只是在使用的时候执行

只能用在`val`变量上



lazy还可以指定`LazyThreadSafetyMode`，其方法定义如下：

```kotlin
/**
 * Creates a new instance of the [Lazy] that uses the specified initialization function [initializer]
 * and thread-safety [mode].
 *
 * If the initialization of a value throws an exception, it will attempt to reinitialize the value at next access.
 *
 * Note that when the [LazyThreadSafetyMode.SYNCHRONIZED] mode is specified the returned instance uses itself
 * to synchronize on. Do not synchronize from external code on the returned instance as it may cause accidental deadlock.
 * Also this behavior can be changed in the future.
 */
public actual fun <T> lazy(mode: LazyThreadSafetyMode, initializer: () -> T): Lazy<T> =
    when (mode) {
        LazyThreadSafetyMode.SYNCHRONIZED -> SynchronizedLazyImpl(initializer)
        LazyThreadSafetyMode.PUBLICATION -> SafePublicationLazyImpl(initializer)
        LazyThreadSafetyMode.NONE -> UnsafeLazyImpl(initializer)
    }
```

具体解释可参考：[LazyThreadSafetyMode](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-lazy-thread-safety-mode/)

+ `SYNCHRONIZED` - 上锁为了保证只有一条线程可去初始化`lazy`属性。也就是说同时多线程进行访问该延迟属性时，一旦没有初始化好，其他线程将无法访问
+ `PUBLICATION` - 还没有被初始化的`lazy`对象，初始化的方法可以被不同的线程调用很多次，直到有一个线程初始化先完成，那么其他的线程都将使用这个初始化完成的值
+ `NONE` - 不会对任何访问和初始化上锁，也就是说完全放任





使用方式，如：

```kotlin
    private val homeAdapter by lazy(LazyThreadSafetyMode.NONE) {
        HomeAdapter(homeItemData)
    }

    private val helper by lazy(LazyThreadSafetyMode.NONE) {
        QuickAdapterHelper.Builder(homeAdapter)
            .build()
            .addBeforeAdapter(HomeTopHeaderAdapter())
    }
```



## 其它

参考：

+ [Kotlin常用的by lazy你真的了解吗](https://juejin.cn/post/7057675598671380493)

