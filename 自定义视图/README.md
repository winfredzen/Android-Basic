# View

参考：

+ [View](https://developer.android.com/reference/android/view/View)

在Android中有2种坐标系

+ Android坐标系
+ View坐标系

**Android坐标系**，屏幕左上角作为坐标系的原点

> 另外在触控事件中，使用`getRawX()`和`getRawY()`方法获得的坐标也是Android坐标系

![010](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/010.png)

**View坐标系**

![011](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/011.png)

**View获取自身的宽和高**

```java
getWidth()
getHeight()
```

**View自身的坐标**

获取View到其父控件的(ViewGroup)的距离

```java
 getTop()
 getLeft()
 getRight()
 getBottom()
```

**MotionEvent提供的方法**

上面*View坐标系*的图中的*原点*，假设就是我们触摸的点。我们知道无论是View还是ViewGroup，最终的点击事件都会由`onTouchEvent(MotionEvent event)`方法来处理。MotionEvent在用户交互中作用重大，其内部提供了很多事件常量，比如我们常用的`ACTION_DOWN`、`ACTION_UP`和`ACTION_MOVE`。此外，`MotionEvent`也提供了获取焦点坐标的各种方法

+ `getX()`：获取点击事件距离控件左边的距离，即视图坐标
+ `getY()`：获取点击事件距离控件顶边的距离，即视图坐标
+ `getRawX()`：获取点击事件距离整个屏幕左边的距离，即绝对坐标
+ `getRawY()`：获取点击事件距离整个屏幕顶边的距离，即绝对坐标



## View的滑动

基本思想：当点击事件传到`View`时，系统记下触摸点的坐标，手指移动时系统记下移动后触摸的坐标并算出偏移量，并通过偏移量来修改`View`的坐标



### layout()方法实现

```java
public class CustomView extends View {

    private int lastX;
    private int lastY;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取手指触摸点的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移位置
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //调用layout方法来重新放置它的位置
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                break;
        }
        return true;

    }
}
```

![012](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/012.gif)



### offsetLeftAndRight()和offsetTopAndBottom()

使用效果与`layout()`方法差不多

```java
            case MotionEvent.ACTION_MOVE:
                //计算偏移位置
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);
                break;
```



### LayoutParams

将`ACTION_MOVE`下的代码，修改为如下的形式：

```java
            case MotionEvent.ACTION_MOVE:
                //计算偏移位置
                int offsetX = x - lastX;
                int offsetY = y - lastY;

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)getLayoutParams();
                layoutParams.leftMargin = getLeft() + offsetX;
                layoutParams.topMargin = getTop() + offsetY;
                setLayoutParams(layoutParams);
                break;
```

这里因为父控件是`FrameLayout`，所以是`FrameLayout.LayoutParams`

也可以使用`ViewGroup.MarginLayoutParams`，如下：

```java
ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)getLayoutParams();
layoutParams.leftMargin = getLeft() + offsetX;
layoutParams.topMargin = getTop() + offsetY;
setLayoutParams(layoutParams);
```



### scrollTo()与scrollBy()

`scrollTo()`表示移动到一个具体的坐标点，`scrollBy()`表示移动的增量

```java
public void scrollBy(int x, int y) {
    scrollTo(mScrollX + x, mScrollY + y);
}
```

将`ACTION_MOVE`修改为如下的内容，同样可以达到效果：

```java
            case MotionEvent.ACTION_MOVE:
                //计算偏移位置
                int offsetX = x - lastX;
                int offsetY = y - lastY;

                ((View)getParent()).scrollBy(-offsetX, -offsetY);
                break;
```

> **为什么是负值？**
>
> 假设我们正用放大镜来看报纸，放大镜用来显示字的内容。同样我们可以把放大镜看作我们的手机屏幕，它们都是负责显示内容的；而报纸则可以被看作屏幕下的画布，它们都是用来提供内容的。放大镜外的内容，也就是报纸的内容不会随着放大镜的移动而消失，它一直存在。同样，我们的手机屏幕看不到的视图并不代表其不存在，如图3-6所示。画布上有3个控件，即Button、EditText 和 SwichButton。只有 Button 在手机屏幕中显示，它的坐标为（60，60）。现在我们调用scrollBy（50，50），按照字面的意思，这个 Button 应该会在屏幕右下侧，可是事实并非如此。如果我们调用scrollBy（50，50），里面的参数都是正值，我们的手机屏幕向 X 轴正方向，也就是向右边平移 50，然后手机屏幕向 Y 轴正方向，也就是向下方平移 50，平移后的效果如图3-7所示。虽然我们设置的数值是正数并且在X轴和Y轴的正方向移动，但 Button 却向相反方向移动了，这是参考对象不同导致的差异。所以我们用 scrollBy 方法的时候要设置负数才会达到自己想要的效果。
>
> ![013](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/013.png)



### Scroller

如下使用`Scroller`，实现滑动，沿着X轴滑动400像素

```java
public class CustomView_04 extends View {

    private Scroller mScroller;

    public CustomView_04(Context context) {
        super(context);
    }

    public CustomView_04(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            Log.d("CustomView", "x : " + mScroller.getCurrX() + " y: " + mScroller.getCurrY());
            ((View)getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, 2000);
        invalidate();
    }

}
```

在点击按钮的时候，调用`mCustomView.smoothScrollTo(-400, 0);`来滑动

```java
findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        CustomView_04 mCustomView = (CustomView_04)findViewById(R.id.customView);
        mCustomView.smoothScrollTo(-400, 0);
    }
});
```

效果如下：

![014](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/014.gif)

> 原理：重写`computeScroll()`方法，系统会在绘制View的时候在`draw()`方法中调用该方法。在这个方法中，我们调用父类的`scrollTo()`方法并通过`Scroller`来不断获取当前的滚动值，每滑动一小段距离我们就调用`invalidate()`方法不断地进行重绘，重绘就会调用`computeScroll()`方法，这样我们通过不断地移动一个小的距离并连贯起来就实现了平滑移动的效果。
>
> `computeScroll()`方法说明
>
> > Called by a parent to request that a child update its values for mScrollX and mScrollY if necessary. This will typically be done if the child is animating a scroll using a `Scroller` object.
>
> `invalidate()`方法说明：
>
> > Invalidate the whole view. If the view is visible, `onDraw(android.graphics.Canvas)` will be called at some point in the future.
> >
> > This must be called from a UI thread. To call from a non-UI thread, call `postInvalidate()`.

> 在 `startScroll()`方法中并没有调用类似开启滑动的方法，而是保存了传进来的各种参数：startX和startY表示滑动开始的起点，dx和dy表示滑动的距离，duration则表示滑动持续的时间。所以 `startScroll()`方法只是用来做前期准备的，并不能使 View 进行滑动。关键是我们在`startScroll()`方法后调用了 `invalidate()`方法，这个方法会导致View的重绘，而View的重绘会调用View的`draw()`方法，`draw()`方法又会调用View的`computeScroll()`方法。



## View事件分发机制

点击事件用`MotionEvent`来表示，当一个点击事件产生后，事件最先传递给`Activity`

`Activity`的构成可参考：[Activity的构成](https://blog.csdn.net/qq_38182125/article/details/86546813)

![016](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/016.png)

当我们点击屏幕时，就产生了点击事件，这个事件被封装成了一个类：`MotionEvent`。而当这个`MotionEvent`产生后，那么系统就会将这个`MotionEvent`传递给`View`的层级，`MotionEvent`在`View`中的层级传递过程就是点击事件分发。在了解了什么是事件分发后，我们还需要了解事件分发的3个重要方法。点击事件有3个重要的方法，它们分别是：

+ `dispatchTouchEvent(MotionEvent ev)`—用来进行事件的分发
+ `onInterceptTouchEvent(MotionEvent ev)`—用来进行事件的拦截，在`dispatchTouchEvent()`中调用，需要注意的是View没有提供该方法
+ `onTouchEvent(MotionEvent ev)`—用来处理点击事件，在`dispatchTouchEvent()`方法中进行调用



> 首先讲一下点击事件由上而下的传递规则，当点击事件产生后会由 `Activity` 来处理，传递给`PhoneWindow`，再传递给`DecorView`，最后传递给顶层的`ViewGroup`。一般在事件传递中只考虑 `ViewGroup` 的 `onInterceptTouchEvent` 方法，因为一般情况下我们不会重写 `dispatchTouchEvent()` 方法。对于根`ViewGroup`，点击事件首先传递给它的`dispatchTouchEvent()`方法，如果该`ViewGroup`的`onInterceptTouchEvent()`方法返回`true`，则表示它要拦截这个事件，这个事件就会交给它的`onTouchEvent()`方法处理；如果`onInterceptTouchEvent()`方法返回`false`，则表示它不拦截这个事件，则这个事件会交给它的子元素的`dispatchTouchEvent()`来处理，如此反复下去。如果传递给底层的`View`，`View`是没有子`View`的，就会调用`View`的`dispatchTouchEvent()`方法，一般情况下最终会调用`View`的`onTouchEvent()`方法。
>
> ![017](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/017.png)



## View的工作流程

参考：

+ [Android View的工作流程](https://blog.csdn.net/qian520ao/article/details/78657084)
+ [Android 的 View 工作流程详解](https://juejin.cn/post/6844903694073331720)























































