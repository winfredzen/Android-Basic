# Paint&Canvas

**`setStyle(Style style)` - 设置样式**

> Style是一个枚举，包括的值有：
>
> + `FILL` - 填充
> + `STROKE` - 描边
> + `FILL_AND_STROKE` - 填充和描边

```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(0xFFFF0000);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(50);

        canvas.drawCircle(200, 200, 150, paint);
    }
```

分别设置的效果：

![007](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/007.png)

![008](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/008.png)

![009](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/009.png)



**设置抗锯齿**

```java
void setAntiAlias(boolean aa)
```

> 在打开抗锯齿的情况下，可以产生平滑的边缘



**设置颜色**

```java
setColor(int color)
```

> 颜色格式为0xAARRGGBB



**设置描边宽度**

```java
setStrokeWidth(float width) 
```

> 单位为像素



**其它一些方法**

```java
reset()
```

> 重置画笔

```java
setARGB(int a, int r, int g, int b)
```

> 设置颜色

```java
setAlpha(int a)
```

> 设置画笔透明度

```java
setStrokeMiter(float miter)
```

> 设置画笔倾斜度。90度拿笔和30度拿笔，画出来的线条样式肯定是不一样的。该函数并没有太大的作用，基本看不出区别
>
> Set the paint's stroke miter(斜接) value. This is used to control the behavior of miter joins when the joins angle is sharp. This value must be >= 0.

```java
setPathEffect(PathEffect effect)
```

> 设置路径样式
>
> ![093](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/093.png)

```java
setStrokeCap(Cap cap)
```

> 设置线帽样式

```java
setStrokeJoin(Join join)
```

> 设置路径的转角样式

```java
setDither(boolean dither)
```

> 设置在绘制图像时的抗抖动效果。



## Canvas使用基础



### 画布背景颜色

有三种方法：

```java
void drawColor(@ColorInt int color)
void drawARGB(int a, int r, int g, int b)
void drawRGB(int r, int g, int b)
```

如：

```java
@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawRGB(255, 0, 255);
}
```

![018](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/018.png)



### 画直线

```java
void drawLine(float startX, float startY, float stopX, float stopY,
            @NonNull Paint paint)
```

如：

```java
@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Paint paint = new Paint();
    paint.setColor(Color.RED);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(50);
    canvas.drawLine(100, 100, 300, 300, paint);
}
```

![019](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/019.png)



### 画多条直线

```java
void drawLines(float[] pts, Paint paint)
```

如：

```java
@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Paint paint = new Paint();
    paint.setStrokeWidth(5);
    float[] pts = {10, 10, 100, 100, 200, 200, 300, 300};
    canvas.drawLines(pts, paint);
}
```

![020](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/020.png)





### 点

```java
//一个点
void drawPoint(float x, float y, Paint paint)
//多个点
void drawPoints(float[] pts, Paint paint)
void drawPoints(float[] pts, int offset, int count, Paint paint)  
```



### Rect、RectF

`RectF`用来保存`Float`类型的矩形，`Rect`用来保存`int`类型的矩形

**绘制圆角矩形**

```java
void drawRoundRect(@NonNull RectF rect, float rx, float ry, @NonNull Paint paint)
```

+ rx - 椭圆的x轴半径
+ ry - 椭圆的y轴半径

![021](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/021.png)

如下：

```java
@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
   Paint paint = new Paint();
   paint.setColor(Color.RED);
   paint.setStyle(Paint.Style.FILL);
   paint.setStrokeWidth(5);
   canvas.drawRoundRect(new RectF(100, 10, 400, 200), 30, 60, paint);
}
```

![022](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/022.png)





### 圆形

```java
void drawCircle(float cx, float cy, float radius, @NonNull Paint paint)
```

+ cx - 圆心x坐标
+ cy - 圆心y坐标
+ radius - 半径



### 椭圆

```java
void drawOval(@NonNull RectF oval, @NonNull Paint paint)
```

利用矩形生成椭圆

![023](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/023.png)



### 圆弧

```java
void drawArc(@NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter,
            @NonNull Paint paint)
```

+ startAngle - 起始角度，以X轴正方向为0度
+ useCenter - 是否有弧的2边，为true时，带有2边，为false时，只有1边



```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        RectF rect1 = new RectF(10, 10, 100, 100);
        canvas.drawArc(rect1, 0, 90, true, paint);

        RectF rect2 = new RectF(110, 10, 200, 100);
        canvas.drawArc(rect2, 0, 90, false, paint);

    }
```

![024](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/024.png)



## Rect与RectF

### contains

`contains`用来判断rect是否包含某个点，用于判断点是否在矩形中

```java
boolean contains(int x, int y)
```

如下的例子，在矩形区域内点击时，边框由绿色变红色：

```java
public class RectPointView extends View {

    private int mX, mY;
    private Paint mPaint;
    private Rect mrect;

    public RectPointView(Context context) {
        super(context);
        init();
    }

    public RectPointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RectPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mrect = new Rect(100, 10, 300, 100);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mX = (int) event.getX();
        mY = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mX = -1;
            mY = -1;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mrect.contains(mX, mY)) {
            mPaint.setColor(Color.RED);
        } else {
            mPaint.setColor(Color.GREEN);
        }
        canvas.drawRect(mrect, mPaint);
    }
}
```

![025](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/025.png)

> `MotionEvent.ACTION_DOWN`中返回`true`，表示当前控件已经拦截（消费）了消息，后续的`ACTION_MOVE`、`ACTION_UP`消息仍会继续传过来。如果返回`false`（系统默认返回`false`），表示当前控件不需要这个消息，后续的`ACTION_MOVE`、`ACTION_UP`就不会传过来了
>
> `invalidate()`和`postInvalidate()`都是用来重绘控件的。
>
> + `invalidate()`一定要在主线程中执行，否则会报错
> + `postInvalidate()` - 可以在任意线程中执行

















