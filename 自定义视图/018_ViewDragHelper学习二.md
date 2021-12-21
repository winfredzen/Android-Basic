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

如下的例子，在`onEdgeLock`中，`edgeFlags == ViewDragHelper.EDGE_LEFT`返回true，表示只开启**左边缘锁定**

```java
            @Override
            public boolean onEdgeLock(int edgeFlags) {
                Log.d(TAG, "onEdgeLock edgeFlags:" + edgeFlags);
                if (edgeFlags == ViewDragHelper.EDGE_LEFT) {
                    return true;
                }
                return false;
            }
        });

        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_TOP);
```

1.当在左边缘上下滑动时，如下的操作

![158](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/158.gif)

此时日志输出如下：

```java
2021-12-19 11:54:27.523 14995-14995/com.example.draglayoutdemo D/DragLayout: onEdgeTouched edgeFlags:1
2021-12-19 11:54:27.524 14995-14995/com.example.draglayoutdemo D/DragLayout: onEdgeTouched edgeFlags:1
2021-12-19 11:54:27.570 14995-14995/com.example.draglayoutdemo D/DragLayout: onEdgeLock edgeFlags:1
```

> 可见先调用两次`onEdgeTouched`后，在调用`onEdgeLock`，之后并没用调用`onEdgeDragStarted`函数
>
> > 怎么理解呢？
> >
> > 当用户手指在侧边滑动时，首先通过`onEdgeTouched`函数通知ViewGroup有滑动动作，然后当用户持续滑动、产生拖动动作时，ViewDragHelper在调用`onEdgeDragStarted`函数之前，会通过`onEdgeLock`函数来获取当前边缘是否被锁定。如果锁定了，即使用户继续滑动，ViewDragHelper也不会调用`onEdgeDragStarted`函数来进行通知

2.在左边缘，左右滑动

日志输出如下：

```java
2021-12-19 12:42:36.620 14995-14995/com.example.draglayoutdemo D/DragLayout: onEdgeTouched edgeFlags:1
2021-12-19 12:42:36.620 14995-14995/com.example.draglayoutdemo D/DragLayout: onEdgeTouched edgeFlags:1
2021-12-19 12:42:36.678 14995-14995/com.example.draglayoutdemo D/DragLayout: onEdgeDragStarted edgeFlags:1
```

> 锁定了左边缘，而在左边缘左右滑动时，任然会调用`onEdgeDragStarted`函数，而`onEdgeLock`并没有被调用
>
> 这是为什么呢？
>
> `onEdgeLock`函数的调用是有条件的。如下：
>
> ![159](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/159.png)
>
> > 只有当dx < dy * 0.5时，才会执行`onEdgeLock`函数，判断用户是否锁定了这个边缘。如果锁定了这个边缘，就不会再调用`onEdgeDragStarted`函数。如果dx、dy不满足dx < dy * 0.5，就直接执行`onEdgeDragStarted`函数。
>
> > dx < dy * 0.5表示横向移动距离不足纵向移动距离的一半，而一般而言，左边缘处的滑动主要是向右滑动，而当dx < dy * 0.5是，表示的是上下滑动，这时系统就会通过`onEdgeLock`函数来看看ViewGroup是不是禁止了这种诡异的操作，如果没有禁止，那么就调用`onEdgeDragStarted`函数；如果禁止了，那就不调用



## onViewReleased

`onViewReleased`函数主要用于通知ViewGroup当前用户手指已脱离了屏幕的事件

### smoothSlideViewTo

```java
public boolean smoothSlideViewTo(@NonNull View child, int finalLeft, int finalTop)
```

函数的作用是触发移动，将指定的View从速度0开始移动。还需要注意的是在`smoothSlideViewTo`函数之后，还需要调用`invalidate`来触发重绘，而不会自动重绘。

如下的例子，在item2上手指释放后，item2会滚动到item1的上方

```java
            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                if (releasedChild.getId() == R.id.tv2) {
                    TextView tv1 = findViewById(R.id.tv1);
                    mDragger.smoothSlideViewTo(releasedChild, tv1.getLeft(), tv1.getTop());
                    invalidate();
                }
            }
        });

        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_TOP);


    }

    @Override
    public void computeScroll() {
        if (mDragger.continueSettling(true)) {
            invalidate();
        }
    }
```

![160](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/160.gif)

这里用到了`continueSettling`函数

```java
public boolean continueSettling(boolean deferCallbacks)
```

这个函数作用如下：

1.根据Scroller计算出的距离值，将View移动到对应的位置

2.判断当前View是否需要继续移动，如果需要继续移动，则返回true，如果不需要继续移动，则将当前View的移动状态设为STATE_IDLE，然后返回false

+ deferCallbacks - 是否使用默认方式来设置当前移动状态
+ 返回值 - 是否需要继续移动



### 其它方法

如下的方法也可以是实现移动

1.`public boolean settleCapturedViewAt(int finalLeft, int finalTop)`

+ finalLeft - 目标位置的left的坐标值
+ finalTop - 目标位置的top坐标值
+ 返回值 - 当前是否需要继续移动。如果移动未结束，则返回true；如果结束，则返回false

这个函数的作用是将当前捕捉到的View进行移动，而且其实速度是手指脱离屏幕时的瞬间速度，不像`smoothSlideViewTo`函数那样速度是0

另外`smoothSlideViewTo`可传入需要定的View，在哪儿都可以使用

而`settleCapturedViewAt`只能移动当前捕捉到的View，所以只能在`onViewReleased`回调函数中使用



2.`public void flingCapturedView(int minLeft, int minTop, int maxLeft, int maxTop)` 

用的比较少，主要作用是在固定区间内移动当前捕捉到的View。



如下的例子，使用`settleCapturedViewAt`实现释放item2时将其移动到item1的效果

```java
            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                if (releasedChild.getId() == R.id.tv2) {
                    TextView tv1 = findViewById(R.id.tv1);
                    mDragger.settleCapturedViewAt(tv1.getLeft(), tv1.getTop());
                    invalidate();
                }
            }
        });

        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_TOP);
```































