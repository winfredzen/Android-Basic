# XML Drawable

在Android世界里，凡是要在屏幕上绘制的东西都可以叫作drawable，比如抽象图形、Drawable类的子类代码、位图图像等

## Shape drawable

使用ShapeDrawable，可以把按钮变成圆.XML drawable和屏幕像素密度无关，所以无需考虑创建特定像素密度目录，直接把它放入默认的drawable文件夹就可以了

`button_beat_box_normal.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">

    <solid android:color="@color/dark_blue" />

</shape>
```

修改按钮背景

![032](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/032.png)

这样修改之后，按钮的背景就变成了圆形

![033](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/033.png)



## StateListDrawable

[StateListDrawable](https://developer.android.com/reference/android/graphics/drawable/StateListDrawable)支持为不同的状态，指定不同的drawable，如下，当button按下时，设置不同的drawable

`button_beat_box_pressed.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">
    <solid android:color="@color/red" />
</shape>
```

`button_beat_box.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/button_beat_box_pressed" android:state_pressed="true" />
    <item android:drawable="@drawable/button_beat_box_normal" />
</selector>
```

![034](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/034.gif)











