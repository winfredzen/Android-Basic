# VelocityTracker

[VelocityTracker](https://developer.android.com/reference/android/view/VelocityTracker)官方文档的解释：

> Helper for tracking the velocity of touch events, for implementing flinging and other such gestures. Use `obtain()` to retrieve a new instance of the class when you are going to begin tracking. Put the motion events you receive into it with `addMovement(android.view.MotionEvent)`. When you want to determine the velocity call `computeCurrentVelocity(int)` and then call `getXVelocity(int)` and `getYVelocity(int)` to retrieve the velocity for each pointer id.
>
> [VelocityTracker](https://developer.android.com/reference/android/view/VelocityTracker.html) 是一个跟踪触摸事件滑动速度的帮助类，用于实现flinging以及其它类似的手势。它的原理是把触摸事件 MotionEvent 对象传递给VelocityTracker的[addMovement(MotionEvent)](https://developer.android.com/reference/android/view/VelocityTracker.html#addMovement(android.view.MotionEvent))方法，然后分析MotionEvent 对象在单位时间类发生的位移来计算速度。你可以使用getXVelocity() 或getXVelocity()获得横向和竖向的速率到速率时，但是使用它们之前请先调用computeCurrentVelocity(int)来初始化速率的单位 。



```java
public void computeCurrentVelocity(int units)
```

方法中，units的含义是毫秒

> The units you would like the velocity in.  A value of 1 provides pixels per millisecond, 1000 provides pixels per second, etc.

比如1000ms时，在1s内，手指在水平方向从左向右滑动100像素，那么水平速度就是100.



如下的例子：

```java
public class VelocityTrackerTest extends AppCompatActivity {

    private TextView mInfo;

    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;

    private int mPointerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVelocityTracker = VelocityTracker.obtain();
        mMaxVelocity = ViewConfiguration.get(this).getMaximumFlingVelocity();

        mInfo = new TextView(this);
        mInfo.setLines(4);
        mInfo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mInfo.setTextColor(Color.BLACK);
        setContentView(mInfo);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        mVelocityTracker = getVelocityTracker();
        mVelocityTracker.addMovement(event);

        final VelocityTracker verTracker = mVelocityTracker;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //求第一个触点的id， 此时可能有多个触点，但至少一个
                mPointerId = event.getPointerId(0);
                break;

            case MotionEvent.ACTION_MOVE:
                //求伪瞬时速度
                verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityX = verTracker.getXVelocity(mPointerId);
                final float velocityY = verTracker.getYVelocity(mPointerId);
                recodeInfo(velocityX, velocityY);
                break;

            case MotionEvent.ACTION_UP:
                releaseVelocityTracker();
                break;

            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private VelocityTracker getVelocityTracker() {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        return mVelocityTracker;
    }

    //释放VelocityTracker
    private void releaseVelocityTracker() {
        if(null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private static final String sFormatStr = "velocityX=%f\nvelocityY=%f";

    /**
     * 记录当前速度
     *
     * @param velocityX x轴速度
     * @param velocityY y轴速度
     */
    private void recodeInfo(final float velocityX, final float velocityY) {
        final String info = String.format(sFormatStr, velocityX, velocityY);
        mInfo.setText(info);
    }
}
```

![050](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/050.png)
