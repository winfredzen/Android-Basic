# Handler

> A Handler allows you to send and process `Message` and Runnable objects associated with a thread's `MessageQueue`. Each Handler instance is associated with a single thread and that thread's message queue. When you create a new Handler it is bound to a `Looper`. It will deliver messages and runnables to that Looper's message queue and execute them on that Looper's thread.

参考：

+ [Android--多线程之Handler](https://www.cnblogs.com/plokmju/p/android_Handler.html)

> [Handler](http://developer.android.com/reference/android/os/Handler.html)，它直接继承自Object，一个`Handler`允许发送和处理`Message`或者`Runnable`对象，并且会关联到主线程的`MessageQueue`中。每个Handler具有一个单独的线程，并且关联到一个消息队列的线程，就是说一个Handler有一个固有的消息队列。当实例化一个Handler的时候，它就承载在一个线程和消息队列的线程，这个Handler可以把`Message`或`Runnable`压入到消息队列，并且从消息队列中取出Message或Runnable，进而操作它们。
> Handler主要有两个作用：
> + 在工作线程中发送消息。
> + 在UI线程中获取、处理消息。

**异步消息处理机制示意图**

![051](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/051.png)

**1.Message**

Message是在线程之间传递的消息，可以在内部携带少量的信息，用于在不同线程之间交换数据

+ what - 用户定义的int型消息代码，用来描述消息
+ obj - 用户指定，随消息发送的对象
+ target - 处理消息的Handler

> Message的目标(target)是一个Handler类实例。Handler可看作message handler的简称。创建Message时，它会自动与一个Handler相关联。Message待处理时，Handler对象负责触发消息处理事件



**2.Handler**

主要用于发送和处理消息



**3.MessageQueue**

消息队列，用于存放所用通过Handler发送的消息。这部分消息会一直存在于消息队列中，等待被处理。每个线程中只会有一个MessageQueue对象



**4.Looper**

Looper是每个线程中的MessageQueue的管家，调用Looper的`loop()`方法后，就会进入到一个无限循环当中，然后每当发现MessageQueue中存在一条消息，就会将它取出，并传递到Handler的`handleMessage()`方法中。每个线程中也只会有一个Looper对象



> 上面介绍到Handler可以把一个Message对象或者Runnable对象压入到消息队列中，进而在UI线程中获取Message或者执行Runnable对象，所以Handler把压入消息队列有两大体系，Post和sendMessage：
>
> + Post：Post允许把一个Runnable对象入队到消息队列中。它的方法有：post(Runnable)、postAtTime(Runnable,long)、postDelayed(Runnable,long)
> + sendMessage：sendMessage允许把一个包含消息数据的Message对象压入到消息队列中。它的方法有：sendEmptyMessage(int)、sendMessage(Message)、sendMessageAtTime(Message,long)、sendMessageDelayed(Message,long)

> 对于Message对象，一般并不推荐直接使用它的构造方法得到，而是建议通过使用`Message.obtain()`这个静态的方法或者`Handler.obtainMessage()`获取。`Message.obtain()`会从消息池中获取一个Message对象，如果消息池中是空的，才会使用构造方法实例化一个新Message，这样有利于消息资源的利用。并不需要担心消息池中的消息过多，它是有上限的，上限为10个。`Handler.obtainMessage()`具有多个重载方法，如果查看源码，会发现其实`Handler.obtainMessage()`在内部也是调用的`Message.obtain()`。

> Message.obtain()方法具有多个重载方法，大致可以分为为两类，一类是无需传递Handler对象，对于这类的方法，当填充好消息后，需要调用`Handler.sendMessage()`方法来发送消息到消息队列中。第二类需要传递一个Handler对象，这类方法可以直接使用`Message.sendToTarget()`方法发送消息到消息队列中，这是因为在Message对象中有一个私有的Handler类型的属性Target，当时obtain方法传递进一个Handler对象的时候，会给Target属性赋值，当调用`sendToTarget()`方法的时候，实际在它内部还是调用的`Target.sendMessage()`方法。

















