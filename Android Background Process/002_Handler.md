# Handler

[Handler](https://developer.android.google.cn/reference/kotlin/android/os/Handler?hl=en)官方文档对其的介绍：

> A Handler allows you to send and process `Message` and Runnable objects associated with a thread's `MessageQueue`. Each Handler instance is associated with a single thread and that thread's message queue. When you create a new Handler it is bound to a `Looper`. It will deliver messages and runnables to that Looper's message queue and execute them on that Looper's thread.
>
> There are two main uses for a Handler: (1) to schedule messages and runnables to be executed at some point in the future; and (2) to enqueue an action to be performed on a different thread than your own.
>
> Scheduling messages is accomplished with the `post`, `postAtTime(java.lang.Runnable,long)`, #postDelayed, `sendEmptyMessage`, `sendMessage`, `sendMessageAtTime`, and `sendMessageDelayed` methods. The *post* versions allow you to enqueue Runnable objects to be called by the message queue when they are received; the *sendMessage* versions allow you to enqueue a `Message` object containing a bundle of data that will be processed by the Handler's `handleMessage` method (requiring that you implement a subclass of Handler).
>
> When posting or sending to a Handler, you can either allow the item to be processed as soon as the message queue is ready to do so, or specify a delay before it gets processed or absolute time for it to be processed. The latter two allow you to implement timeouts, ticks, and other timing-based behavior.
>
> When a process is created for your application, its main thread is dedicated to running a message queue that takes care of managing the top-level application objects (activities, broadcast receivers, etc) and any windows they create. You can create your own threads, and communicate back with the main application thread through a Handler. This is done by calling the same *post* or *sendMessage* methods as before, but from your new thread. The given Runnable or Message will then be scheduled in the Handler's message queue and processed when appropriate.



原来默认创建`Handler`的方式是：

```java
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

        }
    };
```

但是在我Android 11的模拟器上，特使方法已被废弃

![002](https://github.com/winfredzen/Android-Basic/blob/master/Android%20Background%20Process/images/002.png)

参考：[What do I use now that Handler() is deprecated?](https://stackoverflow.com/questions/61023968/what-do-i-use-now-that-handler-is-deprecated)

> ```java
> new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
>     @Override
>     public void run() {
>         // Your Code
>     }
> }, 3000);
> ```

还有一个关于内存泄露的问题

+ [【技术译文】安卓Handler当做内部类，导致内存泄露的问题](https://www.jianshu.com/p/1b39416f1508)



























































