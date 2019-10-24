# Activity

应用的所有`activity`都必须在`manifest`配置文件中声明，这样操作系统才能够找到它们。如果没有声明，则抛出`ActivityNotFoundException`异常

## 启动Activity

最简单的方法

```java
public void startActivity(Intent intent)
```

`intent`对象是`component`用来与操作系统通信的一种媒介工具

`component`包括`activity`、`service`、`broadcast receiver`以及`content provider`

Intent构造方法

```java
public Intent(Context packageContext, Class<?> cls)
```

通过Intent还可以传递数据给activity，如

```java
intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
```

然后可以在`onCreate`方法中取出对应的值：

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_cheat);
	mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
}
```



**从子 activity 获取返回结果** 

另一种情况是需要从子activity获取返回信息

```java
public void startActivityForResult(Intent intent, int requestCode)
```

子activity发送信息一个父activity，可以通过如下的方法:

```java
public final void setResult(int resultCode)
public final void setResult(int resultCode, Intent data)
```

`resultCode`值如下：

+ `Activity.RESULT_OK`
+ `Activity.RESULT_CANCELED`

> 子activity可以不调用`setResult(...)`方法。……在没有调用setResult(...)方法的情况下， 如果用户按了后退按钮，父activity则会收到`Activity.RESULT_CANCELED`的结果代码 



用户在按返回键，回到父activity的时候，会调用父activity如下的方法:

```java
protected void onActivityResult(int requestCode, int resultCode, Intent data)
```

+ requestCode请求码，判断来源
+ resultCode判断结构
+ data数据

如下的例子:

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }

    }
```



## 使用menu

1.`res`目录下新建一个`menu`文件夹

2.重写`onCreateOptionsMenu(Menu menu)`方法

3.重写`onOptionsItemSelected(MenuItem item)`方法



## activity管理

### launcher activity

在`AndroidManifest.xml`中配置

```xml
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
```



### 销毁activity

`finish()`方法可以销毁activity



### 回退栈

>ActivityManager维护着一个非特定应用独享的回退栈。所有应用的activity都共享该回退栈。这也是将ActivityManager设计成操作系统级的activity管理器来负责启动应用activity的原因之一。显然，回退栈是作为一个整体共享于操作系统及设备，而不单单用于某个应用

`finish()`方法可以将activity从栈中弹出



## 启动模型

启动模式一共4种，在`<activity>`标签上通过`android:launchMode`来指定启动模式

+ standard - 默认，每次启动都会创建该活动的一个新的实例
+ singleTop - 在启动活动时，如果发现返回栈的栈顶已经是该活动，则直接使用它，不会创建新的活动实例。如果不处于栈顶，启动时，还是会创建新的实例
+ singleTask - 启动活动时会在返回栈中检查是否存在该活动的实例，如果存在，就直接使用，并把该活动之上的所有活动统统出栈，如果没有发现就会创建一个新的活动实例
+ singleInstance - 启用一个新的返回栈来管理活动。在这种模式下会有一个单独的返回栈来管理这个活动，不管是哪个应用程序来范文这个活动，都共用同一个返回栈



## 最佳实践

1.当前在哪一个activity

创建BaseActivity，其它activity继承自BaseActivity

```java
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("BaseActivity", getClass().getSimpleName());

        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("BaseActivity", "onDestroy");

        ActivityController.removeActivity(this);

    }
}
```



2.随时退出程序

创建一个专门的集合类对所有的activity进行管理

```java
public class ActivityController {


    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
```



3.启动活动

例如启动SecondActivity，需要用来2个字符串参数，在启动时需要传递过来，通常我们会这样写

```java
Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
intent.putExtra("param1", "data1");
intent.putExtra("param2", "data2");
startActivity(intent);
```

这样写，如果SecondActivity是别人开发的，你就不知道需要传递那些参数

可以修改`SecondActivity`，添加如下的静态方法

```java
    public static void actionStart(Context context, String data1, String data2) {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);
    }
```

启动时

```java
SecondActivity.actionStart(FirstActivity.this, "data1", "data2");
```

