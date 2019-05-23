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



## 入门教程

+ [Android Custom Views Tutorial. Part-1](https://medium.com/mindorks/android-custom-views-tutorial-part-1-115fa8d53be5)
+ [Android Custom Views Tutorial. Part-2: Custom Attributes](<https://medium.com/mindorks/android-custom-views-tutorial-part-2-custom-attributes-3adde12c846d>)
+ [Android Custom Views Part-3: Shape Manipulations](<https://medium.com/mindorks/android-custom-views-part-3-shape-manipulations-2d2fcc149ae7>)



1.`invalidate()`和`postInvalidate()` 的区别

>If you want to re draw your view from `UI Thread` you can call `invalidate()` method.

> If you want to re draw your view from `Non UI Thread` you can call `postInvalidate()` method.





