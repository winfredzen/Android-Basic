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



### AppCompatRadioButton

通过上面的使用方式，我们可以实现iOS Segment的效果，如下：

![009](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/009.png)

```xml
    <RadioGroup
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        >

        <androidx.appcompat.widget.AppCompatRadioButton
            android:id="@+id/rbLeft"
            android:layout_width="0dp"
            android:layout_height="50dp"
            android:textColor="@android:color/white"
            android:text="Left"
            android:layout_weight="1"
            android:button="@android:color/transparent"
            android:gravity="center"
            android:background="@drawable/radio_button_left_selector"
            android:checked="true"
            android:onClick="onRadioButtonClicked"
            />

        <androidx.appcompat.widget.AppCompatRadioButton
            android:id="@+id/rbCenter"
            android:layout_width="0dp"
            android:layout_height="50dp"
            android:textColor="@android:color/holo_red_light"
            android:text="Center"
            android:layout_weight="1"
            android:button="@android:color/transparent"
            android:gravity="center"
            android:background="@drawable/radio_button_center_selector"
            android:checked="false"
            android:onClick="onRadioButtonClicked"
            />

        <androidx.appcompat.widget.AppCompatRadioButton
            android:id="@+id/rbRight"
            android:layout_width="0dp"
            android:layout_height="50dp"
            android:textColor="@android:color/holo_red_light"
            android:text="Right"
            android:layout_weight="1"
            android:button="@android:color/transparent"
            android:gravity="center"
            android:background="@drawable/radio_button_right_selector"
            android:checked="false"
            android:onClick="onRadioButtonClicked"
            />

    </RadioGroup>
```

这里是通过点击事件`onRadioButtonClicked`，来处理`RadioButton`的文字颜色



## 自定义Radio Groups & Radio Buttons

参考：

+ [Creating Custom Radio Groups & Radio Buttons in Android](https://crosp.net/blog/software-development/mobile/android/creating-custom-radio-groups-radio-buttons-android/)



上文中自定义的`PresetRadioGroup`，与原来的`RadioGroup`有许多类似之处

**所以很值得我们去看看`RadioGroup`的源码**

最终实现的效果如下：

![010](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/010.png)



















































