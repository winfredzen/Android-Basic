# 自定义RadioGroup

在应该中经常有多个选项，只能选择某一个选项的情况，通常是需要自定义UI的



## 基本使用

如果不做的其它的设置，`RadioGroup`和`RadioButton`的显示可能如下：

![007](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/007.png)

通过设置`RadioButton`中的`android:button`、`android:background`、`android:textColor`可以实现如下的效果：

```xml
<RadioButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Right"
    android:button="@android:color/transparent"
    android:background="@drawable/radio_selector"
    android:textColor="@drawable/text_color"
    android:elevation="4dp"
    android:padding="16dp"
    android:layout_margin="16dp"
    />
```

![008](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/008.png)



























