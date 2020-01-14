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

## layer list drawable

layer list drawable能让两个XML drawable合二为一

如下，定义一个深色的圆环，在按钮按下时显示一个圆环

```xml
<?xml version="1.0" encoding="utf-8"?>

<layer-list xmlns:android="http://schemas.android.com/apk/res/android">

    <item>
        <shape android:shape="oval">
            <solid android:color="@color/red" />
        </shape>
    </item>

    <item>
        <shape android:shape="oval">
            <stroke android:width="4dp" android:color="@color/dark_red"/>
        </shape>
    </item>

</layer-list>

```

![035](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/035.gif)



## 使用mipmap图像

把应用启动器图标放在mipmap目录中，其他图片都放在drawable 目录中



## 9-patch图像

参考：

+ [创建可调整大小的位图（9-Patch 文件）](https://developer.android.com/studio/write/draw9patch?hl=zh-cn)
+ [NinePatch图（9-Patch图，.9图）使用全面介绍](http://www.paincker.com/nine-patch-usage)

如下2张带有圆角和折角的图片，一张normal，一张pressed

![036](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/036.png)

![037](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/037.png)

如果直接将其设置为背景

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/ic_button_beat_box_pressed" android:state_pressed="true" />
    <item android:drawable="@drawable/ic_button_beat_box_default" />
</selector>
```

会发现图片被拉伸变形，效果如下：

![038](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/038.png)

为什么要叫作9-patch呢?9-patch图像分成3×3的网格，即由9部分或9 patch组成的网格。网格角落部分不会被缩放，边缘部分的4个patch只按一个维度缩放，而中间部分则按两个维度缩放

> 9-patch图像和普通PNG图像十分相似，只有两处不同:9-patch图像文件名以.9.png结尾，图像边缘具有1像素宽度的边框。这个边框用以指定9-patch图像的中间位置。边框像素绘制为黑线， 以表明中间位置，边缘部分则用透明色表示。

![039](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/039.png)

1.将图片转为9-patch图像

图片右键，选择**Create 9-patch file**

2.打开图片，编辑，为让图片更醒目，勾选上`Show patches`选项

> 在 1 像素周长的范围内点击，以绘制定义可拉伸图块和（可选）内容区域的线条。点击右键（在 Mac 上，在按住 Shift 键的同时点击）可以擦除之前绘制的线条。

> 左、上两边的黑色像素点，分别表示水平、垂直方向的缩放区域（stretchable area）。缩放区域**可以有多段**，缩放时会**按比例**进行缩放。
>
> 右、下两边的黑色像素点，分别表示水平、垂直方向的内容区域（padding lines）。内容区域**相当于设置Padding**，应该是**一段连续区域**，且9-Patch图的内容区域是**可选**的。

![040](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/040.png)

图片的顶部黑线指定了水平方向的可拉伸区域。左边的黑线标记在竖直方向哪些像素可以拉伸。

显示效果如下:

![041](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/041.png)

底部以及右边框定义 了9-patch图像的可选内容区。内容区是绘制内容(通常是文字)的地方。如果不标记内容区，那 么默认与可拉伸区域保持一致。

如下定义内容区域

![042](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/042.png)

添加内容区域后，显示效果如下

![043](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/043.png)



















