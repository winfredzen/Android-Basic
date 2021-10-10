package com.example.seekbardemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * create by wangzhen 2021/10/10
 */
public class CustomSeekBar extends SeekBar {
    //背景画笔
    private Paint mBackgroundPaint;
    //进度画笔
    private Paint mProgressPaint;
    //第二进度画笔
    private Paint mSecondProgressPaint;
    //游标画笔
    private Paint mThumbPaint;

    public CustomSeekBar(Context context) {
        super(context);
        init(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setDither(true);//防抖动
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.parseColor("#e5e5e5"));

        mProgressPaint = new Paint();
        mProgressPaint.setDither(true);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.parseColor("#0288d1"));

        mSecondProgressPaint = new Paint();
        mSecondProgressPaint.setDither(true);
        mSecondProgressPaint.setAntiAlias(true);
        mSecondProgressPaint.setColor(Color.parseColor("#b8b8b8"));

        mThumbPaint = new Paint();
        mThumbPaint.setDither(true);
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setColor(Color.parseColor("#0288d1"));
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        int rSize = getHeight() / 4;
        int height = getHeight() / 4 / 3;
        int leftPadding = rSize;

        RectF backgroundRect = new RectF(leftPadding,
                getHeight() / 2 - height,
                getWidth(),
                getHeight() / 2 + height);
        canvas.drawRoundRect(backgroundRect, rSize, rSize, mBackgroundPaint);

    }
}


























