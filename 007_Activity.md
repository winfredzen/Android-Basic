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

通过Intent还可以传递数据给activity

```java
intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
```



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