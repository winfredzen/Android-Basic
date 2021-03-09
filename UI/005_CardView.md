# CardView

参考：

+ [创建卡片式布局](https://developer.android.com/guide/topics/ui/layout/cardview?hl=zh-cn)

[`CardView`](https://developer.android.com/guide/topics/ui/layout/"/reference/androidx/cardview/widget/CardView.html"?hl=zh-cn) 微件是 [AndroidX](https://developer.android.com/jetpack/androidx?hl=zh-cn) 的一部分。继承自`FrameLayout`，方便作为其它控件容器，添加3D阴影和圆角效果

添加依赖项：

```groovy
    dependencies {
        implementation "androidx.cardview:cardview:1.0.0"
    }
```

常用属性：

+ `cardBackgroundColor` - 设置背景
+ `cardCornerRadius` - 设置圆角半径
+ `cardElevation` - 为卡片提供自定义高程。高程值越大，绘制的阴影越明显，高程值越小，阴影越淡

> Since padding is used to offset content for shadows, you cannot set padding on CardView. Instead, you can use content padding attributes in XML or `setContentPadding(int, int, int, int)` in code to set the padding between the edges of the CardView and children of CardView.
>
> 使用`contentPadding`设置内部的`padding`

> Due to expensive nature of rounded corner clipping, on platforms before Lollipop, CardView does not clip its children that intersect with rounded corners. Instead, it adds padding to avoid such intersection (See `setPreventCornerOverlap(boolean)` to change this behavior).
>
> Note that, if you specify exact dimensions for the CardView, because of the shadows, its content area will be different between platforms before Lollipop and after Lollipop. By using api version specific resource values, you can avoid these changes. Alternatively, If you want CardView to add inner padding on platforms Lollipop and after as well, you can call `setUseCompatPadding(boolean)` and pass `true`.
>
> `cardUseCompatPadding`默认为false，用于5.0及以上，true则添加额外的padding绘制阴影
>
> `cardPreventCornerOverlap`默认true，用于5.0以下，添加额外的padding，防止内容和圆角重叠



































