# Canvas

## 绘制顺序

参考：

+ [HenCoder Android 开发进阶：自定义 View 1-5 绘制顺序](https://rengwuxian.com/ui-1-5/)



![](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/179.png)



`dispatchDraw(Canvas canvas)`函数用于绘制子视图



## 获取Canvas对象的方法

1.重写`onDraw()`、`dispatchDraw()`函数

2.使用Bitmap创建

```java
public Canvas(@NonNull Bitmap bitmap)
```

如果使用Bitmap构造了一个Canvas，那么在这个Canvas上绘制的图像也会保存在这个Bitmap上，而不会画在View上

如下的例子：

```java
public class BitmapCanvasView extends View {
    private Bitmap mBitmap;
    private Paint mPaint;
    private Canvas mBitmapCanvas;

    public BitmapCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mBitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        mBitmapCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setTextSize(50);
        mBitmapCanvas.drawText("This is Text", 0, 100, mPaint);

        //canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }
}
```

运行代码后，页面是空白的。原因是，我们是将文字绘制在了mBitmapCanvas上，而没有将图片画在画布上。

取消最后一行的注释，效果如下：

![180](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/180.png)





## 画布保存与恢复

```java
 int save()
 void restore()
```

+ `save()` - 先保存当前画布的状态，然后将其放入特定的栈中
+ `restore()` - 会把栈中顶层的画布状态取出来，并按照这个状态恢复画布，然后在这个画布上作画

如下的例子：

```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.RED);
        //保存的画布大小为全屏幕大小
        canvas.save();

        canvas.clipRect(new Rect(100, 100, 800, 800));
        canvas.drawColor(Color.GREEN);
        //恢复整屏画布
        canvas.restore();
        canvas.drawColor(Color.BLUE);
    }
```

![146](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/146.png)

下面的例子，多次`save()`后，页面如下：

```java
canvas.drawColor(Color.RED);
//保存的画布大小为全屏幕大小
int c1 = canvas.save();

canvas.clipRect(new Rect(100, 100, 800, 800));
canvas.drawColor(Color.GREEN);
//保存画布大小为Rect(100, 100, 800, 800)
int c2 = canvas.save();

canvas.clipRect(new Rect(200, 200, 700, 700));
canvas.drawColor(Color.BLUE);
//保存画布大小为Rect(200, 200, 700, 700)
int c3 = canvas.save();

canvas.clipRect(new Rect(300, 300, 600, 600));
canvas.drawColor(Color.BLACK);
//保存画布大小为Rect(300, 300, 600, 600)
int c4 = canvas.save();

canvas.clipRect(new Rect(400, 400, 500, 500));
canvas.drawColor(Color.WHITE);

//连续出栈三次，将最后一次出栈的Canvas状态作为当前画布，并画成黄色背景
canvas.restoreToCount(c2);
canvas.drawColor(Color.YELLOW);
```

![147](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/147.png)

当前栈的状态如下：

![148](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/148.png)

> 注意：在第四次`save()`函数之后，还对画布进行了`canvas.clipRect(new Rect(400, 400, 500, 500));`操作，并将当前画布填充为白色

如果现在调用`restore()`函数来还原画布，则会把栈顶的画布状态取出来，作为当前绘制的画布

```java
···
canvas.restore();
canvas.drawColor(Color.YELLOW);
```

![149](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/149.png)
