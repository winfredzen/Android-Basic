# traceview

参考：

+ [使用 Traceview 检查跟踪日志](https://developer.android.com/studio/profile/traceview)
+ [Android 性能优化：使用 TraceView 找到卡顿的元凶](https://blog.csdn.net/u011240877/article/details/54347396)



官方文档有如下的说明：

> **Traceview 已弃用**。如果您使用的是 Android Studio 3.2 或更高版本，应改为使用 [**CPU 性能分析器**](https://developer.android.com/studio/profile/cpu-profiler)来执行以下操作：检查通过使用 [`Debug`](https://developer.android.com/reference/android/os/Debug) 类[检测应用](https://developer.android.com/studio/profile/generate-trace-logs)而捕获的 `.trace` 文件，记录新方法轨迹，保存 `.trace` 文件，以及检查应用进程的实时 CPU 使用情况。

traceview的特点：

+ 图形的形式展示执行时间、调用栈等
+ 信息全面，包含所有线程

使用方式：

```java
Debug.startMethodTracing("Weather");
// ...
Debug.stopMethodTracing();
```

保存的位置是sd卡下的`Android/data/Package/files`

![010](https://github.com/winfredzen/Android-Basic/blob/master/%E4%BC%98%E5%8C%96/images/010.png)



打开这个`.trace`文件

![011](https://github.com/winfredzen/Android-Basic/blob/master/%E4%BC%98%E5%8C%96/images/011.png)

![012](https://github.com/winfredzen/Android-Basic/blob/master/%E4%BC%98%E5%8C%96/images/012.png)



`Self` - 执行自己代码的时间，`Children` - 调用其它代码所使用的时间

`Top Down` - 函数的调用列表



![013](https://github.com/winfredzen/Android-Basic/blob/master/%E4%BC%98%E5%8C%96/images/013.png)

+ 橙色 - 系统API
+ 绿色 - 应用自身的API
+ 蓝色 - 第三方的API





总结：

+ `traceview`运行开销严重，整体会变慢
+ 可能会带偏优化的方向
+ 虽然在新版中使用的cpu profiler，但是`traceview`可以在代码中埋点，更方便，更准确















































