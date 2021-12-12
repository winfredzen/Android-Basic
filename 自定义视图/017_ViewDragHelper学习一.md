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
                return true;//表示对所有的子view都进行捕捉
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                Log.d(TAG, "child = " + child + ", left = " + left + ", dx = " + dx);
                return left;//让View横向跟随手指移动
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                Log.d(TAG, "child = " + child + ", top = " + top + ", dy = " + dy);
                return top;//让View纵向跟随手指移动
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



### 详解

#### 构造方法

```java
public static ViewDragHelper create(@NonNull ViewGroup forParent, float sensitivity,
            @NonNull Callback cb)
```

+ forParent - 即要拖动的item的父控件的ViewGroup对象，这里就是DragLayout
+ sensitivity - 敏感度，一般设置为1.0，值越大，越敏感。（可以理解为类似于TouchSlop，即手指一动一段距离后才反馈个App）
+ cb - 用于在拦截到消息后，将各种回调结果返回给用户，让用户操作Item



#### Callback

1.`public abstract boolean tryCaptureView(@NonNull View child, int pointerId)`

> Called when the user's input indicates that they want to capture the given child view with the pointer indicated by pointerId. The callback should return true if the user is permitted to drag the given view with the indicated pointer.
> ViewDragHelper may call this method multiple times for the same view even if the view is already captured; this indicates that a new pointer is trying to take control of the view.
> If this method returns true, a call to onViewCaptured(View, int) will follow if the capture is successful.
>
> 在调用时，表示用户用给定的pointerId来capture指定子view。
>
> 如果允许用户使用指定的pointerId拖动给定的view，则回调应返回 true。
>
> 即使视图已经被捕获，ViewDragHelper 也可能为同一个视图多次调用此方法； 这表明一个新的pointer正试图控制视图。
> 如果此方法返回 true，则在捕获成功时将调用 onViewCaptured(View, int)。

+ child - 当前用户触摸的子控件的View对象
+ pointerId - 当前触摸此控件的手指所对应的pointerId

> 在MotionEvent中引入了Pointer概念。一个Pointer就表示一个触摸点，每个Pointer都有自己的事件类型。每个Pointer都有一个自己的id和索引。Pointer的id在整个事件流中不会发生变化，但是索引会发生变化
>
> | 事件            | PointerId                          | PointerIndex                           |
> | --------------- | ---------------------------------- | -------------------------------------- |
> | 依次按下3根手指 | 3个手指的id依次为0、1、2           | 3根手指的索引为0、1、2                 |
> | 抬起第2根手指   | 第1根手指的id为0，第3根手指的id为2 | 第1根手指的索引为0，第3根手指的索引为1 |
> | 抬起第1根手指   | 第3根手指的id为2                   | 第3根手指的索引变为0                   |
>
> **可见同一根手指的id是不会变化的**



+ 返回值 - 表示是否对这个View进行各种事件的捕捉



2.`public int clampViewPositionHorizontal(@NonNull View child, int left, int dx)`

> Restrict the motion of the dragged child view along the horizontal axis. The default implementation does not allow horizontal motion; the extending class must override this method and provide the desired clamping.
>
> 限制拖动的子视图沿水平轴的运动。 默认实现不允许水平运动；

当手指在子View上横向运动时，会在这个函数中回调通知

+ child - 当前手指横向移动所在的子View
+ left - 当前子View如果跟随手指移动，那么它即将移动到的位置的left坐标值就是这里的left
+ dx - 手指横向移动的距离
+ 返回值 - 返回子View的新left坐标值，系统会把该子View的left坐标移动到整个位置



3.`public int clampViewPositionHorizontal(@NonNull View child, int left, int dx)`

与上面类似，当手指在子View纵向移动时，会在这个函数中回调通知



### ViewDragHelper拦截消息

拦截消息在`onInterceptTouchEvent`和`onTouchEvent`函数中进行处理

```java
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }
```

> `onTouchEvent`中需要返回true，表示这里拦截了event消息，这样消息就不会上传个父控件了。而且，只有`MotionEvent.ACTION_DOWN`消息到来时返回true后，后续的消息才能继续到来，否则后续的消息就不会再留到这里



## 疑问解答

### 1.clampViewPositionHorizontal

不重写`clampViewPositionHorizontal`会是神马效果？

![153](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/153.gif)

可见每个item随便滑动了一点，这个item就移动到了左边界，无论怎么移动，left值一直是0



同理，如果不重写`clampViewPositionVertical`，效果如下：

![154](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/154.gif)



























