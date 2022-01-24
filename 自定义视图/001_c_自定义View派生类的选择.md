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



### 继承自View的子类

上面的例子，如果直接继承自`ImageView`，并不需要做什么特别的处理

如下的例子，实现黑白图像效果，利用`ColorMatrix`来实现黑白图像效果：

```java
public class CustomImageView extends ImageView {

    private ColorMatrix colorMatrix = new ColorMatrix(new float[]{
            0.213f, 0.715f, 0.072f, 0, 0,
            0.213f, 0.715f, 0.072f, 0, 0,
            0.213f, 0.715f, 0.072f, 0, 0,
            0,       0,    0, 1, 0,
    });

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        super.onDraw(canvas);
        canvas.restore();
    }
}
```

![182](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/182.png)



## 继承自ViewGroup

1.重写`onLayout`函数，布局每个子控件

2.重写`onMeasure`函数，适配`wrap_content`模式，还需要调用`measureChildren`函数，以测量所有子控件

```java
public class CustomViewGroup extends ViewGroup {
    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量子控件
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        //获取当宽/高设为wrap_content时，此模式下的控件宽高
        int height = 0, maxWidth = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            if (childWidth > maxWidth) {
                maxWidth = childWidth;
            }
            int childHeight = child.getMeasuredHeight();
            height += childHeight;
        }

        //在wrap_content模式下，设置控件宽高
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(maxWidth,height);
        }else if (widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(maxWidth,heightSize);
        }else if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize,height);
        }else {
            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            child.layout(0, top, childWidth, top + childHeight);
            top += childHeight;
        }
    }
}

```

布局文件如下：

```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:orientation="vertical"
    android:background="#ffff00"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".CustomViewGroupActivity">

    <com.custom.harvic.testcustomctrl.CustomViewGroup
        android:background="#ff0000"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/dog"/>

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textStyle="bold"
            android:textColor="#ffff00"
            android:text="启舰"/>
    </com.custom.harvic.testcustomctrl.CustomViewGroup>

</LinearLayout>
```

效果如下：

![183](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/183.png)

















































































