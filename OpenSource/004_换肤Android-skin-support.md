# Android-skin-support

[Android-skin-support](https://github.com/ximsfei/Android-skin-support)是一个开源的换肤库，在项目中使用了，感觉也挺方便的，记录下使用心得和遇到的问题

1.`TextView`设置`textColor`

一般情况下，可以直接在xml中定义`background`和`textColor`

```xml
            <TextView
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:background="?attr/colorPrimary"
                android:gravity="center_vertical"
                android:text="textView"
                android:textColor="@color/text_color"
                android:textSize="18sp" />
```

![012](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/012.png)

![013](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/013.png)

在[Frequently Asked Questions](https://github.com/ximsfei/Android-skin-support/blob/master/docs/FAQ.md)中，有描述如下的情况：

> **动态设置`TextView`的`textColor`(`mTextView.setTextColor(getResources().getColor(R.color.text_color)))`无法换肤?**
>
> 可以通过动态设置`textAppearance`来实现`textColor`的换肤操作。`mTextView.setTextAppearance(R.style.CustomTextAppearance);`
>
> 问题解析:
>
> 该框架是基于Resource Id来进行换肤的。控件初始化时，框架会解析布局文件中设置的资源(或者有业务代码直接设置`setTextAppearance(R.style.CustomTextAppearance)`，会从该style中解析出textColorResId)。

在项目中有一种情况是，`TextView`有被选中的情况和未被选中的情况，此时`background`和`textColor`都不一样

在代码中就可以使用`setTextAppearance`来实现，在`styles.xml`中定会2个style

```xml
    <style name="TabChannelTextAppearanceNormal" parent="WhiteText30">
        <item name="android:textColor">@color/channel_text_normal</item>
    </style>
    <style name="TabChannelTextAppearanceSelected" parent="WhiteText30">
        <item name="android:textColor">@color/channel_text_selected</item>
    </style>
```

定义颜色（按文档要求，在要换肤的资源中也要定义）

```xml
    <!--  Tab频道选中文字颜色  -->
    <color name="channel_text_selected">#FFFFFFFF</color>
    <!--  Tab频道未选中文字颜色  -->
    <color name="channel_text_normal">#FFDEDEDE</color>

    <color name="mine_text_color_white">#FF2D3543</color>
    <!--  Tab频道选中文字颜色  -->
    <color name="channel_text_selected_white">#FF2D3442</color>
```

然后就可以代码中设置了

```java
        if (category.selected) {
            binding.root.setBackgroundResource(com.iland.os.library_base.R.drawable.bg_selected);
            binding.text.setTextAppearance(R.style.TabChannelTextAppearanceSelected);
        } else {
            		binding.root.setBackgroundResource(com.iland.os.library_base.R.drawable.bg_selected_middle);
            binding.text.setTextAppearance(R.style.TabChannelTextAppearanceNormal);
        }

```

而对于背景，通过设置`setBackgroundResource`来设置

文档中有如下的内容，可供参考：

> 如果项目中有特殊需求。例如, 股票控件: 控件颜色始终为红色或绿色, 不需要随着模式切换而换肤
>
> 那么可以使用类似的方法, 直接设置drawable
>
> ```java
> setBackgroundDrawable(redDrawable) // 不支持换肤
> background="#ce3d3a"
> ```
>
> 而不是使用`R.drawable.red`
>
> ```java
> setBackgroundResource(R.drawable.red)
> background="@drawable/red"
> ```

















































