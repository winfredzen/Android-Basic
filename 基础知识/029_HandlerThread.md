# HandlerThread

[HandlerThread](https://developer.android.com/reference/android/os/HandlerThread)是Thread的子类，它有一个`Looper`，这个`Looper`可以用来创建`Handler`

`Thread.start()`必须被调用

使用场景：

> HandlerThread所做的就是在新开的子线程中创建Looper，所以它的使用场景就是Thread + Looper使用场景的结合，即：`在子线程中执行耗时，多任务的操作。`
> HandlerThread的特点：`单线程串行执行任务`。

优缺点：

+ 只要开启一个线程，就可以处理多个耗时任务
+ 任务是串行执行的，不能并行执行。一旦队列中有某个任务执行时间过长，就会导致后续的任务都会被延迟处理

**注意事项**

> 1. `HandlerThread`不再需要使用的时候，要调用`quitSafe()`或者`quit()`方法来结束线程。
> 2. `quitSafe()`会等待正在处理的消息处理完后再退出，而`quit()`不管是否正在处理消息，直接移除所有回调

## 简单使用

参考：

+ [详解 Android 中的 HandlerThread](https://droidyue.com/blog/2015/11/08/make-use-of-handlerthread/)

+ [HandlerThread解析](https://zhuanlan.zhihu.com/p/60814560)
+ [HandlerThread详解](https://www.jianshu.com/p/5b6c71a7e8d7)

**使用普通的Thread来创建一个Handler**

```java
Handler mHandler;
private void createManualThreadWithHandler() {
  new Thread() {
      @Override
        public void run() {
            super.run();
            Looper.prepare();
            mHandler = new Handler(Looper.myLooper());
            Looper.loop();
        }
    }.start();
}
```

+ 调用`Looper.prepare` 创建与当前线程绑定的`Looper`实例
+ 使用上面创建的`Looper`生成`Handler`实例
+ 调用`Looper.loop()`实现消息循环



**直接使用HandlerThread**

```java
HandlerThread handlerThread = new HandlerThread("handler-thread");
handlerThread.start(); // 必须在Handler创建前调用，因为线程start后才会创建Looper

Handler threadHandler = new Handler(handlerThread.getLooper()) {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        // 处理消息，因为这个方法是在子线程调用，所以可以在这执行耗时任务
    }
};
```

