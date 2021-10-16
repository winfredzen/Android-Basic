# ItemDecoration

可参考：

+ [RecyclerView ItemDecoration 完全解析](https://www.jianshu.com/p/bcbfb84fe6d1)
+ [RecyclerView之ItemDecoration由浅入深](https://www.jianshu.com/p/b46a4ff7c10a)



如下一个简单的`RecyclerView`的例子

![023](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/023.png)

如果给`RecyclerView`添加一个系统的`DividerItemDecoration`

```java
DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
mRecyclerView.addItemDecoration(decoration);
```

![024](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/024.png)

如果要需改分割线的背景，可以通过`DividerItemDecoration`的`setDrawable`方法，如下的`drawable_divider.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle"
    >

    <!-- 渐变色 -->
    <gradient
        android:angle="135"
        android:centerColor="#4CAF50"
        android:endColor="#2E7D32"
        android:startColor="#81C784"
        android:type="linear" />

    <size android:height="2dp" />

</shape>
```

![025](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/025.png)



## 自定义 ItemDecoration

主要三个方法

```java
public void onDraw(Canvas c, RecyclerView parent, State state)
public void onDrawOver(Canvas c, RecyclerView parent, State state)
public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state)
```

+ `getItemOffsets()`,可以实现类似padding的效果
+ `onDraw()`,可以实现类似绘制背景的效果，内容在上面
+ `onDrawOver()`，可以绘制在内容的上面，覆盖内容



### getItemOffsets() 

如下的例子，将`RecyclerView`的背景设置为红色

```java
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = 10;
        outRect.left = 20;
        outRect.right = 40;
    }
}
```

```java
LinearItemDecoration linearItemDecoration = new LinearItemDecoratio
mRecyclerView.addItemDecoration(linearItemDecoration);
```

![026](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/026.png)



### onDraw()

如下的例子，在左侧绘制一个圆圈

```java
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "LinearItemDecoration";
    private Paint paint;

    public LinearItemDecoration() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = 10;
        outRect.left = 100;
        outRect.right = 40;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        Log.e(TAG, "onDraw: ");
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            c.drawCircle(50, childView.getTop() + childView.getHeight() / 2, 20, paint);
        }
    }
}
```

![027](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/027.png)



通过这种方式，我们可以自己绘制分割线

```java
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "LinearItemDecoration";
    private Paint paint;
    private Paint dividerPaint;

    public LinearItemDecoration() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(Color.parseColor("#e6e6e6"));
        dividerPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = 5;
        outRect.left = 100;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        Log.e(TAG, "onDraw: ");
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            //获取getItemOffsets中设置的值
            int leftDecorationWidth = layoutManager.getLeftDecorationWidth(childView);
            int topDecorationHeight = layoutManager.getTopDecorationHeight(childView);
            int rightDecorationWidth = layoutManager.getRightDecorationWidth(childView);
            int bottomDecorationHeight = layoutManager.getBottomDecorationHeight(childView);

            int left = leftDecorationWidth / 2;
            canvas.drawCircle(left, childView.getTop() + childView.getHeight() / 2, 20, paint);
            // getItemOffsets()中的设置的是 bottom = 5px;所以在 drawRect 时，top 为 childView.getBottom,bottom为top+bottomDecorationHeight
            canvas.drawRect(new Rect(
                    leftDecorationWidth,
                    childView.getBottom(),
                    childView.getWidth() + leftDecorationWidth,
                    childView.getBottom() + bottomDecorationHeight
            ), dividerPaint);
        }
    }
}
```

![028](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/028.png)




























