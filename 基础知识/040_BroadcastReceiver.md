# BroadcastReceiver

+ 标准广播 - 一种完全异步的广播，在广播发出之后，所有的广播接收器几乎可以同一时刻接收到这条广播消息，因此它们之间没有任何先后顺序可言
+ 有序广播 - 是一种同步执行的广播，在刚播发出之后，同一时刻只会有一个广播接收器能够接收到这条广播消息，当这个广播接收器中的逻辑执行完毕后，广播才会继续传递。所以此时的广播是有先后顺序的，优先级高的广播接收器就可以先收到广播消息，并且后面的广播接收器还可以截断正在传递的广播，这样后面的广播接收器就无法收到广播消息了



广播的机制

1. 消息订阅者订阅
2. 消息发布者发布广播
3. 在已注册列表中，寻找合适的消息订阅者，发送广播



![065](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/065.png)



广播本质是观察者模式

**广播的注册**

1.静态注册 - 8.0后静态注册有限制

在`AndroidManifest.xml`中注册

2.动态注册



**系统广播 & 应用广播**

Android内置了很多系统级别的广播，比如手机开机完成后会发出一条广播，电池电量发生变化会发出一条广播



## 静态注册系统广播

首先自定义一个`ImoocBroadcastReceiver`，继承自`BroadcastReceiver`

```java
public class ImoocBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "ImoocBroadcastReceiver";
    /*
    * 接收到广播要做什么
    * intent - 发过来的广播
    */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive:" + action);
        }
    }
}
```

然后在`AndroidManifest.xml`中注册

```xml
        <!--   静态注册广播    -->
        <receiver android:name=".ImoocBroadcastReceiver">
            <!--       接收哪些广播     -->
            <intent-filter>
                <!--     开机广播     -->
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <!--     电量低广播     -->
                <action android:name="android.intent.action.BATTERY_LOW" />
                <!--     卸载应用包广播     -->
                <action android:name="android.intent.action.PACKAGE_REMOVED" />
                <!--        卸载应用包广播需要添加如下的scheme        -->
                <data android:scheme="package"/>
            </intent-filter>
        </receiver>
```

**要注意的是：在Android8.0之后的版本，这种静态注册时没有效果的**，可参考：

+ [监听App安装、卸载的系统广播](https://blog.csdn.net/ezconn/article/details/116804530)



## 动态注册系统广播

动态注册无须在`AndroidManifest.xml`中注册`<receiver/>`组件

```java
public class MainActivity extends AppCompatActivity {

    private ImoocBroadcastReceiver imoocBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //新建一个广播接收器
        imoocBroadcastReceiver = new ImoocBroadcastReceiver();
        //要接收哪些广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);//ACTION_UNINSTALL_PACKAGE无效果
        intentFilter.addDataScheme("package");
        //向系统注册
        registerReceiver(imoocBroadcastReceiver, intentFilter)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册广播接收器
        if (imoocBroadcastReceiver != null) {
            unregisterReceiver(imoocBroadcastReceiver);
        }
    }

```

在某个应用被卸载时，会输出log

```java
2021-07-07 17:11:52.240 6590-6590/com.example.broadcastdemo D/ImoocBroadcastReceiver: onReceive:android.intent.action.PACKAGE_REMOVED
```



**广播的生命周期**

`onReceive`方法是主线程中执行的，不能做耗时操作

> This method is called when the BroadcastReceiver is receiving an Intent broadcast. During this time you can use the other methods on BroadcastReceiver to view/modify the current result values. This method is always called within the main thread of its process, unless you explicitly asked for it to be scheduled on a different thread using `Context.registerReceiver(BroadcastReceiver, IntentFilter, String, android.os.Handler)`. When it runs on the main thread you should never perform long-running operations in it (there is a timeout of 10 seconds that the system allows before considering the receiver to be blocked and a candidate to be killed). You cannot launch a popup dialog in your implementation of onReceive().
>
> **If this BroadcastReceiver was launched through a <receiver> tag, then the object is no longer alive after returning from this function.** This means you should not perform any operations that return a result to you asynchronously. If you need to perform any follow up background work, schedule a `JobService` with `JobScheduler`. If you wish to interact with a service that is already running and previously bound using `bindService()`, you can use `peekService(Context, Intent)`.
>
> The Intent filters used in `Context.registerReceiver(BroadcastReceiver, IntentFilter)` and in application manifests are *not* guaranteed to be exclusive. They are hints to the operating system about how to find suitable recipients. It is possible for senders to force delivery to specific recipients, bypassing filter resolution. For this reason, `onReceive()` implementations should respond only to known actions, ignoring any unexpected Intents that they may receive.



## 自定义广播

### App内广播

即在App内发送广播，然后App内接收广播

```java
public static final String MY_ACTION = "com.wz.broadcast.custom";
public static final String BROADCAST_CONTENT = "broadcast_content";


sendBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //新建广播
        Intent intent = new Intent(MY_ACTION);
        //广播数据
        intent.putExtra(BROADCAST_CONTENT, editText.getText().toString());
        //发送广播
        sendBroadcast(intent);
    }
});
//新建一个广播接收器
imoocBroadcastReceiver = new ImoocBroadcastReceiver();
//要接收哪些广播
IntentFilter intentFilter = new IntentFilter();
intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);//ACTION_UNINSTALL_PACKAGE无效果
//intentFilter.addDataScheme("package");
//自定义Action
intentFilter.addAction(MY_ACTION);
//向系统注册
registerReceiver(imoocBroadcastReceiver, intentFilter);
```

处理接收到广播消息

```java
public class ImoocBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "ImoocBroadcastReceiver";
    /*
    * 接收到广播要做什么
    * intent - 发过来的广播
    */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive:" + action);

            if (action.equals(MainActivity.MY_ACTION)) {
                //自定义的广播，获取自定义的数据
                String content = intent.getStringExtra(MainActivity.BROADCAST_CONTENT);
                Log.d(TAG, "content:" + content);
            }
        }
    }
}
```



### App间广播

即一个应用发送广播，一个应用接收广播，使用上与上面App内的广播没有区别



## 发送有序广播

`sendOrderedBroadcast`可发送有序广播

`abortBroadcast()`可以将广播截断，后面的广播接收器将无法再接收到这条广播



## 本地广播

前面发送和接收的广播全部属于系统全局广播，即发出的广播可以被其他任何应用程序接收到，并且我们也可以接收到来自于其他任何应用程序的广播，这样就有安全方面的问题。

为了解决安全方面的问题，Android引入了一套本地广播机制，使用这个机制发出的广播只能够在应用程序的内部进行传递

使用`LocalBroadcastManager`





















