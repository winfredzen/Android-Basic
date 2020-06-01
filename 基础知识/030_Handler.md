# Handler

> A Handler allows you to send and process `Message` and Runnable objects associated with a thread's `MessageQueue`. Each Handler instance is associated with a single thread and that thread's message queue. When you create a new Handler it is bound to a `Looper`. It will deliver messages and runnables to that Looper's message queue and execute them on that Looper's thread.

参考：

+ [Android--多线程之Handler](https://www.cnblogs.com/plokmju/p/android_Handler.html)

> [Handler](http://developer.android.com/reference/android/os/Handler.html)，它直接继承自Object，一个`Handler`允许发送和处理`Message`或者`Runnable`对象，并且会关联到主线程的`MessageQueue`中。每个Handler具有一个单独的线程，并且关联到一个消息队列的线程，就是说一个Handler有一个固有的消息队列。当实例化一个Handler的时候，它就承载在一个线程和消息队列的线程，这个Handler可以把`Message`或`Runnable`压入到消息队列，并且从消息队列中取出Message或Runnable，进而操作它们。

**异步消息处理机制示意图**

![051](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/051.png)

