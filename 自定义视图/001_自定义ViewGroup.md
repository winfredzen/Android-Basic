# 自定义ViewGroup

> `View`及`ViewGroup`基本相同，只是在`ViewGroup`中不仅要绘制自己，还要绘制其中的子控件，而`View`只需要绘制自己就可以了，所以这里就以`ViewGroup`为例来讲述整个绘制流程。

绘制流程分为3步

1.测量 - `onMeasure()`，测量当前控件的大小，为正式布局提供建议（注意：只是建议，至于用不用，要看`onLayout()`函数）

2.布局 - `onLayout()`，使用`layout()`函数对所有子控件进行布局

3.绘制 - `onDraw()`，根据布局的位置绘图



布局绘画涉及两个过程：测量过程和布局过程。测量过程通过`measure()`函数来实现，是View树自顶向下的遍历，每个View在循环过程中将尺寸细节往下传递，当测量过程完成之后，所有的View都存储了自己的尺寸。布局过程则通过`layout()`函数来实现，也是自顶向下的，在这个过程中，每个父View负责通过计算好的尺寸放置它的子View。

前面提到，`onMeasure()`函数是用来测量当前控件大小的，给`onLayout()`函数提供数值参考。需要特别注意的是，测量完成以后，要通过`setMeasuredDimension(int，int)`函数设置给系统



## onMeasure

```java
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
```

> Measure the view and its content to determine the measured width and the measured height. This method is invoked by `measure(int, int)` and should be overridden by subclasses to provide accurate and efficient measurement of their contents.
>
> **CONTRACT:** When overriding this method, you *must* call `setMeasuredDimension(int, int)` to store the measured width and height of this view. Failure to do so will trigger an `IllegalStateException`, thrown by `measure(int, int)`. Calling the superclass' `onMeasure(int, int)` is a valid use.
>
> The base class implementation of measure defaults to the background size, unless a larger size is allowed by the MeasureSpec. Subclasses should override `onMeasure(int, int)` to provide better measurements of their content.
>
> If this method is overridden, it is the subclass's responsibility to make sure the measured height and width are at least the view's minimum height and width (`getSuggestedMinimumHeight()` and `getSuggestedMinimumWidth()`).

参数：`widthMeasureSpec`和`heightMeasureSpec`，是指父类传递过来给当前`View`的一个建议值，即想把当前`View`的尺寸设置为宽`widthMeasureSpec`、高`heightMeasureSpec`

表面上看起来是`int`类型，其实是`mode+size`的组合形式

`widthMeasureSpec`和`heightMeasureSpec`转换为二进制数字表示，它们都是`32`位的，前2位代表模式（`mode`），后面30位代表数值（`size`）

mode有三种：

+ `UNSPECIFIED`（未指定）：父元素不对子元素施加任何束缚，子元素可以得到任意想要的大小。
+ `EXACTLY`（完全）：父元素决定子元素的确切大小，子元素将被限定在给定的边界里而忽略它本身的大小。
+ `AT_MOST`（至多）：子元素至多达到指定大小的值。

[MeasureSpec](https://developer.android.com/reference/android/view/View.MeasureSpec#MeasureSpec())类提供了获取mode和size 方法

```java
getMode(int measureSpec)
getSize(int measureSpec)
```



`XML`布局和模式有如下的对应关系：

+ `wrap_content`-＞`MeasureSpec.AT_MOST`
+ `match_parent`-＞`MeasureSpec.EXACTLY`
+ 具体值-＞`MeasureSpec.EXACTLY`



> 一定要注意的是，当模式是`MeasureSpec.EXACTLY`时，就不必设定我们计算的数值了，因为这个大小是用户指定的，我们不应更改。但当模式是`MeasureSpec.AT_MOST`时，也就是说用户将布局设置成了`wrap_content`，就需要将大小设定为我们计算的数值，因为用户根本没有设置具体值是多少，需要我们自己计算。



## onLayout

```java
protected void onLayout (boolean changed, 
                int left, 
                int top, 
                int right, 
                int bottom)
```

> Called from layout when this view should assign a size and position to each of its children. Derived classes with children should override this method and call layout on each of their children.



如下的例子，实现三个`TextView`的竖排，背景的layout宽度是`match_parent`，高度是`wrap_content`，效果如下：

![006](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/006.png)



自定义类`MyLinLayout`

```java
package com.example.mylinlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyLinLayout extends ViewGroup {


    public MyLinLayout(Context context) {
        super(context);
    }

    public MyLinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int height = 0;
        int width = 0;
        int count = getChildCount();
        for (int i = 0;i < count; i++) {
            //测量子控件
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //获取子控件的高度和宽度
            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();
            //得到最大宽度，并累加高度
            height += childHeight;
            width = Math.max(childWidth, width);
        }
        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth : width,
                (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            child.layout(0, top, childWidth, top + childHeight);
            top += childHeight;
        }
    }
}
```

使用`MyLinLayout`布局：

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.example.mylinlayout.MyLinLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="#ff00ff"
    tools:context=".MainActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="第一个TextView"
        android:background="#ff0000"/>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="第二个TextView"
        android:background="#00ff00"/>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="第三个TextView"
        android:background="#0000ff"/>


</com.example.mylinlayout.MyLinLayout>
```



一些疑问点？

1.`getMeasuredWidth()`与`getWidth()`函数的区别？

> `getMeasureWidth()`函数在`measure()`过程结束后就可以获取到宽度值；而`getWidth()`函数要在`layout()`过程结束后才能获取到宽度值。
>
> `getMeasureWidth()`函数中的值是通过`setMeasuredDimension()`函数来进行设置的；而`getWidth()`函数中的值则是通过`layout(left，top，right，bottom)`函数来进行设置的。



2.container自己什么时候被布局？

> container也有父控件，它的布局是在父控件中由它的父控件完成的，就这样一层一层的向上由各自的父控件完成对自己的布局，直到所有控件的顶层节点。在所有控件的顶部有一个`ViewRoot`，它才是所有控件的祖先节点
>
> 在它的布局中，会调用一个`layout()`函数
>
> ![047](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/047.png)
>
> 在`setFrame(l, t, r, b)`函数中设置的是自己的位置，设置结束以后才会调用`onLayout(changed, l, t, r, b)`函数来设置内部所有子控件的位置



## 添加边距









































