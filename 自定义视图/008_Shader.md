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

如下的例子，将x轴和y轴都设置为`Shader.TileMode.REPEAT`

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



























































