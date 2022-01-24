# 自定义View的派生类的选择

如果控件内部没有包裹其它控件，那就继承自View类

如果控件内部包裹了其它控件，那就继承自ViewGroup类



## 继承自View

1.当继承自View类时，需要重写View的`onMeasure`函数，并实现对`wrap_content`的处理

2.重写`onDraw`函数，绘制控件内容



### 直接继承自View

如下的例子，显示一张图像，当宽度和高度设置为`wrap_content`时，使用图像本身的宽度和高度作为View的宽度和高度

```java
public class CustomView extends View {
    public CustomView(Context context) {
        super(context);
        init(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private int mWidth, mHeight;
    private Bitmap mBmp;

    private void init(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.mipmap.dog);
        mWidth = drawable.getIntrinsicWidth();
        mHeight = drawable.getIntrinsicHeight();

        mBmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.dog);
        mWidth = mBmp.getWidth();
        mHeight = mBmp.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mHeight);
        } else {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (mBmp == null) {
            return;
        }
        canvas.drawBitmap(mBmp, 0, 0, null);


    }
}
```

![181](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/181.png)





















