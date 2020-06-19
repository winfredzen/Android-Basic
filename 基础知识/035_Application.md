# Application

参考：

+ [Android Context完全解析，你所不知道的Context的各种细节](https://blog.csdn.net/guolin_blog/article/details/47028975)
+ [Android：全面解析 Application类](https://juejin.im/entry/59c30e0ff265da06611f7024)



[Application](https://developer.android.com/reference/android/app/Application#onConfigurationChanged(android.content.res.Configuration))类，在应用程序启动的时候，系统会自动将这个类进行初始化

可创建子类，并在`AndroidManifest.xml`的`android:name`属性中指定这个类，如下的例子：

```java
public class MyApplication extends Application{
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getInstance() {
        return mContext;
    }
}
```

在AndroidManifest文件中指定自定义的Application

```xml
 <application
        android:name=".MyApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme" />
```



> **Note:** There is normally no need to subclass Application. In most situations, static singletons can provide the same functionality in a more modular way. If your singleton needs a global context (for example to register broadcast receivers), include `Context.getApplicationContext()` as a `Context` argument when invoking your singleton's `getInstance()` method.
>
> 注意：通常不需要子类化Application。 在大多数情况下，静态单例可以以更模块化的方式提供相同的功能。 如果您的单例需要全局上下文（例如，注册广播接收者），则在调用单例的`getInstance()`方法时将 `Context.getApplicationContext()` 作为`Context`参数。



















