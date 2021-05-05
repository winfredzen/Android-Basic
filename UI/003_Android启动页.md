# Android启动页

参考：

+ [Android 启动 ( 欢迎 ) 页面实现](https://www.jianshu.com/p/fbdab718cd1d)

写启动页时遇到的一些小问题

1.启动页顶部有状态栏，不是全屏的

添加如下的代码来达到全屏状态

```java
getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
```



2.启动时白屏后，再显示启动页面

可在主题设置时，将`android:windowIsTranslucent`设置为透明

```xml
    <style name="AppTheme" parent="MainTheme">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>

        <item name="android:windowIsTranslucent">true</item>
    </style>
```

另外可以设置图片

+ [Android 欢迎页快速启动](https://ln0491.github.io/2016/11/07/Android-欢迎页快速启动/)



## 其它的方式

有一种使用`Launcher Theme`的方式，参考：

+ [The right way to implement a Splash Screen in Android](https://shishirthedev.medium.com/the-right-way-to-implement-a-splash-screen-in-android-acae0e52949a)
+ [How do I make a splash screen? ](https://stackoverflow.com/questions/5486789/how-do-i-make-a-splash-screen)

> 在**res/values/style.xml**创建**Style**
>
> ```xml
> <style name="SplashScreenTheme", parent = "Theme.AppCompat.NoActionBar">
>     <item          name="android:windowBackground">@drawable/launcher_screen_with_logo</item>
> </style>
> ```
>
> Set the style as theme for **SplashScreenActivity** in **AndroidManifest.xml**
>
> ```xml
> <activity android:name=".SplashScreenActivity"
>     android:theme="@style/SplashScreenTheme">
>     <intent-filter>
>         <action android:name="android.intent.action.MAIN" />
> <category android:name="android.intent.category.LAUNCHER" />
>     </intent-filter>
> </activity>
> ```
>
> 





























