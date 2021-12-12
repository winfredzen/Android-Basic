# ViewDragHelper学习一

> ViewDragHelper is a utility class for writing custom ViewGroups. It offers a number of useful operations and state tracking for allowing a user to drag and reposition views within their parent ViewGroup.
>
> ViewDragHelper是写自定义ViewGroup控件的一个非常实用的类，它提供了很多函数和跟踪状态，以便用户拖拽和重新布局其内部的子控件

一般而言，凡是包裹在ViewGroup中的子控件，当我们需要拖动这些子控件的效果时，都可以考虑使用ViewDragHelper类

> 注意：通过ViewDragHelper类能实现的功能，通过`onInterceptTouchEvent`和`onTouchEvent`这两个函数，同样也可以实现。反正则可能不行



## 简单用法

如下的例子，自定义`DragLayout`（继承自`LinearLayout`），实现可以拖动的布局。`DragLayout`实现如下：

```java
public class DragLayout extends LinearLayout {
    private static final String TAG = "DragLayout";

    private ViewDragHelper mDragger;

    public DragLayout(Context context) {
        super(context);
        init(context);
    }

    public DragLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                Log.d(TAG, "child = " + child + ", left = " + left + ", dx = " + dx);
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                Log.d(TAG, "child = " + child + ", top = " + top + ", dy = " + dy);
                return top;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }
}
```

布局如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<com.example.draglayoutdemo.DragLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/tv1"
        android:layout_margin="10dp"
        android:gravity="center"
        android:layout_gravity="center"
        android:background="#ff0000"
        android:text="Item 1"
        android:layout_width="100dp"
        android:layout_height="100dp" />

    <TextView
        android:id="@+id/tv2"
        android:layout_margin="10dp"
        android:gravity="center"
        android:layout_gravity="center"
        android:background="#00ff00"
        android:text="Item 2"
        android:layout_width="100dp"
        android:layout_height="100dp" />


    <TextView
        android:id="@+id/tv3"
        android:layout_margin="10dp"
        android:gravity="center"
        android:layout_gravity="center"
        android:background="#0000ff"
        android:text="Item 3"
        android:layout_width="100dp"
        android:layout_height="100dp" />

</com.example.draglayoutdemo.DragLayout>
```

效果如下：

![152](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/152.gif)



































