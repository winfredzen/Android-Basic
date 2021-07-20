# Shader

Shader在三维软件中被称为着色器，是用来给空白图形上色的。

Shader类只是一个基类，其子类有：[BitmapShader](https://developer.android.google.cn/reference/kotlin/android/graphics/BitmapShader), [ComposeShader](https://developer.android.google.cn/reference/kotlin/android/graphics/ComposeShader), [LinearGradient](https://developer.android.google.cn/reference/kotlin/android/graphics/LinearGradient), [RadialGradient](https://developer.android.google.cn/reference/kotlin/android/graphics/RadialGradient), [SweepGradient](https://developer.android.google.cn/reference/kotlin/android/graphics/SweepGradient)

Paint中有一个函数专门用于设置Shader

```java
paint.setShader(shader)
```

> After that any object (other than a bitmap) that is drawn with that paint will get its color(s) from the shader.
>
> paint绘制时会从shader中取颜色



## BitmapShader

```java
public BitmapShader(@NonNull Bitmap bitmap, @NonNull TileMode tileX, @NonNull TileMode tileY)
```

+ bitmap - 用来指定图案
+ tileX - 当X轴超出单张图片大小时所使用的重复策略
+ tileY - 当Y轴超出单张图片大小时所使用的重复策略

TileMode的取值如下：

+ CLAMP - 用边缘色彩来填充多余空间
+ REPEAT - 重复图像来填充多余空间
+ MIRROR - 重复使用镜像模式的图像来填充多余空间

如下的例子，**将x轴和y轴都设置为`Shader.TileMode.REPEAT`**

```java
public class BitmapShaderView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;


    public BitmapShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog_edge);

        mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
}
```

效果如下：

![058](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/058.png)

> X轴、Y轴全部设置为REPEAT模式，当控件的显示范围超出单张图片的显示范围时，在X轴上将使用REPEAT模式，Y轴通用



如果X轴、Y轴都设置为`Shader.TileMode.MIRROR`

![059](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/059.png)

> 在X轴上没两张图片的显示都像镜子一样翻转一下；同样，在Y轴上每两张图片的显示也像镜子一样翻转一下



如果X轴、Y轴都设置为`Shader.TileMode.CLAMP`

![060](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/060.png)

> 当控件区域超出单张图片的大小时，空白位置就用图片的边缘颜色来填充



**填充顺序**

从CLAMP模式的效果可以看出，空白区域的填充是先竖向填充，然后再以竖向填充结果为模板进行横向填充

![061](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/061.png)

如下在X轴上使用MIRROR模式，在Y轴上使用REPEAT模式：

```java
mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.MIRROR, Shader.TileMode.REPEAT));
```

![062](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/062.png)

> **无论哪两种混合模式，只需要记住先填充Y轴，然后填充X轴**





**绘图位置与图像显示**

在上面的例子中，`drawRect()`函数是把控件的整个大小区域都覆盖了，绘制的效果如下：

```java
canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
```

![058](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/058.png)

如果只绘制控件1/3的区域，如下：

```java
float left = getWidth() / 3;
float top = getHeight() / 3;
float right = getWidth() * 2 / 3;
float bottom = getHeight() * 2 / 3;
canvas.drawRect(left, top, right, bottom, mPaint);
```

效果如下：

![063](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/063.png)

> 整个区域的绘制好似从上面完整图片抠出来的一小部分
>
> 这说明：无论利用绘图函数绘制多大的图像、在哪里绘制，都与shader无关。**因为Shader总是从控件的左上角开始的，而我们绘制的只是显示出来的部分而已，没有绘制的部分虽然已经生成，但是不会显示出来**



## 例子

**1.望远镜效果**

效果如下：

![064](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/064.gif)

```java
public class TelescopeView extends View {
    private Paint mPaint;
    private Bitmap mBitmap, mBitmapBG;
    private int mDx = -1, mDy = -1;

    public TelescopeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDx = (int) event.getX();
                mDy = (int) event.getY();
                postInvalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                mDx = (int) event.getX();
                mDy = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mDx = -1;
                mDy = -1;
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmapBG == null) {
            mBitmapBG = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvasBg = new Canvas(mBitmapBG);
            canvasBg.drawBitmap(mBitmap, null, new Rect(0, 0, getWidth(), getHeight()), mPaint);
        }

        if (mDx != -1 && mDy != -1) {
            mPaint.setShader(new BitmapShader(mBitmapBG, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
            canvas.drawCircle(mDx, mDy, 150, mPaint);
        }
    }
}
```

> 这里，需要将图片缩放到控件大小，以完全覆盖控件。先新建一张空白的位图，这张位图的大小和控件的大小一样，然后对背景图进行拉伸，画到这张空白的位图上。之所以在`onDraw()`函数中创建`mBitmapBG`，而不是在初始化中创建，是因为在初始化时，`getWidth()`和`getHeight()`函数是获取不到值的











































































