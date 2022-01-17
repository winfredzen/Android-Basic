package com.getui.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Time：2019/9/20 on 4:24 PM.
 * Description:.
 * Author:jimlee.
 */
public class FlowLayoutView extends FrameLayout {

    private int measuredHeight; // 测量得到的高度
    private int realHeight; // 整个流式布局控件的实际高度


    public FlowLayoutView(Context context) {
        this(context, null);
    }

    public FlowLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int currentWidth = getPaddingLeft();
        int currentHeight = getPaddingTop();
        int maxChildHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int marginleft = lp.leftMargin;
                int marginRight = lp.rightMargin;
                int marginTop = lp.topMargin;
                int marginBottom = lp.bottomMargin;
                int childMeasuredWidth = child.getMeasuredWidth();
                int childMeasuredHeight = child.getMeasuredHeight();
                if (currentWidth + childMeasuredWidth + marginleft < getWidth() - getPaddingRight()) {  //判断是否需要换行，忽略子控件自身的marginRight
                    t = currentHeight + marginTop;
                    l = currentWidth + marginleft;
                    r = l + childMeasuredWidth;
                    b = t + childMeasuredHeight;
                    childMeasuredHeight = childMeasuredHeight + marginBottom + marginTop; //测量高度= 控件高度+ marginTop+marginBottom

                } else {
                    currentWidth = getPaddingLeft();
                    currentHeight = currentHeight + maxChildHeight;
                    maxChildHeight = 0;
                    t = currentHeight + marginTop;
                    l = currentWidth + marginleft;
                    r = l + childMeasuredWidth;
                    b = t + childMeasuredHeight;
                    childMeasuredHeight = childMeasuredHeight + marginBottom + marginTop;  //测量高度= 控件高度+ marginTop+marginBottom
                }

                // 更新最大高度
                maxChildHeight = Math.max(maxChildHeight, childMeasuredHeight);

                currentWidth += childMeasuredWidth + marginleft + marginRight; //测量高度= 控件高度+ marginRight+marginLeft
                child.layout(l, t, r, b);
            }

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int flowLayoutWidth = 0;  //布局最大宽度
        int flowLayoutHeight = 0;//布局最大高度
        int childCount = getChildCount();
        int currentWidth = getPaddingLeft();
        int maxChildHeight = 0;  //最大的子View高度
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        }
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childMeasuredWidth = child.getMeasuredWidth();
            int childMeasuredHeight = child.getMeasuredHeight();
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int marginleft = lp.leftMargin;
            int marginRight = lp.rightMargin;
            int marginTop = lp.topMargin;
            int marginBottom = lp.bottomMargin;
            if (childMeasuredWidth + marginleft + marginRight >= widthSize - getPaddingRight() - getPaddingLeft()) {  //判断当前子控件是否充满布局
                flowLayoutWidth = widthSize;
                flowLayoutHeight += childMeasuredHeight + marginBottom + marginTop;
                currentWidth = getPaddingLeft();
                maxChildHeight = 0;
                Log.d("TAG", "FULL layout");
            } else if (currentWidth + childMeasuredWidth + marginleft >= widthSize - getPaddingRight()) {  //判断是否换行
                flowLayoutWidth = Math.max(currentWidth + getPaddingRight(), flowLayoutWidth);
                flowLayoutHeight += maxChildHeight;
                currentWidth = getPaddingLeft();
                maxChildHeight = childMeasuredHeight + marginBottom + marginTop;
                Log.d("TAG", "Change Line");
            } else {
                maxChildHeight = Math.max(maxChildHeight, childMeasuredHeight + marginBottom + marginTop);
            }
            currentWidth += childMeasuredWidth + marginleft + marginRight;
        }


        // 得到最终的宽高
        // 宽度：如果是EXACTLY模式，则遵循测量值，否则使用我们计算得到的宽度值
        // 高度：只要布局中内容的高度大于测量高度，就使用内容高度（无视测量模式）；否则才使用测量高度
        int width = widthMode == MeasureSpec.EXACTLY ? widthSize : flowLayoutWidth + getPaddingLeft() + getPaddingRight();
        realHeight = flowLayoutHeight + maxChildHeight + getPaddingTop() + getPaddingBottom();
        if (heightMode == MeasureSpec.EXACTLY) {
            realHeight = Math.max(measuredHeight, realHeight);
        }
        // 设置最终的宽高
        setMeasuredDimension(width, realHeight);
    }


}
