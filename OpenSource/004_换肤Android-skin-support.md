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

    <!--  Tab频道选中文字颜色  -->
    <color name="channel_text_selected_white">#FF2D3442</color>
    <!--  Tab频道未选中文字颜色  -->
    <color name="channel_text_normal_white">#B32D3542</color>
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



![014](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/014.png)

![015](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/015.png)



2.`EditText`相关设置

a.设置清除按钮图标

文档中说是支持`drawableRight`，但由于我是自定义view，在自定义view中自定义设置了`drawableRight`，所以导致了设置无效

所以在自定义view中需要单独的进行默写设置

```java
        //清除按钮图标
        Drawable clearDrawable = SkinCompatResources.getDrawable(getContext(), R.drawable.ic_edit_clear);
        if (clearDrawable != null) {
            mClearDrawable = clearDrawable;
            mClearDrawable.setBounds(0, 0, 40, 40);
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mClearDrawable, getCompoundDrawables()[3]);
        }
```

![014](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/014.png)

![017](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/017.png)

在我的自定义view中，换肤对`textColor`和`textColorHint`貌似无效，所以还会改用`textAppearance`

在xml中设置`android:textAppearance="@style/SearchEditTextTextAppearance"`

```java
    <!--  搜索框TextAppearance  -->
    <style name="SearchEditTextTextAppearance">
        <item name="android:textColor">@color/search_text_color</item>
        <item name="android:textColorHint">@color/search_hint_text_color</item>
    </style>
```

b.光标cursor

对`EditText`来说，还有一个光标Cursor的问题，一般而言是通过设置`textCursorDrawable`也是有效的

但在我当前测试的车机上却没有生效，后来也通过来代码设置，但是`setTextCursorDrawable(cursorDrawable);`该方法，只是在`Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q`才有效果，遗憾的是当前车机的版本比较低，所以在低版本上通过其它的方法来设置，尝试了多种方法，也没有效果，最后也不了了之

最后是通过将`android:textCursorDrawable="@null"`，测试光标的颜色会与`textColor`的颜色保持一致



3.自定义view

按文档要求自定义view，需实现`SkinCompatSupportable`接口，重写`applySkin()`方法

然后可通过`SkinCompatResources`类中的系列方法来获取drawable和color

如：

```java
SkinCompatResources.getDrawable(getContext(), R.drawable.ic_edit_clear);

SkinCompatResources.getColor(getContext(), R.color.search_text_color)
```







































