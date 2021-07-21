# RadialGradient

RadialGradient是Shader的另一种实现，它的含义是放射渐变

**双色渐变**

```java
public RadialGradient(float centerX, float centerY, float radius,
            @ColorInt int centerColor, @ColorInt int edgeColor, @NonNull TileMode tileMode)
```

+ centerX、centerY - 渐变中心x、y坐标
+ radius - 渐变半径
+ centerColor - 渐变起始色，取值必须是8位0xAARRGGBB色值。alpha值不能省略
+ edgeColor - 渐变结束的颜色，即渐变圆边缘的颜色
+ tileMode - 当控件区域大于指定的渐变区域时，空白区域的颜色填充方式

如下的例子：

```java
public class RadialGradientView extends View {
    private Paint mPaint;
    private RadialGradient mRadialGradient;
    private int mRadius;

    public RadialGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRadialGradient == null) {
            mRadius = getWidth() / 2;
            mRadialGradient =new RadialGradient(getWidth() / 2, getHeight() / 2, mRadius, 0xffff0000, 0xff00ff00, Shader.TileMode.REPEAT);
            mPaint.setShader(mRadialGradient);
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
    }
}
```

![074](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/074.png)

> 之所以在`onDraw()`函数中初始化`mRadialGradient`，是因为`getWidth()`和`getHeight()`函数需要在生命周期函数`onLayout()`执行完毕后才会有值



**多色渐变**

```java
public RadialGradient(float centerX, float centerY, float radius,
            @NonNull @ColorInt int[] colors, @Nullable float[] stops,
            @NonNull TileMode tileMode)
```

+ colors - 表示所需的渐变颜色数组
+ stops - 表示每种渐变颜色所在的位置百分点，取值范围为`0~1`，数量必须与colors数组保持一致。一般第一个数值取0，最后一个取1.如果第一个数值和最后一个数值并没有取0和1，比如，取一个位置数组`{0.2, 0.5, 0.8}`,起始点是0.2百分比位置，终点是0.8百分比位置，而0~0.2百分比位置和0.8~1百分比位置都是没有指定颜色值。这些位置的颜色就是根据指定的TileMode空白区域填充模式来自行填充的。



如下的例子：

```java
public class ColorsRadialGradientView extends View {
    private Paint mPaint;
    private RadialGradient mRadialGradient;
    private int mRadius;

    public ColorsRadialGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRadialGradient == null) {
            mRadius = getWidth() / 2;
            int[] colors = new int[] {0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00};
            float[] stops = new float[] {0f, 0.2f, 0.5f, 1f};
            mRadialGradient =new RadialGradient(getWidth() / 2, getHeight() / 2, mRadius, colors, stops, Shader.TileMode.REPEAT);
            mPaint.setShader(mRadialGradient);
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);
    }
}
```

![075](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/075.png)



**TileMode填充模式**

修改radius，使用不同的填充模式，观察区别

```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRadialGradient == null) {
            mRadius = getWidth() / 6;
            mRadialGradient =new RadialGradient(getWidth() / 2, getHeight() / 2, mRadius, 0xffff0000, 0xff00ff00, Shader.TileMode.CLAMP);
            mPaint.setShader(mRadialGradient);
        }
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
```

**1.TileMode.CLAMP**

![076](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/076.png)

**2.TileMode.MIRROR**

![077](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/077.png)

**3.TileMode.REPEAT**

![078](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/078.png)

















