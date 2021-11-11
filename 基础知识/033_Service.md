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



## 启动服务&绑定服务的区别与联系

首先要明白的一点是`startService()`和`bindService()`是可以一起调用的

> 服务生命周期（从创建到销毁）可遵循以下任一路径：
>
> - 启动服务
>
>   该服务在其他组件调用 `startService()` 时创建，然后无限期运行，且必须通过调用 `stopSelf()` 来自行停止运行。此外，其他组件也可通过调用 `stopService()` 来停止此服务。服务停止后，系统会将其销毁。
>
> - 绑定服务
>
>   该服务在其他组件（客户端）调用 `bindService()` 时创建。然后，客户端通过 `IBinder` 接口与服务进行通信。客户端可通过调用 `unbindService()` 关闭连接。多个客户端可以绑定到相同服务，而且当所有绑定全部取消后，系统即会销毁该服务。（服务*不必*自行停止运行。）
>
> 这两条路径并非完全独立。您可以绑定到已使用 `startService()` 启动的服务。例如，您可以使用 `Intent`（标识要播放的音乐）来调用 `startService()`，从而启动后台音乐服务。随后，当用户需稍加控制播放器或获取有关当前所播放歌曲的信息时，Activity 可通过调用 `bindService()` 绑定到服务。此类情况下，在所有客户端取消绑定之前，`stopService()` 或 `stopSelf()` 实际不会停止服务。



>  在启动方式中,启动的Service的组件不能获取Service对象的实例,因此无法调用Service中的任何函数,也不能够获取Service中的任何状态和数据信息.能够以启动方式使用的Service,需要具备自管理的能力,而且不需要通过函数调用获取Service的功能和数据.
>
> 在绑定方式中,Service是通过服务连接实现的,服务连接可以获取Service的对象实例,可以直接调用Service中的实现的函数,或者直接获得Service中的状态和数据信息,并且同一个Service可以绑定多个服务连接,可以同时为多个不同的组件提供服务.



> 启动服务：一旦服务开启跟调用者(开启者)就没有任何关系了。开启者退出了，开启者挂了，服务还在后台长期的运行。开启者**不能调用**服务里面的方法。
>
> 绑定服务：bind的方式开启服务，绑定服务，调用者挂了，服务也会跟着挂掉。绑定者可以调用服务里面的方法。



> APP关闭后不希望Service被系统回收，会有通知栏显示，点击可以进入APP，此时可以使用**前台服务**































