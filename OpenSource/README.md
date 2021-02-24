# EventBus

文档与教程

+ [EventBus Documentation](https://greenrobot.org/eventbus/documentation/)



不使用`EventBus`的方案

**1.使用监听器**

![001](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/001.png)

如下的例子：

`PublisherDialogFragment`相当于`Publisher`

```java
public class PublisherDialogFragment extends DialogFragment {

    private static final String TAG = "PublisherDialogFragment";

    private OnEventListener mListener;

    public interface OnEventListener {
        void onSuccess();
        void onFailure();
    }

    public void setEventListener(OnEventListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Publisher");
        final String[] items = {"Success", "Failure"}; //设置对话框要显示的一个list，一般用于显示几个命令时
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //succ
                        if (mListener != null) {
                            mListener.onSuccess();
                        }
                        break;
                    case 1:
                        //fail
                        if (mListener != null) {
                            mListener.onFailure();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        return builder.create();
    }
}
```

在`SubScriber`中监听

```java
final PublisherDialogFragment fragment = new PublisherDialogFragment();
fragment.setEventListener(new PublisherDialogFragment.OnEventListener() {
    @Override
    public void onSuccess() {
        setImageSrc(R.drawable.ic_happy);
    }
    @Override
    public void onFailure() {
        setImageSrc(R.drawable.ic_sad);
    }
});
fragment.show(getSupportFragmentManager(), "publiser");
```



**2.使用本地广播**

![002](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/002.png)

> 如果是在AndroidX中使用，注意`LocalBroadcastManager`已废弃，要使用的话，要导入
>
> ```groovy
> implementation 'androidx.localbroadcastmanager:localbroadcastmanager:1.0.0'
> ```

注册广播：

```java
    public static final String HANDLE_EVENT_ACTION = "handle_event_action";

    public static final String STATUS_KEY = "status";

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取action
            final String action = intent.getAction();
            if (HANDLE_EVENT_ACTION.equals(action)) {
                final boolean status = intent.getBooleanExtra(STATUS_KEY, false);
                if (status) {
                    setImageSrc(R.drawable.ic_happy);
                } else {
                    setImageSrc(R.drawable.ic_sad);
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //注册广播

        final IntentFilter filter = new IntentFilter(HANDLE_EVENT_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();

        //取消注册广播
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }
```

发送广播：

```java
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        //succ
                        //发送广播
                        final Intent intent = new Intent();
                        intent.setAction(MainActivity.HANDLE_EVENT_ACTION);
                        intent.putExtra(MainActivity.STATUS_KEY, true);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }
                        break;
                    case 1: {
                        //fail
                        final Intent intent = new Intent();
                        intent.setAction(MainActivity.HANDLE_EVENT_ACTION);
                        intent.putExtra(MainActivity.STATUS_KEY, false);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }
                        break;
                    default:
                        break;
                }
```



## 基本

![003](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/003.png)

![004](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/004.png)

主要是事件、订阅、发布

![005](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/005.png)

1.定义事件

```java
public class SuccesssEvent {
}
public class FailureEvent {
}
```

2.订阅

如下的代码，在`MainActivity`中订阅

```java
    @Override
    protected void onStart() {
        super.onStart();
        //注册
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

    //事件
    @Subscribe
    public void onSuccessEvent(SuccesssEvent event) {
        setImageSrc(R.drawable.ic_happy);
    }

    @Subscribe
    public void onFailureEvent(FailureEvent event) {
        setImageSrc(R.drawable.ic_sad);
    }
```

> 通过`@Subscribe`注解写回调函数

3.发布事件

```java
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        //succ
                        EventBus.getDefault().post(new SuccesssEvent());
                    }
                        break;
                    case 1: {
                        //fail
                        EventBus.getDefault().post(new FailureEvent());
                    }
                        break;
                    default:
                        break;
                }
```



## 线程

### ThreadMode

通过指定线程模式，可以指定回调函数运行于哪个线程

+ `POSTING` - 在哪个线程发布的就在哪个线程执行

  > Subscribers will be called in the same thread posting the event. This is the default. Event delivery is done synchronously and all subscribers will have been called once the posting is done. This ThreadMode implies the least overhead because it avoids thread switching completely. Thus this is the recommended mode for simple tasks that are known to complete is a very short time without requiring the main thread. Event handlers using this mode should return quickly to avoid blocking the posting thread, which may be the main thread. Example:
  >
  > ```java
  > // Called in the same thread (default)
  > // ThreadMode is optional here
  > @Subscribe(threadMode = ThreadMode.POSTING)
  > public void onMessage(MessageEvent event) {
  >     log(event.message);
  > }
  > ```



+ `MAIN` - 订阅回调函数一定运行于主线程

  > Subscribers will be called in Android’s main thread (sometimes referred to as UI thread). If the posting thread is the main thread, event handler methods will be called directly (synchronously like described for ThreadMode.POSTING). Event handlers using this mode must return quickly to avoid blocking the main thread. Example:
  >
  > ```java
  > // Called in Android UI's main thread
  > @Subscribe(threadMode = ThreadMode.MAIN)
  > public void onMessage(MessageEvent event) {
  >     textField.setText(event.message);
  > }
  > ```



+ `MAIN_ORDERED` - 

  > Subscribers will be called in Android’s main thread. The event is always enqueued for later delivery to subscribers, so the call to post will return immediately. This gives event processing a stricter and more consistent order (thus the name MAIN_ORDERED). For example if you post another event in an event handler with MAIN thread mode, the second event handler will finish before the first one (because it is called synchronously – compare it to a method call). With MAIN_ORDERED, the first event handler will finish, and then the second one will be invoked at a later point in time (as soon as the main thread has capacity).
  >
  > Event handlers using this mode must return quickly to avoid blocking the main thread. Example:
  >
  > ```java
  > // Called in Android UI's main thread
  > @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
  > public void onMessage(MessageEvent event) {
  >     textField.setText(event.message);
  > }
  > ```



+ `BACKGROUND` - 后台线程

  > Subscribers will be called in a background thread. If posting thread is not the main thread, event handler methods will be called directly in the posting thread. If the posting thread is the main thread, EventBus uses a single background thread that will deliver all its events sequentially. Event handlers using this mode should try to return quickly to avoid blocking the background thread.
  >
  > ```java
  > // Called in the background thread
  > @Subscribe(threadMode = ThreadMode.BACKGROUND)
  > public void onMessage(MessageEvent event){
  >     saveToDisk(event.message);
  > }
  > ```



+ `ASYNC` - 一定运行于一个独立的新开的线程上

  > Event handler methods are called in a separate thread. This is always independent from the posting thread and the main thread. Posting events never wait for event handler methods using this mode. Event handler methods should use this mode if their execution might take some time, e.g. for network access. Avoid triggering a large number of long running asynchronous handler methods at the same time to limit the number of concurrent threads. EventBus uses a thread pool to efficiently reuse threads from completed asynchronous event handler notifications.
  >
  > ```java
  > // Called in a separate thread
  > @Subscribe(threadMode = ThreadMode.ASYNC)
  > public void onMessage(MessageEvent event){
  >     backend.send(event.message);
  > }
  > ```
  >
  > 



#### `POSTING`

如下开启一个线程发送事件：

```java
                    case 2: {
                        //启动一个线程发送事件
                        new Thread("posting-002") {
                            @Override
                            public void run() {
                                super.run();
                                EventBus.getDefault().post(new PostingEvent(Thread.currentThread().toString()));
                            }
                        }.start();

                    }
```

订阅者更新UI:

```java
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onPostingEvent(PostingEvent event) {
        setPublisherThreadInfo(event.threadInfo);
        setSubscriberThreadInfo(Thread.currentThread().toString());
    }

    private void setPublisherThreadInfo(String threadInfo) {
        setTextView(R.id.publisherThreadTextView, threadInfo);
    }

    private void setSubscriberThreadInfo(String threadInfo) {
        setTextView(R.id.subscriberThreadTextView, threadInfo);
    }

    //应该在主线程中运行
    private void setTextView(int resId, String text) {
        final TextView textView = (TextView) findViewById(resId);
        textView.setText(text);
        textView.setAlpha(0.5f);
        textView.animate().alpha(1).start(); //动画
    }

```

会提示如下的错误：

![006](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/006.png)



可以将订阅者中的处理Event的方法修改为如下的形式：

```java
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onPostingEvent(PostingEvent event) {
        final String threadInfo = Thread.currentThread().toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPublisherThreadInfo(event.threadInfo);
                setSubscriberThreadInfo(threadInfo);
            }
        });

    }
```



#### `MAIN_ORDERED`

`MAIN_ORDERED`与`MAIN`的区别

先使用`MAIN`

```java
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainOrderEvent(MainOrderEvent event) {
        Log.d(TAG, "onMainOrderedEvent: enter @" + SystemClock.uptimeMillis()); //开机时间
        setPublisherThreadInfo(event.threadInfo);
        setSubscriberThreadInfo(Thread.currentThread().toString());
        Log.d(TAG, "onMainOrderedEvent: exit @" + SystemClock.uptimeMillis()); //开机时间
    }  
```

```java
Log.d(TAG, "onClick: before @" + SystemClock.uptimeMillis());
EventBus.getDefault().post(new MainOrderEvent(Thread.currentThread().toString()));
try {
    TimeUnit.SECONDS.sleep(1); //休眠1s
} catch (InterruptedException e) {
    e.printStackTrace();
}
Log.d(TAG, "onClick: after @" + SystemClock.uptimeMillis());
```

此时控制的输出顺序为：

```tex
2021-02-24 15:31:24.782 22892-22892/com.example.eventbus D/PublisherDialogFragment: onClick: before @279388204
2021-02-24 15:31:24.783 22892-22892/com.example.eventbus D/MainActivity: onMainOrderedEvent: enter @279388205
2021-02-24 15:31:24.784 22892-22892/com.example.eventbus D/MainActivity: onMainOrderedEvent: exit @279388206
2021-02-24 15:31:25.786 22892-22892/com.example.eventbus D/PublisherDialogFragment: onClick: after @279389208
```

> 可见，事件的发布方被事件的订阅方阻塞了

如果`threadMode`为`MAIN_ORDERED`，输出为：

```tex
2021-02-24 15:35:04.195 23019-23019/com.example.eventbus D/PublisherDialogFragment: onClick: before @279607617
2021-02-24 15:35:05.196 23019-23019/com.example.eventbus D/PublisherDialogFragment: onClick: after @279608618

2021-02-24 15:35:05.219 23019-23019/com.example.eventbus D/MainActivity: onMainOrderedEvent: enter @279608641
2021-02-24 15:35:05.220 23019-23019/com.example.eventbus D/MainActivity: onMainOrderedEvent: exit @279608642
```



## Sticky Events

有时候，需要先发布，再订阅事件

![008](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/008.png)

例如，一个Activity打开另一个Activity，在打开另一个Activity之前发送消息，然后再被打开的Activity中获取消息

```java
//启动另一个Activity
Intent intent = new Intent(this, StickyActivity.class);
//先发布
EventBus.getDefault().postSticky(new StickyMessageEvent("stikcy message"));
startActivity(intent);
```

获取消息：

```java
    @Subscribe(sticky = true)
    public void onStickyMessageEvent(StickyMessageEvent event) {
        setTitle(event.message);
    }
```

> 注意`@Subscribe(sticky = true)`



## 配置

在构建apk之前，需要定义混淆规则，目的是保证订阅回调函数在构建apk的过程中，不会被误删

参考：

+ [r8-proguard](https://github.com/greenrobot/EventBus#r8-proguard)

在`proguard-rules.pro`中，添加如下的规则：

```java
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
 
# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
```



























