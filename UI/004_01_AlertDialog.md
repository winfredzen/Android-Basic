# AlertDialog

`AlertDialog`的继承关系如下：

![030](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/030.png)

先参考[Android Custom Dialog Example – Making Custom AlertDialog](https://www.simplifiedcoding.net/android-custom-dialog-example/)中例子

1.dialog的布局为`my_dialog.xml`

![031](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/031.png)

2.在Activity中显示个dialog

```java
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
```

效果如下：

![032](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/032.png)

> 其实我有个疑问？
>
> 1.在布局中我设置的大小都是`match_parent`，但实际显示的时候，类似于`wrap_content`，这个大小是怎么确定的呢？



**修改Dialog的大小**

例如修改宽度和高度为屏幕尺寸的一半

```java
        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay(); //获取屏幕宽高
        Point point = new Point();
        display.getSize(point);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = (int) (point.x * 0.5); //宽度设置为屏幕宽度的0.5
        layoutParams.height = (int) (point.y * 0.5); //高度设置为屏幕高度的0.5
        window.setAttributes(layoutParams);
```

此时的显示效果如下：

![033](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/033.png)



**修改显示的位置**

比如要在底部显示，此时如果设置宽度为`MATCH_PARENT`，高度为`WRAP_CONTENT`

```java
 				WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay(); //获取屏幕宽高
        Point point = new Point();
        display.getSize(point);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
```

![034](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/034.png)

可见，左、右、底部都有间隙，还不是真正的在底部

需要设置

```java
alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);    // 如果不设置，则四周会有空隙
```

![035](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/035.png)

在上面的基础上，还可以设置便宜

```java
layoutParams.y = 200;
```

![036](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/036.png)



**动画**

另外还可以通过`window.setWindowAnimations`来设置动画

参考：

+ [自定义底部 Dialog——宽度充满屏幕](https://github.com/Bakumon/blog/issues/3)
+ [Android实现底部对话框BottomDialog](https://www.jianshu.com/p/e1d2cc82e756)



定义`src/main/res/anim/bottom_up_in.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="@android:integer/config_mediumAnimTime"
        android:fromYDelta="100%p"
        android:toYDelta="0"/>
    <alpha
        android:duration="@android:integer/config_mediumAnimTime"
        android:fromAlpha="0.95"
        android:toAlpha="1"/>
</set>
```

定义`src/main/res/anim/bottom_down_out.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="@android:integer/config_mediumAnimTime"
        android:fromYDelta="0"
        android:toYDelta="100%p" />
    <alpha
        android:duration="@android:integer/config_mediumAnimTime"
        android:fromAlpha="1"
        android:toAlpha="0.95" />
</set>
```

定义动画：

```xml
    <!-- 底部 dialog 进入离开动画 -->
    <style name="Animation.Bottom" parent="@android:style/Animation">
        <item name="android:windowEnterAnimation">@anim/bottom_up_in</item>
        <item name="android:windowExitAnimation">@anim/bottom_down_out</item>
    </style>
```

然后通过如下的代码设置动画：

```java
Window window = alertDialog.getWindow();
window.setWindowAnimations(R.style.Animation_Bottom);
```

最后的效果如下：

![037](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/037.gif)











































