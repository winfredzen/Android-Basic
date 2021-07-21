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



**多颜色渐变**

```java
LinearGradient(float x0, float y0, float x1, float y1, int[] colors, float[] positions, Shader.TileMode tile)
```

+ colors - 用于指定渐变的颜色值数组
+ positions - 与渐变颜色相对应，取值范围为0~1，表示每种颜色在整条渐变线中的百分比位置

如下的例子：

```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int[] colors = {0xffff0000,0xff00ff00,0xff0000ff,0xffffff00,0xff00ffff};
        float[]  pos = {0f,0.2f,0.4f,0.6f,1.0f};
        LinearGradient linearGradient = new LinearGradient(0, getHeight() / 2, getWidth(), getHeight() / 2, colors, pos, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
```

![067](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/067.png)

修改`LinearGradient`的起点，如下：

```java
LinearGradient linearGradient = new LinearGradient(getWidth() / 2, getHeight() / 2, getWidth(), getHeight() / 2, colors, pos, Shader.TileMode.CLAMP);
```

![071](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/071.png)



**TileMode填充模式**

如果将上面的创建`LinearGradient`的终点位置指定为`getWidth() / 2`，即控件宽度的一半，设置不同的`TileMode`

1.`TileMode.CLAM`

![068](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/068.png)

2.`TileMode.MIRROR`

![069](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/069.png)

3.`TileMode.REPEAT`

![070](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/070.png)



**Shader填充与显示区域**

Shader的布局和显示是分离的，Shader总是从控件左上角开始布局，如果单张图片无法覆盖整个控件，则会使用`TileMode`重复模式来填充空白区域；而`canvas.draw`系列函数则只表示哪部分被显示出来

如下绘制渐变文字效果

```java
int[] colors = {0xffff0000,0xff00ff00,0xff0000ff,0xffffff00,0xff00ffff};
float[]  pos = {0f,0.2f,0.4f,0.6f,1.0f};
LinearGradient linearGradient = new LinearGradient(0, getHeight() / 2, getWidth() / 2, getHeight() / 2, colors, pos, Shader.TileMode.CLAMP);
mPaint.setShader(linearGradient);
mPaint.setTextSize(50);
canvas.drawText("Hello LinearGradient Hello LinearGradient", 0, 200, mPaint);
```

![072](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/072.png)



































