package com.wz.numberpickertest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;

/**
 * create by wangzhen 2023/4/25
 */
public class MyNumberPicker extends LinearLayout {

    private static final String TAG = "MyNumberPicker";

    /**
     * The coefficient by which to adjust (divide) the max fling velocity.
     */
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;

    /**
     * The strength of fading in the top and bottom while drawing the selector.
     */
    private static final float TOP_AND_BOTTOM_FADING_EDGE_STRENGTH = 0.9f;

    Paint mPaint;

    private float mLastDownOrMoveEventY;

    private int mCurrentScrollOffset;

    private VelocityTracker mVelocityTracker;

    private int mMaximumFlingVelocity;

    private int mMinimumFlingVelocity;

    private int mTouchSlop;

    /**
     * The previous Y coordinate while scrolling the selector.
     */
    private int mPreviousScrollerY;

    private Scroller mFlingScroller;

    public MyNumberPicker(Context context) {
        this(context, null);
    }

    public MyNumberPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyNumberPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.d(TAG, "MyNumberPicker <init>");

        setWillNotDraw(false);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(60);
        mPaint = paint;

        // initialize constants
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity()
                / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT;

        mFlingScroller = new Scroller(getContext(), null, true);

        mCurrentScrollOffset = 200;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d(TAG, "onLayout");

        initializeFadingEdges();
    }

    private void initializeFadingEdges() {
        Log.d(TAG, "initializeFadingEdges");
        setVerticalFadingEdgeEnabled(true);
        setFadingEdgeLength(200);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");

        int y = mCurrentScrollOffset;
        canvas.drawText("床前明月光，疑是地上霜", 10, y, mPaint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                mLastDownOrMoveEventY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                //移动的距离
                float currentMoveY = event.getY();
                int deltaMoveY = (int) ((currentMoveY - mLastDownOrMoveEventY));
                scrollBy(0, deltaMoveY);
                invalidate();
                mLastDownOrMoveEventY = currentMoveY;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                //flying
                VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();
                if (Math.abs(initialVelocity) > mMinimumFlingVelocity) {
                    fling(initialVelocity);
                }

                break;
            default:
                break;
        }


        return super.onTouchEvent(event);
    }

    @Override
    public void scrollBy(int x, int y) {
        Log.d(TAG, "scrollBy y = " + y);
        mCurrentScrollOffset += y;

    }

    /**
     * Flings the selector with the given <code>velocityY</code>.
     */
    private void fling(int velocityY) {
        Log.d(TAG, "fling velocityY = " + velocityY);

        mPreviousScrollerY = 0;
        if (velocityY > 0) {
            mFlingScroller.fling(0, 0, 0, velocityY, 0, 0, 0, Integer.MAX_VALUE);
        } else {
            mFlingScroller.fling(0, Integer.MAX_VALUE, 0, velocityY, 0, 0, 0, Integer.MAX_VALUE);
        }
        invalidate();
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.d(TAG, "computeScroll");

        Scroller scroller = mFlingScroller;
        if (scroller.computeScrollOffset()) {
            int currentScrollerY = scroller.getCurrY();
            if (mPreviousScrollerY == 0) {
                mPreviousScrollerY = scroller.getStartY();
            }
            scrollBy(0, currentScrollerY - mPreviousScrollerY);
            mPreviousScrollerY = currentScrollerY;
            invalidate();
        }
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH;
    }
    @Override
    protected float getBottomFadingEdgeStrength() {
        return TOP_AND_BOTTOM_FADING_EDGE_STRENGTH;
    }

    @Override
    public int getSolidColor() {
        return Color.parseColor("#555555");
    }

}
















