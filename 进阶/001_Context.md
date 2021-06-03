# Context

记录对Context的理解，参考：

+ [Android Context完全解析，你所不知道的Context的各种细节](https://guolin.blog.csdn.net/article/details/47028975)



`Context`的继承结构

![003](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/003.png)



## Application Context

1.`getApplication()`与`getApplicationContext()`的区别与联系

如下的例子：

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication myApplication = (MyApplication) getApplication();
        Log.d(TAG, "getApplication is " + myApplication);

        Context appContext = getApplicationContext();
        Log.d(TAG, "getApplicationContext is " + appContext);

    }
```

输出结果为:

```java
2021-06-03 16:11:19.035 7821-7821/com.example.contextdemo D/MainActivity: getApplication is com.example.contextdemo.MyApplication@4072822
2021-06-03 16:11:19.035 7821-7821/com.example.contextdemo D/MainActivity: getApplicationContext is com.example.contextdemo.MyApplication@4072822
```

> 通过打印结果可知，它们是同一个对象，`Application`本身就是一个`Context`，所以这里获取`getApplicationContext()`得到的结果就是`MyApplication`本身的实例



**为什么提供2个返回同样结果的方法呢？**

> `getApplication()`方法，只能在`Activity`和Service中才能调用的到
>
> `getApplicationContext()`作用域更广一些，任何一个`Context`的实例，只要调用`getApplicationContext()`方法都可以拿到我们的`Application`对象。























