# Service

定义一个MyService类，继承自`Service`，如下：

```java
public class MyService extends Service {

    private static final String TAG = "MyService";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand executed");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy executed");
    }
}
```



在Activity中启动服务和停止服务，如下的`MainActivity`

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startService = (Button) findViewById(R.id.start_service);
        Button stopService = (Button) findViewById(R.id.stop_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);//启动服务
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);//停止服务
                break;
            default:
                break;
        }
    }
}
```

界面效果如下：

![052](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/052.png)

当点击`START SERVICE`时，控制台输出如下：

```xml
06-03 16:53:13.474 31948-31948/com.example.servicetest D/MyService: onCreate executed
06-03 16:53:13.474 31948-31948/com.example.servicetest D/MyService: onStartCommand executed
```

当点击`STOP SERVICE`时，控制台输出如下：

```xml
06-03 16:53:53.544 31948-31948/com.example.servicetest D/MyService: onDestroy executed
```

如果多次点击`START SERVICE`，只会输出

```xml
06-03 16:55:20.199 31948-31948/com.example.servicetest D/MyService: onStartCommand executed
```

表示`onStartCommand`方法被调用



通常可在Settings->Developer options->Running services中找到**正在运行的服务**

![053](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/053.png)





## 活动和服务进行通信

参考：

+ [创建绑定服务](https://developer.android.com/guide/components/services#CreatingBoundService)

> 绑定服务允许应用组件通过调用 `bindService()` 与其绑定，从而创建长期连接。此服务通常不允许组件通过调用 `startService()` 来*启动*它。
>
> 如需与 Activity 和其他应用组件中的服务进行交互，或需要通过进程间通信 (IPC) 向其他应用公开某些应用功能，则应创建绑定服务。
>
> 如要创建绑定服务，您需通过实现 `onBind()` 回调方法返回 `IBinder`，从而定义与服务进行通信的接口。然后，其他应用组件可通过调用 `bindService()` 来检索该接口，并开始调用与服务相关的方法。服务只用于与其绑定的应用组件，因此若没有组件与该服务绑定，则系统会销毁该服务。您*不必*像通过 `onStartCommand()` 启动的服务那样，以相同方式停止绑定服务。
>
> 如要创建绑定服务，您必须定义指定客户端如何与服务进行通信的接口。服务与客户端之间的这个接口必须是 `IBinder` 的实现，并且服务必须从 `onBind()` 回调方法返回该接口。收到 `IBinder` 后，客户端便可开始通过该接口与服务进行交互。
>
> 多个客户端可以同时绑定到服务。完成与服务的交互后，客户端会通过调用 `unbindService()` 来取消绑定。如果没有绑定到服务的客户端，则系统会销毁该服务。

这就需要借助`onBind(Intent intent)` 方法了

例如下面的例子，在`MyService`里提供了一个下载功能，在活动中可以决定何时开始下载，以及随时查看下载的进度。实现这个功能的思路是创建一个专门的`Binder`对象来对下载功能进行管理

`bindService`方法有三个参数

```
bindService(Intent service, ServiceConnection conn,int flags)
```

+ service - 服务
+ conn - ServiceConnection类型
+ flags - 绑定的操作选项，`BIND_AUTO_CREATE`表示在活动和服务进行绑定后自动创建服务，这使得`MyService`中的`onCreate()`方法得到执行，但`onStartCommand()`方法不会执行



`MyService`类

```java
public class MyService extends Service {

    private static final String TAG = "MyService";

    private DownloadBinder mBinder = new DownloadBinder();

    //模拟下载
    class DownloadBinder extends Binder {
        public void startDownload() {
            Log.d(TAG, "startDownload executed");
        }

        public int getProgress() {
            Log.d(TAG, "getProgress executed");
            return 0;
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand executed");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy executed");
    }
}

```

`MainActivity`类

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyService.DownloadBinder mDownloadBinder;

    //ServiceConnection匿名类
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {//活动与服务成功绑定调用
            mDownloadBinder = (MyService.DownloadBinder)service;
            //调用方法
            mDownloadBinder.startDownload();
            mDownloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {//活动与服务的连接断开时调用

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startService = (Button) findViewById(R.id.start_service);
        Button stopService = (Button) findViewById(R.id.stop_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);

        Button bindService = (Button) findViewById(R.id.bind_service);
        Button unbindService = (Button) findViewById(R.id.unbind_service);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);//启动服务
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);//停止服务
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, mConnection, BIND_AUTO_CREATE);//绑定服务
                break;
            case R.id.unbind_service:
                unbindService(mConnection);//解绑服务
                break;
            default:
                break;
        }
    }
}
```

界面如下：

![054](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/054.png)



点击下`BIND SERVICE`按钮，控制台输出如下：

```xml
06-03 18:02:33.722 1486-1486/com.example.servicetest D/MyService: onCreate executed
06-03 18:02:33.726 1486-1486/com.example.servicetest D/MyService: startDownload executed
06-03 18:02:33.727 1486-1486/com.example.servicetest D/MyService: getProgress executed
```

说明在活动中已近成功调用了服务里提供的方法了

点击下`UNBIND SERVICE`按钮，控制台输出如下：

```xml
06-03 18:03:32.299 1486-1486/com.example.servicetest D/MyService: onDestroy executed
```









