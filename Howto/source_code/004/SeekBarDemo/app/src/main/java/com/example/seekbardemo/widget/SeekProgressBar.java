package com.example.seekbardemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * create by wangzhen 2021/10/11
 */
public class SeekProgressBar extends View {
    private Path mPathProgressBg;//进度条背景路径
    private Path mPathProgressFg;//进度条前景路径
    private Paint mPaintProgress;//绘制进度条的画笔
    private Paint mPaintRoundRect;//绘制显示进度的圆角矩形的画笔
    private Paint mPaintProgressText;//绘制显示进度文字的画笔
    private float mProgressHeight = 20;//进度条高度
    private PathMeasure mPathMeasure;
    private int mColorProgressBg = Color.BLACK;//进度条背景
    private int mColorProgressFg = Color.RED;//进度条前景
    private int mColorSeekGg = Color.RED;//拖拽的圆角矩形的背景颜色
    private float mProgress = 0.5f;//进度条进度
    private float mTextSize = 30;//进度条文字大小
    private Paint.FontMetricsInt mFontMetricsInt;//用于获取画笔绘制文字的参数
    private int mColorProgressText = Color.WHITE;//绘制文字的颜色
    private float mProgressStrMarginV = 10;//显示进度的文字与显示进度的圆角矩形垂直方向的边距
    private float mProgressStrMarginH = 20;//显示进度的文字与显示进度的圆角矩形水平方向的边距
    private float mRoundRectRadius = 10;//圆角矩形的圆角半径
    private RectF mProgressRoundRectF;//显示进度的圆角矩形（用于判断手指触摸的点是否在它的内部）
    private boolean mIsTouchSeek;
    private float mStartTouchX;
    private String mProgressMaxText = "100%";//绘制的文字的最大值（用于确定显示进度的矩形的宽高）
    public SeekProgressBar(Context context) {
        this(context,null);
    }

    public SeekProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SeekProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgress.setStrokeCap(Paint.Cap.ROUND);//设置线头为圆角
        mPaintProgress.setStyle(Paint.Style.STROKE);//设置绘制样式为线条
        mPaintProgress.setStrokeJoin(Paint.Join.ROUND);//设置拐角为圆角
        mPaintProgress.setStrokeWidth(mProgressHeight);

        mPaintRoundRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintRoundRect.setStyle(Paint.Style.FILL);
        mPaintRoundRect.setColor(mColorSeekGg);

        mPaintProgressText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgressText.setStrokeWidth(1);
        mPaintProgressText.setStyle(Paint.Style.FILL);
        mPaintProgressText.setColor(mColorProgressText);
        mPaintProgressText.setTextSize(mTextSize);//设置字体大小
        mPaintProgressText.setTextAlign(Paint.Align.CENTER);//将文字水平居中
        mFontMetricsInt = mPaintProgressText.getFontMetricsInt();

