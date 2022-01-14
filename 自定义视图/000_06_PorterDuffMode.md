# PorterDuffMode

参考：

+ [HenCoder Android 开发进阶: 自定义 View 1-2 Paint 详解](https://rengwuxian.com/ui-1-2/)



在使用Xfermode时，一般需要做如下的两件事

1.禁用硬件加速

```java
setLayerType(LAYER_TYPE_SOFTWARE, null);
```

2.使用离屏绘制

```java
int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

//核心绘制代码

canvas.restoreToCount(layerId);
```



关于`PorterDuff.Mode`的原理，可参考如下的文章：

+ [各个击破搞明白PorterDuff.Mode - 简书](https://www.jianshu.com/p/d11892bbe055)

+  [What does PorterDuff.Mode mean in android graphics.What does it do?](https://stackoverflow.com/questions/8280027/what-does-porterduff-mode-mean-in-android-graphics-what-does-it-do)

也可参考官方文档[PorterDuff.Mode](https://developer.android.com/reference/android/graphics/PorterDuff.Mode.html)

其基本使用模式如下：

```java
Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

...

canvas.drawBitmap(rectBitmap, 0, 0, paint); // 画方
paint.setXfermode(xfermode); // 设置 Xfermode
canvas.drawBitmap(circleBitmap, 0, 0, paint); // 画圆
paint.setXfermode(null); // 用完及时清除 Xfermode
```

目标图像与源图像合成效果如下，借用一张图：

![162](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/162.png)

那什么是源图像和目标图像呢？

参考官方文档中的例子：

```java
 Paint paint = new Paint();
 canvas.drawBitmap(destinationImage, 0, 0, paint);

 PorterDuff.Mode mode = // choose a mode
 paint.setXfermode(new PorterDuffXfermode(mode));

 canvas.drawBitmap(sourceImage, 0, 0, paint);
 
```

本人的理解是，第一个绘制的是目标图像，第二个绘制的是源图像

> 在Xfermode设置前画出的图像叫做目标图像，即给谁应用Xfermode；
> 
> 在Xfermode设置后画出的图像叫做源图像，即拿什么应用Xfermode



如下的例子，模式为`PorterDuff.Mode.SRC_IN`

```java
public class PorterDuffXfermodeView extends View {
    private int width = 200;
    private int height = 200;
    private Bitmap dstBmp;
    private Bitmap srcBmp;
    private Paint mPaint;

    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        dstBmp = makeDst(width, height);
        srcBmp = makeSrc(width, height);
        mPaint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(dstBmp, 0, 0, mPaint);//圆形
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(srcBmp, width / 2, height / 2, mPaint);//方形
        mPaint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }

    private Bitmap makeDst(int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCC44);
        canvas.drawOval(new RectF(0, 0, w, h), p); //圆形
        return bitmap;
    }

    private Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(0, 0, w, h, p); //方形
        return bm;
    }
}
```

![163](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/163.png)



**其它相关模式**

1.Mode.ADD(饱和度相加)

ADD模式就是对SRC与DST两张图片相交区域的饱和度进行相加

![164](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/164.png)



2.Mode.LIGHTEN(变亮)

![165](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/165.png)

如下例子，书架+灯光的例子：

```java
public class LightBookView extends View {
    private Paint mBitPaint;
    private Bitmap BmpDST,BmpSRC;

    public LightBookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBitPaint = new Paint();
        BmpDST = BitmapFactory.decodeResource(getResources(), R.drawable.book_bg, null);
        BmpSRC = BitmapFactory.decodeResource(getResources(),R.drawable.book_light,null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        canvas.drawBitmap(BmpDST,0,0,mBitPaint);
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
        canvas.drawBitmap(BmpSRC,0,0,mBitPaint);

        mBitPaint.setXfermode(null);
        canvas.restore();
    }
}
```

![166](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/166.png)



3.Mode.MULTIPLY(正片叠色)

![167](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/167.png)



如下的例子，Twitter表示的描边效果：

![168](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/168.png)

```java
public class TwitterView extends View {
    private Paint mBitPaint;
    private Bitmap BmpDST,BmpSRC;
    public TwitterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mBitPaint = new Paint();
        BmpDST = BitmapFactory.decodeResource(getResources(), R.drawable.twiter_bg, null);
        BmpSRC = BitmapFactory.decodeResource(getResources(),R.drawable.twiter_light,null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        canvas.drawBitmap(BmpDST,0,0,mBitPaint);
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvas.drawBitmap(BmpSRC,0,0,mBitPaint);

        mBitPaint.setXfermode(null);
        canvas.restore();
    }
}
```



## 源图像模式

1.Mode.SRC

在处理源图像所在区域的相交问题时，全部以源图像显示

![169](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/169.png)



2.Mode.SRC_IN

上面尝试过，效果如下：

![163](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/163.png)



> SRC_IN模式是在相交时利用目标图像的透明度来改变源图像的透明度和饱和度的
> 
> 当目标图像的透明度为0时，源图像就完全不显示了



利用该特性，可实现许多的效果，如圆角效果和图片倒影效果

参考：

+ [Android图形处理--PorterDuff.Mode那些事儿](https://blog.csdn.net/HardWorkingAnt/article/details/78045232)



a.实现圆角效果

```java
public class SRCINView extends View {
    private Paint mPaint;

    public SRCINView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(LAYER_TYPE_SOFTWARE, null); //关闭硬件加速

        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.dog);

        int width = getWidth() / 2;
        int height = width * src.getHeight() / src.getWidth();

        int radius = Math.min(width,height) / 2;
        canvas.drawCircle(width / 2, height / 2, radius, mPaint);

        //设置Xfermode
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //源图，图太大了，缩小绘制
        canvas.drawBitmap(src, null, new Rect(0, 0, width, height), mPaint);

        //还原Xfermode
        mPaint.setXfermode(null);
    }
}
```

![170](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/170.png)



b.实现倒影效果

```java
public class InvertedImageView_SRCIN extends View {
    private Paint mBitPaint;
    private Bitmap BmpDST, BmpSRC, BmpRevert;

    public InvertedImageView_SRCIN(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBitPaint = new Paint();
        BmpDST = BitmapFactory.decodeResource(getResources(), R.drawable.dog_invert_shade, null);
        BmpSRC = BitmapFactory.decodeResource(getResources(), R.drawable.dog, null);

        Matrix matrix = new Matrix();
        matrix.setScale(1F, -1F);
        // 生成倒影图
        BmpRevert = Bitmap.createBitmap(BmpSRC, 0, 0, BmpSRC.getWidth(), BmpSRC.getHeight(), matrix, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth() * 2 / 3;
        int height = width * BmpDST.getHeight() / BmpDST.getWidth();

        //先画出小狗图片
        canvas.drawBitmap(BmpSRC, null, new RectF(0, 0, width, height), mBitPaint);

        //再画出倒影
        canvas.save();
        canvas.translate(0, height);

        canvas.drawBitmap(BmpDST, null, new RectF(0, 0, width, height), mBitPaint);
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(BmpRevert, null, new RectF(0, 0, width, height), mBitPaint);

        mBitPaint.setXfermode(null);

        canvas.restore();
    }
}
```

![171](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/171.png)



3.Mode.SRC_OUT

![172](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/172.png)

当目标图像完全不透明时，计算结果将是透明的

a.橡皮擦效果

> 这里需要将手势轨迹对应的小狗图像隐藏，所以小狗图像时源图像，用于显示；而将手势轨迹所在的图像作为目标图像，用于控制哪部分小狗图像显示。



```java
public class EraserView_SRCOUT extends View {
    private Paint mBitPaint;
    private Bitmap BmpDST, BmpSRC, BmpText;
    private Path mPath;
    private float mPreX, mPreY;

    public EraserView_SRCOUT(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBitPaint = new Paint();
        mBitPaint.setColor(Color.RED);
        mBitPaint.setStyle(Paint.Style.STROKE);
        mBitPaint.setStrokeWidth(45);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        BmpText = BitmapFactory.decodeResource(getResources(), R.drawable.guaguaka_text, null);
        BmpSRC = BitmapFactory.decodeResource(getResources(), R.drawable.dog, options);
        BmpDST = Bitmap.createBitmap(BmpSRC.getWidth(), BmpSRC.getHeight(), Bitmap.Config.ARGB_8888);
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(BmpText, null, new RectF(0, 0, BmpDST.getWidth(), BmpDST.getHeight()), mBitPaint);

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

        //先把手指轨迹画到目标Bitmap上
        Canvas c = new Canvas(BmpDST);
        c.drawPath(mPath, mBitPaint);

        //然后把目标图像画到画布上
        canvas.drawBitmap(BmpDST, 0, 0, mBitPaint);

        //计算源图像区域
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(BmpSRC, 0, 0, mBitPaint);

        mBitPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                mPreX = event.getX();
                mPreY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX = (mPreX + event.getX()) / 2;
                float endY = (mPreY + event.getY()) / 2;
                mPath.quadTo(mPreX, mPreY, endX, endY);
                mPreX = event.getX();
                mPreY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }
}
```

![173](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/173.png)



## 目标图像模式

在于SRC相关的模式中，在处理相交区域时，优先以源图像显示为主；而在于DST相关的模式中，在处理相交区域时，优先以目标图像显示为主



1.Mode.DST_IN

`Mode.DST_IN`在相交时，利用源图像的透明度来改变目标图像的透明度和饱和度

a.圆角效果

利用SRC模式实现的效果，只需将源图像和目标图像颠倒，利用对应的DST模式就可以实现同样的效果

```java
public class RoundImageView_SRCIN extends View {
    private Paint mBitPaint;
    private Bitmap BmpDST,BmpSRC;

    public RoundImageView_SRCIN(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBitPaint = new Paint();
        //BmpDST = BitmapFactory.decodeResource(getResources(), R.drawable.dog_shade, null);
        //BmpSRC = BitmapFactory.decodeResource(getResources(),R.drawable.dog,null);
        BmpDST = BitmapFactory.decodeResource(getResources(), R.drawable.dog, null);
        BmpSRC = BitmapFactory.decodeResource(getResources(),R.drawable.dog_shade,null);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth()/2;
        int height = width * BmpDST.getHeight()/BmpDST.getWidth();

        canvas.save();

        canvas.drawBitmap(BmpDST,null,new RectF(0,0,width,height),mBitPaint);
        //mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mBitPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(BmpSRC,null,new RectF(0,0,width,height),mBitPaint);

        mBitPaint.setXfermode(null);
        canvas.restore();
    }
}
```

![174](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/174.png)



b.区域波纹

![175](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/175.png)

> 波纹是目标图像，文字遮罩是源图像

```java
public class TextWave_DSTIN extends View {
    private Paint mPaint;
    private Path mPath;
    private int mItemWaveLength = 1000;
    private int dx;

    private Bitmap BmpSRC, BmpDST;

    public TextWave_DSTIN(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        BmpSRC = BitmapFactory.decodeResource(getResources(), R.drawable.text_shade, null);
        BmpDST = Bitmap.createBitmap(BmpSRC.getWidth(), BmpSRC.getHeight(), Bitmap.Config.ARGB_8888);

        startAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        generateWavePath();

        //先清空bitmap上的图像,然后再画上Path
        Canvas c = new Canvas(BmpDST);
        //每次都会在BmpDST上绘制波纹，所以在绘制新的波纹前，需要先清空图像
        c.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        c.drawPath(mPath, mPaint);

        //在合成前，先利用canvas.drawBitmap(BmpSRC,0,0,mPaint)将文字绘制出来
        canvas.drawBitmap(BmpSRC, 0, 0, mPaint);
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(BmpDST, 0, 0, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(BmpSRC, 0, 0, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    /**
     * 生成此时的Path
     */
    private void generateWavePath() {
        mPath.reset();
        int originY = BmpSRC.getHeight() / 2;
        int halfWaveLen = mItemWaveLength / 2;
        mPath.moveTo(-mItemWaveLength + dx, originY);
        for (int i = -mItemWaveLength; i <= getWidth() + mItemWaveLength; i += mItemWaveLength) {
            mPath.rQuadTo(halfWaveLen / 2, -50, halfWaveLen, 0);
            mPath.rQuadTo(halfWaveLen / 2, 50, halfWaveLen, 0);
        }
        mPath.lineTo(BmpSRC.getWidth(), BmpSRC.getHeight());
        mPath.lineTo(0, BmpSRC.getHeight());
        mPath.close();
    }

    public void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mItemWaveLength);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (Integer) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}
```

![176](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/176.png)






























