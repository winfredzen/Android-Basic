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



