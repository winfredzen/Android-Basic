# DisplayMetrics

[DisplayMetrics](https://developer.android.com/reference/kotlin/android/util/DisplayMetrics)描述有关显示器的一般信息的结构，例如其大小，密度和字体缩放

通常使用如下的形式获取

```java
context.getResources().getDisplayMetrics();
```

已Mi 10 Lite为例，

> 分辨率 2400 x 1080 FHD+

获取如下的数据：

```java
DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
Log.d(TAG, "widthPixels: " + displayMetrics.widthPixels);
Log.d(TAG, "heightPixels: " + displayMetrics.heightPixels);
Log.d(TAG, "densityDpi: " + displayMetrics.densityDpi);
Log.d(TAG, "density: " + displayMetrics.density);
Log.d(TAG, "scaledDensity: " + displayMetrics.scaledDensity);
Log.d(TAG, "xdpi: " + displayMetrics.xdpi);
Log.d(TAG, "ydpi: " + displayMetrics.ydpi);
Log.d(TAG, "DisplayMetrics.DENSITY_DEVICE_STABLE: " + DisplayMetrics.DENSITY_DEVICE_STABLE);
```

输出结果为：

```java
2021-03-23 09:34:15.434 28100-28100/com.flyco.tablayoutsamples D/SimpleHomeAdapter: widthPixels: 1080
2021-03-23 09:34:15.434 28100-28100/com.flyco.tablayoutsamples D/SimpleHomeAdapter: heightPixels: 2201
2021-03-23 09:34:15.434 28100-28100/com.flyco.tablayoutsamples D/SimpleHomeAdapter: densityDpi: 440
2021-03-23 09:34:15.434 28100-28100/com.flyco.tablayoutsamples D/SimpleHomeAdapter: density: 2.75
2021-03-23 09:34:15.434 28100-28100/com.flyco.tablayoutsamples D/SimpleHomeAdapter: scaledDensity: 2.75
2021-03-23 09:34:15.434 28100-28100/com.flyco.tablayoutsamples D/SimpleHomeAdapter: xdpi: 400.467
2021-03-23 09:34:15.434 28100-28100/com.flyco.tablayoutsamples D/SimpleHomeAdapter: ydpi: 400.525
2021-03-23 09:34:15.434 28100-28100/com.flyco.tablayoutsamples D/SimpleHomeAdapter: DisplayMetrics.DENSITY_DEVICE_STABLE: 440
```

**density**的说明：

> The logical density of the display. This is a scaling factor for the Density Independent Pixel unit, where one DIP is one pixel on an approximately 160 dpi screen (for example a 240x320, 1.5"x2" screen), providing the baseline of the system's display. Thus on a 160dpi screen this density value will be 1; on a 120 dpi screen it would be .75; etc.
>
> This value does not exactly follow the real screen size (as given by `xdpi` and `ydpi`), but rather is used to scale the size of the overall UI in steps based on gross changes in the display dpi. For example, a 240x320 screen will have a density of 1 even if its width is 1.8", 1.3", etc. However, if the screen resolution is increased to 320x480 but the screen size remained 1.5"x2" then the density would be increased (probably to 1.5).
>
> 显示的逻辑密度。 这是密度独立像素单位的比例因子，其中一个DIP是大约160 dpi屏幕（例如240x320、1.5“ x2”屏幕）上的一个像素，提供系统显示的基线。 因此，在160dpi的屏幕上，此密度值为1； 在120 dpi的屏幕上为0.75； 等等。
>
> 此值不完全符合实际屏幕大小（由xdpi和ydpi给出），而是用于根据显示dpi的总体变化来逐步缩放整个UI的大小。 例如，一个240x320的屏幕即使其宽度为1.8“，1.3”等，其密度也将为1。但是，如果将屏幕分辨率提高到320x480，但屏幕尺寸保持为1.5“ x2”，则密度将增加 （可能是1.5）。

> **DPI**（英语：**D**ots **P**er **I**nch，每英寸点数）是一个量度单位，用于点阵数字图像，意思是指每一[英寸](https://zh.wikipedia.org/wiki/英吋)长度中，取样或可显示或输出点的数目。如：打印机输出可达600DPI的分辨率，表示打印机可以在每一平方英寸的面积中可以输出600X600＝360000个输出点。

所以 densityDpi / 160 = density，如 440 / 160 = 2.75



**scaledDensity**的说明：

> A scaling factor for fonts displayed on the display. This is the same as `density`, except that it may be adjusted in smaller increments at runtime based on a user preference for the font size.
>
> 显示屏上显示的字体的比例因子。 这与密度相同，除了可以在运行时根据用户对字体大小的喜好以较小的增量进行调整。

如果在系统中，将字体大小设置为最大，则获取的结果就有变化：

```java
2021-03-23 09:55:25.815 2826-2826/com.flyco.tablayoutsamples D/SimpleHomeAdapter: density: 2.75
2021-03-23 09:55:25.815 2826-2826/com.flyco.tablayoutsamples D/SimpleHomeAdapter: scaledDensity: 3.85
```

`scaledDensity`由`2.75`变为`3.85`



不错的文章，值得细看：

+ [Screen measurement in android](https://twiserandom.com/android/screen-measurement-in-android/index.html)
+ [The DisplayMetrics class in android a tutorial](https://twiserandom.com/android/util/the-displaymetrics-class-in-android-a-tutorial/index.html)























