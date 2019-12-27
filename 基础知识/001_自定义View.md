# 自定义View
官方教程[Custom View Components](https://developer.android.com/guide/topics/ui/custom-components)

## 继承View

可以直接继承`View`，也可以继承`View`的子类，如[Button](https://developer.android.com/reference/android/widget/Button.html)

```java
class PieChart extends View {
    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
```

## 自定义属性

参考[Define Custom Attributes](https://developer.android.com/training/custom-views/create-view#customattr)

+ 在 `<declare-styleable> `中定义view的attributes，一般在`res/values/attrs.xml`中

+ 在XML布局中，指定value的值
+ 在运行时获取属性值
+ 使用获取到的值

例如：

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
   <declare-styleable name="PieChart">
       <attr name="showText" format="boolean" />
       <attr name="labelPosition" format="enum">
           <enum name="left" value="0"/>
           <enum name="right" value="1"/>
       </attr>
   </declare-styleable>
</resources>
```

定义2个自定义属性 `showText` 和 `labelPosition`, 属于`PieChart` styleable entity，名称与类名相同

定义后就可以在 layout XML 文件中使用，与其它属性不同的是，自定义的属性的命令空间不同，它们属于`http://schemas.android.com/apk/res/[your package name]`，而不是`http://schemas.android.com/apk/res/android`

使用例子如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:custom="http://schemas.android.com/apk/res/com.example.customviews">
 <com.example.customviews.charting.PieChart
     custom:showText="true"
     custom:labelPosition="left" />
</LinearLayout>
```

注意使用的是`com.example.customviews.charting.PieChart`，作为tag 标签的名称，如果是内部类可以使用如下的形式`com.example.customviews.charting.PieChart$PieView`

**使用自定义属性**

当一个view被从布局的xml中创建，xml标签中的所用属性被读取，传递到了view的构造方法中的[AttributeSet](https://developer.android.com/reference/android/util/AttributeSet.html)中

把 `AttributeSet` 传递到 `obtainStyledAttributes()`方法中，该方法会传回一个已取消引用和样式化的值的`TypedArray`数组(This method passes back a TypedArray array of values that have already been dereferenced and styled.)

```java
public PieChart(Context context, AttributeSet attrs) {
   super(context, attrs);
   TypedArray a = context.getTheme().obtainStyledAttributes(
        attrs,
        R.styleable.PieChart,
        0, 0);

   try {
       mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
       textPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
   } finally {
       a.recycle();
   }
}
```

> Note that `TypedArray` objects are a shared resource and must be recycled after use.
>
> 注意 `TypedArray` 对象是一个共享的资源，在use之后必须recycled



### 添加属性和事件

上面的例子只能在初始化时读取属性的值。添加动态行为，可以为每个自定义属性添加getter和setter

```java
public boolean isShowText() {
   return mShowText;
}

public void setShowText(boolean showText) {
   mShowText = showText;
   invalidate();
   requestLayout();
}
```

注意`setShowText` 调用了 `invalidate()` 和 `requestLayout()`。



## 构造函数

参考：

+ [从 View 构造函数中被忽略的 {int defStyleAttr} 说起](https://blog.lujun.co/2017/05/09/ignored-parameter-defStyleAttr-in-view-construct/)

自定义view的时候，一般有如下的4个构造方法可供重写，已button为例

```java
public Button(Context context) {
    this(context, null);
}
public Button(Context context, AttributeSet attrs) {
    this(context, attrs, com.android.internal.R.attr.buttonStyle);
}
public Button(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context, attrs, defStyleAttr, 0);
}
public Button(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
}
```

参考：[A deep dive into Android View constructors](https://blog.danlew.net/2016/07/19/a-deep-dive-into-android-view-constructors/)一文中的讲解

已`ImageView`为例，在XML中声明一个ImageView

```xml
<ImageView
  android:layout_width="wrap_content"
  android:layout_height="wrap_content"
  android:src="@drawable/icon"
  />
```

layout_width、layout_height、src是怎么来的呢？是在`<declare-styleable>`中声明的，如src:

```xml
<declare-styleable name="ImageView">
  <!-- Sets a drawable as the content of this ImageView. -->
  <attr name="src" format="reference|color" />

  <!-- ...snipped for brevity... -->

</declare-styleable>
```

每个`<declare-styleable>`会为每个attribute生成一个`R.styleable.[name]`和`R.styleable.[name]_[attribute]`。例如好，上面的会生成`R.styleable.ImageView` 和 `R.styleable.ImageView_src`

 `R.styleable.[name]`是所有attribute资源的数组。系统使用它来查找属性值。 每个`R.styleable.[name]_[attribute]`只是该数组的索引，因此您可以一次检索所有属性，然后单独查找每个值。



### defStyleAttr

`defStyleAttr`的解释：

>An attribute in the current theme that contains a reference to a style resource that supplies default values for the view. Can be 0 to not look for defaults
>
>当前主题中一个包含 style 资源引用(Style 中有该 View 默认属性值集合)的值，这个引用对应的资源属性/值会填充 attrs 中没有声明的属性。如果是 0 则不会寻找默认属性值填充。

>简单的说，可以为所有的views定义一个基础样式。例如，你可以在你的theme中设置`textViewStyle`，一次性修改app中所有的`TextViews`
>
>首先，定义attribute
>
>```xml
><resources>
>  <declare-styleable name="Theme">
>
>    <!-- ...snip... -->
>
>    <!-- Default TextView style. -->
>    <attr name="textViewStyle" format="reference" />
>
>    <!-- ...etc... -->
>
>  </declare-styleable>
></resource>
>```
>
>之后，在当前的主题中设置`textViewStyle`
>
>```xml
><resources>
>  <style name="Theme">
>
>    <!-- ...snip... -->
>
>    <item name="textViewStyle">@style/Widget.TextView</item>
>
>    <!-- ...etc... -->
>
>  </style>
></resource>
>```
>
>在`Application` 或者 `Activity`中设置主题
>
>```xml
><activity
>  android:name=".MyActivity"
>  android:theme="@style/Theme"
>  />
>```
>
>在`obtainStyledAttributes()`中使用
>
>```java
>TypedArray ta = theme.obtainStyledAttributes(attrs, R.styleable.TextView, R.attr.textViewStyle, 0);
>```



### defStyleRes

`defStyleRes`的解释:

> A resource identifier of a style resource that supplies default values for the view, used only if defStyleAttr is 0 or can not be found in the theme. Can be 0 to not look for defaults
>
> 为 View 提供默认值的一个样式资源标识符(不局限于当前 Theme 中)，仅在 `defStyleAttr` 为 0 或 `defStyleAttr` 指定的 style 中无法找到默认值。如果设置为 0 无效

> defStyleRes更简单。它只是一种样式资源（即`@style/Widget.TextView`） 
>
> defStyleRes中样式的属性仅在defStyleAttr未定义时应用（无论是0还是未在主题中设置）



一个 attribute 值的确定过程大致如下：

1. xml 中查找，若未找到进入第 2 步；
2. xml 中的 style 查找，若未找到进入第 3 步；
3. 若 defStyleAttr 不为 0，由 defStyleAttr 指定的 style 中寻找，若未找到进入第 4 步；
4. 若 defStyleAttr 为 0 或 defStyleAttr 指定的 style 中寻找失败，进入 defStyleRes 指定的 style 中寻找，若寻找失败，进入第 5 步查找；
5. 查找在当前 Theme 中指定的属性值。

## 入门教程

+ [Android Custom Views Tutorial. Part-1](https://medium.com/mindorks/android-custom-views-tutorial-part-1-115fa8d53be5)
+ [Android Custom Views Tutorial. Part-2: Custom Attributes](<https://medium.com/mindorks/android-custom-views-tutorial-part-2-custom-attributes-3adde12c846d>)
+ [Android Custom Views Part-3: Shape Manipulations](<https://medium.com/mindorks/android-custom-views-part-3-shape-manipulations-2d2fcc149ae7>)



1.`invalidate()`和`postInvalidate()` 的区别

>If you want to re draw your view from `UI Thread` you can call `invalidate()` method.

> If you want to re draw your view from `Non UI Thread` you can call `postInvalidate()` method.





