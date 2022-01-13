# PorterDuffMode

参考：

+ [HenCoder Android 开发进阶: 自定义 View 1-2 Paint 详解](https://rengwuxian.com/ui-1-2/)



在使用Xfermode时，一般需要做如下的两件事

1.禁用硬件加速

```java
setLayerType(LAYER_TYPE_SOFTWARE, null);
```

2.使用离屏绘制

```java
int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

//核心绘制代码

canvas.restoreToCount(layerId);
```



关于`PorterDuff.Mode`的原理，可参考如下的文章：

+ [各个击破搞明白PorterDuff.Mode - 简书](https://www.jianshu.com/p/d11892bbe055)

+  [What does PorterDuff.Mode mean in android graphics.What does it do?](https://stackoverflow.com/questions/8280027/what-does-porterduff-mode-mean-in-android-graphics-what-does-it-do)

也可参考官方文档[PorterDuff.Mode](https://developer.android.com/reference/android/graphics/PorterDuff.Mode.html)

其基本使用模式如下：

```java
Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

...

canvas.drawBitmap(rectBitmap, 0, 0, paint); // 画方
paint.setXfermode(xfermode); // 设置 Xfermode
canvas.drawBitmap(circleBitmap, 0, 0, paint); // 画圆
paint.setXfermode(null); // 用完及时清除 Xfermode
```

目标图像与源图像合成效果如下，借用一张图：

![162](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/162.png)


















