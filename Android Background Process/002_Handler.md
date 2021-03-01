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



## Message

Message是在线程之间传递的消息，它可以在内部携带少量的信息，用于在不同线程之间交换数据。常用属性

+ what - 用于区分handler发送消息的不同线程来学院
+ arg1 - 如果子线程想要向主线程传递整型数据，则可以用使用这些参数
+ obj - 任意对象



## MessageQueue

`MessageQueue`就是消息队列的意思，它主要用于存放所有通过Handler发送过来的消息。这部分消息一直存放于消息队列当中，等待被处理。每个线程中只会有一个`MessageQueue`对象



## Looper

Looper是每个线程中的MessageQueue的管家，调用Looper的loop()方法后，就会进入到一个无限循环当中，然后每当MessageQueue中存在一条消息，Looper就会将这条消息取出，并将它传递到Handler的`handleMessage()`方法中。每个线程只有一个`Looper`对象



**主线程向子线程传递消息**

如下创建一个`Handler`，在主线程发送消息`handler2.sendEmptyMessage(1000)`

然后在子线程中创建Handler，处理消息：

```java
new Thread() {
    @Override
    public void run() {
        super.run();
        handler2 = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.e(TAG, "whate is " + msg.what);
            }
        };
    }
}.start();
```

运行时会有如下的异常：

> java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()

![003](https://github.com/winfredzen/Android-Basic/blob/master/Android%20Background%20Process/images/003.png)

> 系统会自动为主线程开启消息循环，子线程却不会

子线程需主动开启消息循环

```java
        new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                handler2 = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.e(TAG, "whate is " + msg.what);
                    }
                };
                Looper.loop();

            }
        }.start();
```



## Handler其它方法

1.`boolean post(@NonNull Runnable r)`

> Causes the Runnable r to be added to the message queue. The runnable will be run on the thread to which this handler is attached.

如下所示，使用post方法来在修改UI显示：

```java
private Handler handler = new Handler();
......
new Thread(){
    @Override
    public void run() {
        super.run();
        Log.d(TAG, "Outter Thread is : " + Thread.currentThread().getName());
        handler.post(new Runnable() {
            @Override
            public void run() {
                btn.setImageResource(R.mipmap.stop);
                Log.d(TAG, "Inner Run Thread is : " + Thread.currentThread().getName());
            }
        });
    }
}.start();
```

输出结果为：

> 2021-03-01 13:41:10.927 1868-1898/com.example.handlerdemo D/TimerActivity2: Outter Thread is : Thread-3
> 2021-03-01 13:41:10.930 1868-1868/com.example.handlerdemo D/TimerActivity2: Inner Run Thread is : main

即`post`方法中的`Runnable`可以运行在主线程中



2.`boolean postDelayed(@NonNull Runnable r, long delayMillis)`

> Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses. The runnable will be run on the thread to which this handler is attached. The time-base is `SystemClock.uptimeMillis`. Time spent in deep sleep will add an additional delay to execution.

可延迟执行



3.`boolean postAtTime(@NonNull Runnable r, long uptimeMillis)`

> Causes the Runnable r to be added to the message queue, to be run at a specific time given by uptimeMillis. The time-base is `SystemClock.uptimeMillis`. Time spent in deep sleep will add an additional delay to execution. The runnable will be run on the thread to which this handler is attached.

有可以实现延迟执行的效果：

```java
handler.postAtTime(new Runnable() {
    @Override
    public void run() {
        btn.setImageResource(R.mipmap.stop);
    }
}, SystemClock.uptimeMillis() + 3000);
```



4.`void removeCallbacks(@NonNull Runnable r)`

> Remove any pending posts of Runnable r that are in the message queue.

移除Runnable



## 内存泄露的问题

参考：

+ [【技术译文】安卓Handler当做内部类，导致内存泄露的问题](https://www.jianshu.com/p/1b39416f1508)

主要是在延迟发送消息的时候，按返回键，Activity要被销毁时，循环引用导致的

如何解决呢？

1.在`onDestroy()`方法中，移除message和runnable

```java

/**
 * 内存泄漏
 */
public class MemoryLeakActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            textView.setText("收到消息");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak);

        button = findViewById(R.id.button);
        textView  = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                handler.sendEmptyMessageDelayed(1, 5000);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacksAndMessages(null);
    }
}
```



2.弱引用

```java
    private static class MyHandler extends Handler {

        private WeakReference<MemoryLeakActivity> weakReference;

        public MyHandler(MemoryLeakActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            MemoryLeakActivity activity = weakReference.get();
            activity.textView.setText("xxxx");
        }
    }
```































