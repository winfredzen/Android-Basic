# Android编程权威指南笔记一

**R.java文件**

可在目录`app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/`下，项目包名称中找到`R.java`文件。`R.java`文件在Android项目编译过程中自动生成



**快捷键Option+Return**

使用`Option+Return`(`Alt+Enter`)组合键，让Android Studio自动为你导入



**资源**

+ 资源是应用非代码形式的内容，如图像文件、音频文件以及XML文件等
+ 项目的所有资源文件都存放在目录`app/res`的子目录下
+ 布局也是一种资源
+ 资源id为int型



**配置Android Studio识别成员变量的m前缀**

Mac下，选择`Preferences`->`Editor`->`Code Style`->`Java` 选择Code Generation

在Naming表单的Field一行中，添加m作为前缀

![007](https://github.com/winfredzen/Android-Basic/blob/master/images/007.png)

设置后，需要Android Studio为`mTextResId`(int)生成获取方法时，它生成的是`getTextResId()`而不是`getMTextResId()`;而在为`mAnswerTrue`(boolean)生成获取方法时，生成的是`isAnswerTrue()`而不是`isMAnswerTrue()`。



**this的使用**

在Button的事件中，如果要使用Toast，注意this的区别，这里的this指的是监听器`View.OnClickListener`



**在设备上运行**

1.连接设备，Mac可以理解识别设备，如果在Windows上，则可能需要安装adb(Android Debug Bridge)

2.如果开发者选项不可见，在"设置-关于手机/平板"，点击版本号(Build Number)7次启用

3.找到开发者选项，勾选"USB调试"



**图标资源**

drawable-mdpi、drawable-hdpi、drawable-xhdpi和drawable-xxhdpi四个目录

+ mdpi:中等像素密度屏幕(约160dpi)
+ hdpi:高像素密度屏幕(约240dpi)
+ xhdpi:超高像素密度屏幕(约320dpi)
+ xxhdpi:超超高像素密度屏幕(约480dpi)

任何添加到res/drawable目录中、后缀为.png .jpg或者.gif的文件都会自动获得资源ID(注意，文件名必须是小写字母且不能有任何空格符号)



## activity的生命周期

每个Activity都有其生命周期，在其生命周期类，activity在运行、暂停、停止和不存在这四种状态键转换

![008](https://github.com/winfredzen/Android-Basic/blob/master/images/008.png)

| 状态   | 有内存实例 | 用户可见 | 处于前台 |
| ------ | ---------- | -------- | -------- |
| 不存在 | 否         | 否       | 否       |
| 停止   | 是         | 否       | 否       |
| 暂停   | 是         | 是或部分 | 否       |
| 运行   | 是         | 是       | 是       |

![010](https://github.com/winfredzen/Android-Basic/blob/master/images/010.png)

应用启动并创建Activity初始实例后：

+ onCreate()
+ onStart()
+ onResume()

Activity处于运行状态(在内存里，用户可见，活动在前台)

点击后退键

+ onPause()
+ onStop()
+ onDestroy()

Activity处于不存在状态(不在内存里，显然不可见，不会活动在前台)

如果启动后，再点击主屏幕键

+ onPause()
+ onStop() 
+ onSaveInstanceState

Activity已处于停止状态(在内存中，但不可见，不会活动在前台)

然后再点击最近应用键

+ onStart()
+ onResume()

可发现，系统没有调用`onCreate()`



如果启动后，点击最近应用键，调用

+ onPause()
+ onStop()
+ onSaveInstanceState

如果此时，选中应用，调用

+ onStart()
+ onResume()



一个activity A打开另一个activity B

如果被打开的B是全屏的，非透明的，A调用

+ onPause()
+ onStop()

按Back键返回A，A调用

+ onRestart()
+ onStart()
+ onResume()



如果B是非全屏，例如一个dialog，A调用

+ onPause()

按Back键返回A，A调用

+ onResume()



-----

**创建过滤filter**

选择`Edit Filter Configuration`选项

![009](https://github.com/winfredzen/Android-Basic/blob/master/images/009.png)



### 设备旋转与 activity 生命周期

当旋转设备后，调用过程如下：

+ onPause()
+ onStop()
+ onSaveInstanceState
+ onDestroy()
+ onCreate()
+ onStart()
+ onResume()

设备旋转时，系统会销毁Activity的实例，然后创建一个新的Activity的实例。

**创建水平模式布局**

res目录上右键，选择`New->Android resource directory`菜单项，Resource type选择`layout`，Source set保持main不变

![011](https://github.com/winfredzen/Android-Basic/blob/master/images/011.png)

选择Orientation，点击`>>`按钮

![012](https://github.com/winfredzen/Android-Basic/blob/master/images/012.png)

Android Stuido会创建`res/layout-land`目录

设备处于水平方向时，Android会找到并使用`res/layout-land`目录下的布局资源。其他情况下，
它会默认使用`res/layout`目录下的布局资源

然后将布局文件copy过来，注意2个布局文件的文件名必须相同



#### 保存数据以应对设备旋转

重写`onSaveInstanceState(Bundle outState)`方法，将数据保存在bundle中

```java
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }
```

注意：在Bundle中存储和恢复的数据类型只能是基本类型(`primitive type`)以及可以实现`Serializable`或`Parcelable`接口的对象

然后可以在`onCreate()`取出数据

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate() called");

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
      	......
	}
```







完整的activity生命周期

![013](https://github.com/winfredzen/Android-Basic/blob/master/images/013.png)



**测试相关**

`Developer options`中有一项`Don’t keep activities`，启用后，点击主屏幕键会暂停并停止当前的activity



## Android应用调试

### 异常断点

选择`Run → View Breakpoints`，点击`+`号，选择`Java Exception Breakpoints`选项

输入`RuntimeException`，按提示选择`RuntimeException` (java.lang)。`RuntimeException`是`NullPointerException`、`ClassCastException`及其他常见异常的超类，因此该设置基本适用于所有异常 

![014](https://github.com/winfredzen/Android-Basic/blob/master/images/014.png)

如果出错会直接跳转到出错点

![015](https://github.com/winfredzen/Android-Basic/blob/master/images/015.png)



### Android Lint

`Android Lint`是Android应用代码的静态分析器(static analyzer)。作为一个特殊程序，它能在不运行代码的情况下检查代码错误。凭着对Android框架的熟练掌握，Android Lint能深入检查代码，找出编译器无法发现的问题。在大多数情况下，Android Lint检查出的问题都值得重视

选择`Analyze → Inspect Code`



