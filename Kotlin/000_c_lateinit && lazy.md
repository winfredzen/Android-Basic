# lateinit && lazy



## lateinit

`lateinit`用来延迟初始化，**lateinit 只用于变量 var**

```kotlin
private lateinit var textView: TextView
```



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