        mPathProgressBg = new Path();
        mPathProgressFg = new Path();
        mPathMeasure = new PathMeasure();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSizeWidth(widthMeasureSpec), measureSizeHeight(heightMeasureSpec));
    }

    //计算宽度
    private int measureSizeWidth(int size) {
        int mode = MeasureSpec.getMode(size);
        int s = MeasureSpec.getSize(size);
        if (mode == MeasureSpec.EXACTLY) {
            return s;
        } else{
            return Math.min(s, 200);
        }
    }
    //计算高度
    private int measureSizeHeight(int size) {
        int mode = MeasureSpec.getMode(size);
        int s = MeasureSpec.getSize(size);
        if (mode == MeasureSpec.EXACTLY) {
            return s;
        }else {
            //自适应模式，返回所需的最小高度
            return (int) (mTextSize + mProgressStrMarginV * 2);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //进度条绘制在控件中央,宽度为控件宽度(mProgressHeight/2是为了显示出左右两边的圆角)
        mPathProgressBg.moveTo(mProgressHeight / 2,h / 2f);
        mPathProgressBg.lineTo(w - mProgressHeight / 2,h / 2f);
        //将进度条路径设置给PathMeasure
        mPathMeasure.setPath(mPathProgressBg,false);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //判断手指是否触摸了显示进度的圆角矩形块，这样才可以拖拽
                if(mProgressRoundRectF != null && mProgressRoundRectF.contains(event.getX(),event.getY())){
                    //记录手指刚接触屏幕的X轴坐标（因为只需要在X轴上平移）
                    mStartTouchX = event.getX();
                    mIsTouchSeek = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mIsTouchSeek){
                    //计算横向移动的距离
                    float moveX = event.getX() - mStartTouchX;
                    //计算出当前进度的X轴所显示的进度长度
                    float currentProgressWidth = mPathMeasure.getLength() * mProgress;//计算进度条的进度
                    //计算滑动后的X轴的坐标
                    float showProgressWidth = currentProgressWidth + moveX;
                    //计算边界值
                    if(showProgressWidth < 0){
                        showProgressWidth = 0;
                    }else if(showProgressWidth > mPathMeasure.getLength()){
                        showProgressWidth = mPathMeasure.getLength();
                    }
                    //计算滑动后的进度
                    mProgress = showProgressWidth / mPathMeasure.getLength();
                    //重绘
                    invalidate();
                    //刷新用于计算移动的X轴坐标
                    mStartTouchX = event.getX();
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsTouchSeek = false;
                break;
        }
        return mIsTouchSeek;
    }

    /**
     * 设置进度
     */
    public void setProgress(float progress){
        this.mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制进度条
        drawProgress(canvas);
        //绘制进度显示的圆角矩形
        drawShowProgressRoundRect(canvas);
        //绘制进度
        drawProgressText(canvas);
    }

    private void drawProgressText(Canvas canvas) {
        String progressText = (int)Math.floor(100 * mProgress) + "%";
        //让文字垂直居中的偏移
        int offsetY = (mFontMetricsInt.bottom - mFontMetricsInt.ascent) / 2 - mFontMetricsInt.bottom;
        //将文字绘制在矩形的中央
        canvas.drawText(progressText,mProgressRoundRectF.centerX(),mProgressRoundRectF.centerY() + offsetY,mPaintProgressText);
    }

    private void drawShowProgressRoundRect(Canvas canvas) {
        float stop = mPathMeasure.getLength() * mProgress;//计算进度条的进度
        //根据要绘制的文字的最大长宽来计算要绘制的圆角矩形的长宽
        Rect rect = new Rect();
        mPaintProgressText.getTextBounds(mProgressMaxText,0, mProgressMaxText.length(),rect);
        //要绘制矩形的宽、高
        float rectWidth = rect.width() + (mProgressStrMarginH * 2);
        float rectHeight = rect.height() + (mProgressStrMarginV * 2);
        //计算边界值（为了不让矩形在左右两边超出边界）
        if(stop < rectWidth / 2f){
            stop = rectWidth / 2f;
        }else if(stop > (getWidth() - rectWidth / 2f)){
            stop = getWidth() - rectWidth / 2f;
        }
        //定义绘制的矩形
        float left = stop - rectWidth / 2f;
        float right = stop + rectWidth / 2f;
        float top = getHeight() / 2f - rectHeight / 2f;
        float bottom = getHeight() / 2f + rectHeight / 2f;
        mProgressRoundRectF = new RectF(left,top,right,bottom);
        //绘制为圆角矩形
        canvas.drawRoundRect(mProgressRoundRectF, mRoundRectRadius, mRoundRectRadius,mPaintRoundRect);
    }

    private void drawProgress(Canvas canvas) {
        mPathProgressFg.reset();
        mPaintProgress.setColor(mColorProgressBg);
        //绘制进度背景
        canvas.drawPath(mPathProgressBg, mPaintProgress);
        //计算进度条的进度
        float stop = mPathMeasure.getLength() * mProgress;
        //得到与进度对应的路径
        mPathMeasure.getSegment(0,stop,mPathProgressFg,true);
        mPaintProgress.setColor(mColorProgressFg);
        //绘制进度
        canvas.drawPath(mPathProgressFg, mPaintProgress);
    }
}