# 封装控件

在xml中经常会发现如下的代码：

![001](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/001.png)



这些属性不是控件自带的，是自定义的

如何添加自定义的属性？利用xml中的`declare-styleable`来实现

在`res/values`目录下，新建`attrs.xml`文件，例如如下的xml：

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <declare-styleable name="MyTextView">
        <attr name="header" format="reference" />
        <attr name="headerHeight" format="dimension" />
        <attr name="headerVisibleHeight" format="dimension" />
        <attr name="age">
            <flag name="child" value="10" />
            <flag name="young" value="18" />
            <flag name="old" value="60" />
        </attr>
    </declare-styleable>

</resources>
```

+ `declare-styleable` 有个`name`属性，对应类名，也就是为哪个类添加自定义属性

> 自定义属性可以组合使用，比如
>
> ```xml
> <attr name="border_color" format="color|reference" />
> ```
>
> > 既可以自定义`color`的值（比如#xxxxxx），也可以引用`color.xml`中的值，如`@color/xxx`
>
> + `reference` - 指的是引用`strings.xml`、`colors.xml`、`drawable.xml`中的值

+ `flag`是自定义的，类似于`android:gravity="top"`
+ `dimension`指的是`dimension.xml`文件引用过来的值。注意如果是`dp`，就会进行像素转换

其它的一些定义：

+ boolean - 布尔值

+ float - 浮点值

+ integer - 整型值

+ string - 字符串

+ fraction - 百分数，如

  ![002](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/002.png)

+ enum - 枚举值

  ![003](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/003.png)



在自定义控件中获取属性值，主要使用`TypedArray`类，这个类提供了获取某个属性的所有的方法

![004](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/004.png)



比如上的例子，xml中的定义如下：

```xml
    <com.example.customviewtest.MyTextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:header="@drawable/ic_launcher_background"
        app:headerHeight="300dp"
        app:headerVisibleHeight="100dp"
        app:age="young"/>
```

在`MyTextView`中通过`TypedArray`，获取对应的值，并设置text，来显示

```java
public class MyTextView extends TextView {
    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        float headerHeight = typedArray.getDimension(R.styleable.MyTextView_headerHeight, -1);
        int age = typedArray.getInt(R.styleable.MyTextView_age, -1);

        typedArray.recycle();
        this.setText("headerHeight:" + headerHeight + " age:" + age);

    }

}
```

![005](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/005.png)





























