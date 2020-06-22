# EditText

参考：

+ [Android之自定义EditText光标和下划线颜色](https://segmentfault.com/a/1190000009507919)

默认的EditText在我的手机显示效果如下：

![001](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/001.png)

在实际的项目中，可能会有如下的需求：



**1.取消底部的线**

参考：

+ [EditText去掉下划线](https://www.jianshu.com/p/977918ebc207)

这里使用透明背景，定义背景为透明色：

```xml
android:background="@color/transparent"
```

颜色定义如下：

```xml
<color name="transparent">#00000000</color>
```

![002](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/002.png)

