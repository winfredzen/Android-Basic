# ViewDragHelper学习二



## 边界判断

ViewDragHelper还可以实现边界判断的功能，判断用户是不是在ViewGroup的边缘进行拖动。注意，判断是ViewGroup的边缘，而不是屏幕的边缘

有如下的几个函数

1.`public void onEdgeTouched(int edgeFlags, int pointerId)`

当系统检测到ViewGroup边缘出现触摸事件（所以触摸事件，包括点击、滑动）时，就会通过这个回调函数通知用户

+ edgeFlags - 用户触摸的边缘，EDGE_LEFT, EDGE_TOP, EDGE_RIGHT, EDGE_BOTTOM
+ pointerId - 当前触摸此控件的手指对应的pointerId



2.`public void onEdgeDragStarted(int edgeFlags, int pointerId)`

当用户在ViewGroup边缘有拖动行为的时候，通过此函数通知用户



3.`public boolean onEdgeLock(int edgeFlags)`

该回调函数决定某个边缘是否锁定



上面的函数需要调用`setEdgeTrackingEnabled(int edgeFlags)`来开启边缘捕捉功能

如下的例子：

```java
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

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return 1;
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                Log.d(TAG, "onEdgeTouched edgeFlags:" + edgeFlags);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                Log.d(TAG, "onEdgeDragStarted edgeFlags:" + edgeFlags);
            }
        });

        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_TOP);

    }
```

在左边缘和上边缘滑动一下

控制台输出结果如下：

```java
2021-12-14 21:41:14.137 12484-12484/com.example.draglayoutdemo D/DragLayout: onEdgeTouched edgeFlags:1
2021-12-14 21:41:14.137 12484-12484/com.example.draglayoutdemo D/DragLayout: onEdgeTouched edgeFlags:1
2021-12-14 21:41:14.613 12484-12484/com.example.draglayoutdemo D/DragLayout: onEdgeDragStarted edgeFlags:1
2021-12-14 21:41:21.053 12484-12484/com.example.draglayoutdemo D/DragLayout: onEdgeTouched edgeFlags:4
2021-12-14 21:41:21.054 12484-12484/com.example.draglayoutdemo D/DragLayout: onEdgeTouched edgeFlags:4
2021-12-14 21:41:21.821 12484-12484/com.example.draglayoutdemo D/DragLayout: onEdgeDragStarted edgeFlags:4
```

> onEdgeTouched函数可能会被调用多次，但发现有拖动动作时，onEdgeDragStarted函数将只会被调用一次



上面的边界判断的几个函数，结合`public void captureChildView(@NonNull View childView, int activePointerId) `函数，可以实现一些特别的功能

以前的例子中，我们是通过`tryCaptureView(@NonNull View child, int pointerId)`函数来指定捕捉那个View的拖动动作。而`captureChildView`可以绕过`tryCaptureView`，直接开启对指定View的捕捉功能

```java
public void captureChildView(@NonNull View childView, int activePointerId) 
```

+ childView - 要捕捉拖动动作的childView对象
+ activePointerId - 激活当前childView进行捕捉操作的手指id



如下的例子：

```java
    private void init(Context context) {
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                //只捕获tv1、tv2
                return child.getId() == R.id.tv1 || child.getId() == R.id.tv2;
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

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return 1;
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                Log.d(TAG, "onEdgeTouched edgeFlags:" + edgeFlags);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                Log.d(TAG, "onEdgeDragStarted edgeFlags:" + edgeFlags);
                //开启对tv3的捕捉动作
                mDragger.captureChildView(findViewById(R.id.tv3), pointerId);
            }
        });

        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_TOP);


    }

```

效果如下：

![157](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/157.gif)

可见，当手指在边缘滑动是，tv3会被拖动，在它当前的位置，跟随手指相对移动

> 注意
>
> 1.`mDragger.captureChildView(findViewById(R.id.tv3), pointerId)`开启的捕捉功能，只会临时开启，当用户松手时，此次捕获结束，回到初始状态。如果没有在`tryCaptureView`中开启对这个View的捕捉，那么正常情况下这个View仍然不会被捕捉



### onEdgeLock

```java
public boolean onEdgeLock(int edgeFlags)
```

该函数用于决定某个边缘是否被锁定。当一个边缘被锁定，用户手指在这个边缘拖动时，会过滤一些条件，在条件成功的前提下，就不调用`onEdgeDragStarted`

+ 返回值 - 是否锁定了一个边缘。如果是true，则表示锁定当前边缘，false，表示不锁定当前边缘

> 注意，其他回调函数都是在用户出现一定的手势之后通知我们，而这个函数是在用户出现拖动动作时，根据这个函数来判断是不是锁定了某个边缘

















