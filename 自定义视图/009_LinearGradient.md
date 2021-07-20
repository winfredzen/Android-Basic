# LinearGradient

`LinearGradient`继承自`Shader`，可实现线性渐变效果

**双色渐变**

使用构造方法：

```java
LinearGradient(float x0, float y0, float x1, float y1, int color0, int color1, Shader.TileMode tile)
```

+ x0, y0 - 起始渐变点坐标
+ x1, y1 - 结束渐变点坐标
+ color0 - 起始颜色 color1 - 终止颜色，颜色值必须使用`0xAARRGGBB`形式的十六进制数表示，其中表示透明度的`AA`一定不能少

如下的例子：

```java
public class LinearGradientView extends View {
    private Paint mPaint;

    public LinearGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setShader(new LinearGradient(0, getHeight() / 2, getWidth(), getHeight() / 2, 0xffff0000, 0xff00ff00, Shader.TileMode.CLAMP));
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
}
```

![066](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/066.png)

























