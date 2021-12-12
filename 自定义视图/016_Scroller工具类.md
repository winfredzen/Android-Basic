# Scroller工具类

View中有scrollTo和scrollBy两个函数

```java
public void scrollTo(int x, int y)
public void scrollBy(int x, int y) 
```

1.向左是X轴正向，向右是Y轴正方向

2.`scrollTo`和`scrollBy`函数滚动，改变的只有`getScrollX`的值，原始坐标(x,y)是不会改变的

3.`scrollTo`和`scrollBy`只移动其中的内容，不移动背景



`scrollTo`和`scrollBy`移动的时候，没有动画，要实现动画的过程，可借助`Scroller`



## `Scroller`使用方法

1.初始化

```java
public Scroller(Context context) 
public Scroller(Context context, Interpolator interpolator)
```

2.调用`startScroll`

```java
public void startScroll(int startX, int startY, int dx, int dy, int duration) 
```

这个函数是指定开始滚动的位置，和在X轴方向、Y轴方向的滚动距离。它的作用和`ValueAnimator`一样，只会根据插值器和起始、终止位置来计算当前应该移动到的位置并反馈给用户，只做数值计算，并不会真正的移动

```java
mScroller.startScroll(0, 0, -mScrollerWidth, 0, 500);
invalidate();
```

在调用`startScroll`后，需要调用`invalidate`函数来绘制View。

3.在`public void computeScroll()` 中处理计算出的数据

`computeScroll`是`View`的函数，当调用`invalidate`或者`postInvalidate`进行重绘时，就会调用`computeScroll`函数来重绘与`Scroller`有关的`View`的部分

```java
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
```

`computeScrollOffset()`方法，当Scroller还在计算中，表示当前控件还在滚动中，就会返回true。当Scroller计算结束，就会返回false。



## 例子

### 滑动开关

效果如下：

![151](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/151.gif)

`ToggleButton`代码如下：

```java
public class ToggleButton extends ViewGroup {
    private static final String TAG = "ToggleButton";

    /**
     * 滑块的宽度
     */
    private int mSliderWidth;

    /**
     * 滑块滚动到另一边的距离
     */
    private int mScrollerWidth;

    /**
     * 滑动处理类
     */
    private Scroller mScroller;

    /**
     * 表示滑块的开关状态
     */
    private boolean mIsOpen = false;

    public ToggleButton(Context context) {
        super(context);
        init(context);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);

        setBackgroundResource(R.mipmap.background);
        //滑块
        ImageView slider = new ImageView(context);
        slider.setImageResource(R.mipmap.slide);
        addView(slider);
        //点击事件
        slider.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "slider onClick");
                if (mIsOpen) {
                    mScroller.startScroll(-mScrollerWidth, 0, mScrollerWidth, 0, 500);
                } else {
                    mScroller.startScroll(0, 0, -mScrollerWidth, 0, 500);
                }
                mIsOpen = !mIsOpen;
                invalidate();
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //这里直接将大小设为背景图片的大小
        Drawable drawable = getResources().getDrawable(R.mipmap.background);
        Log.d(TAG, "width = " + drawable.getIntrinsicWidth() + ", height = " + drawable.getIntrinsicHeight());
        setMeasuredDimension(drawable.getIntrinsicWidth() * 2, drawable.getIntrinsicHeight() * 2);
    }

    /**
     * 在onLayout中设置滑块的位置
     * @param changed changed
     * @param l l
     * @param t t
     * @param r r
     * @param b b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mSliderWidth = getMeasuredWidth() / 2;
        mScrollerWidth = getMeasuredWidth() - mSliderWidth;
        //设置滑块ImageView的位置
        View view = getChildAt(0);
        view.layout(0, 0, mSliderWidth, getMeasuredHeight());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 记录上次的手指的x坐标
     * 1.在 onInterceptTouchEvent 和 onTouchEvent 中都记录
     * 原因是：第一次的action_down和action_move事件都走onInterceptTouchEvent
     * 拦截action_move事件后，后续的action_move事件只走onTouchEvent
     */
    private int mLastX;

    /**
     * 1.拦截action_move事件，让滑块随手指滑动
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent ev = " + ev.getAction());
        mLastX = (int) ev.getX();
        //拦截ACTION_MOVE事件，
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent ev = " + event.getAction());
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果滑动还没结束，就将其结束
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = mLastX - x;
                //边界判断，防止滑块越界
                if (getScrollX() + deltaX > 0) {
                    scrollTo(0, 0);
                    return true;
                } else if (getScrollX() + deltaX < -mScrollerWidth) {
                    scrollTo(-mScrollerWidth, 0);
                    return true;
                }
                scrollBy(deltaX, 0);
                break;
            case MotionEvent.ACTION_UP:
                //处理松手时自动滑动
                smoothScroll();
                break;
        }
        mLastX = x;
        return super.onTouchEvent(event);
    }

    /**
     * 处理松手时的自动滑动
     */
    private void smoothScroll() {
        //边界点位于整个滑块的1/4处
        int bound = -getMeasuredWidth() / 4; 
        int deltaX = 0;
        if (getScrollX() < bound) {
            deltaX = -mScrollerWidth - getScrollX();
            if (!mIsOpen) {
                mIsOpen = true;
            }
        }
        if (getScrollX() >= bound) {
            deltaX = -getScrollX();
            if (mIsOpen) {
                mIsOpen = false;
            }
        }
        mScroller.startScroll(getScrollX(), 0, deltaX, 0);
        invalidate();
    }
}
```

上面的例子在模拟器上运行似乎没什么问题，但在真机上运行却有点问题

在真机上运行，哪怕一个很小的点击事件（按下、抬起），也会被识别有action_move事件，导致`onClick`事件一直没被调用

我参考[在 ViewGroup 中管理轻触事件](https://developer.android.com/training/gestures/viewgroup)中的例子，对`onInterceptTouchEvent`进行修改，添加如下的判断：

```java
ViewConfiguration vc = ViewConfiguration.get(getContext());
mTouchSlop = vc.getScaledTouchSlop();
Log.d(TAG, "mTouchSlop = " + mTouchSlop);    

	public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent ev = " + ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = (int) ev.getX();
        }
        //拦截ACTION_MOVE事件
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            //diff
            final int xDiff = Math.abs((int) (ev.getX() - mLastX));
            if (xDiff > mTouchSlop) {
                return true;
            }
        }
        return false;
    }
```

貌似就OK了



































