# 自定义SeekBar



## 基本

自定义`SeekBar`，可以自定义`SeekBar`的`thumb`，背景颜色等

如下的设置：

```xml
    <androidx.appcompat.widget.AppCompatSeekBar
        android:layout_marginTop="50dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/seekbar01"
        android:thumb="@drawable/seekbar_thumb"
        android:progressDrawable="@drawable/seekbar_progress_style"
        android:progress="50"
        />
```

`seekbar_thumb.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="#FF0000">
    <item>
        <shape android:shape="oval">
            <solid android:color="#FF0000"/>
            <size android:width="30dp" android:height="30dp"/>
        </shape>
    </item>
</ripple>
```

`seekbar_progress_style.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:id="@android:id/background"
        android:drawable="@drawable/seekbar_track">
    </item>

    <item android:id="@android:id/progress">
        <clip android:drawable="@drawable/seekbar_progress"/>
    </item>
</layer-list>
```

`seekbar_track.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape>
            <corners android:radius="5dp"/>
            <gradient
                android:angle="270"
                android:centerColor="#0000FF"
                android:endColor="#00FF00"
                android:startColor="#FF0000"
                android:type="linear"/>
        </shape>
    </item>
</layer-list>
```

`seekbar_progress.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/progressshape">
        <clip>
            <shape android:shape="rectangle">
                <size android:height="5dp"/>
                <corners android:radius="5dp"/>
                <solid android:color="#3db5ea"/>
            </shape>
        </clip>
    </item>
</layer-list>
```

最终的效果如下：

![012](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/012.png)



在上面的例子中有使用到`<clip>`标签，有什么用呢？看如下的例子，一个渐变的seekbar

```xml
    <SeekBar
        android:layout_marginTop="50dp"
        android:layout_width="match_parent"
        android:layout_height="68px"
        android:progressDrawable="@drawable/seekbar_progress_drawable"
        android:thumb="@null"
        android:splitTrack="false"
        android:duplicateParentState="true"
        />
```

`seekbar_progress_drawable.xml`内容如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <!--定义seekbar滑动条的底色-->
    <item android:id="@android:id/background">
        <shape>
            <corners android:radius="10px" />
            <solid android:color="#33748CA3" />
        </shape>
    </item>

    <!--定义seekbar滑动条进度颜色-->
    <item android:id="@android:id/progress">
        <clip>
            <shape>
                <corners android:radius="10px" />
                <gradient
                    android:endColor="#D7E4F1"
                    android:startColor="#677d90" />
            </shape>
        </clip>
    </item>
</layer-list>
```

![026](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/026.png)

如果没有`<clip>`标签，则显示效果如下(此时进度只有50%，但显示的还是100%):

![027](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/027.png)





## 自定义

在网上看了2个例子

1.[Android自定义view实现SeekBar](https://www.jianshu.com/p/9fdb251c76f3)

这个是直接继承`SeekBar`，感觉效果一般般



2.[Android自定义View实现可拖拽的进度条](https://www.jianshu.com/p/4f4e30953739)

这个继承自`View`，比较有借鉴的意义

```java
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
```

![013](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/013.png)



















