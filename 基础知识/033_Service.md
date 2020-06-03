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

当点击`STOP SERVICE`时，控制天输出如下：

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































