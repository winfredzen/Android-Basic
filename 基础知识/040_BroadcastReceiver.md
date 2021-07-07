# BroadcastReceiver

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



系统广播 & 应用广播



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



































