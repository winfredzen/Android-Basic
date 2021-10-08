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



1.为什么`PresetRadioGroup`中要重写`public void addView(View child, int index, ViewGroup.LayoutParams params)`方法和`protected void onFinishInflate()` 方法

我自己的理解是，在`LayoutInflater`填充布局的过程中，有调用到这些方法

在`LayoutInflater`的

```java
    /**
     * Recursive method used to descend down the xml hierarchy and instantiate
     * views, instantiate their children, and then call onFinishInflate().
     * <p>
     * <strong>Note:</strong> Default visibility so the BridgeInflater can
     * override it.
     */
    void rInflate(XmlPullParser parser, View parent, Context context,
            AttributeSet attrs, boolean finishInflate) throws XmlPullParserException, IOException {

        final int depth = parser.getDepth();
        int type;
        boolean pendingRequestFocus = false;

        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String name = parser.getName();

            if (TAG_REQUEST_FOCUS.equals(name)) {
                pendingRequestFocus = true;
                consumeChildElements(parser);
            } else if (TAG_TAG.equals(name)) {
                parseViewTag(parser, parent, attrs);
            } else if (TAG_INCLUDE.equals(name)) {
                if (parser.getDepth() == 0) {
                    throw new InflateException("<include /> cannot be the root element");
                }
                parseInclude(parser, context, parent, attrs);
            } else if (TAG_MERGE.equals(name)) {
                throw new InflateException("<merge /> must be the root element");
            } else {
                final View view = createViewFromTag(parent, name, context, attrs);
                final ViewGroup viewGroup = (ViewGroup) parent;
                final ViewGroup.LayoutParams params = viewGroup.generateLayoutParams(attrs);
                rInflateChildren(parser, view, attrs, true);
                viewGroup.addView(view, params); //这里
            }
        }

        if (pendingRequestFocus) {
            parent.restoreDefaultFocus();
        }

        if (finishInflate) {
            parent.onFinishInflate(); //这里
        }
    }
```





















































