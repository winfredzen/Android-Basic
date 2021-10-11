# 自定义简单的Counter

在Youtube上看了[Droidcon NYC 2016 - Measure, Layout, Draw, Repeat: Custom Views and ViewGroups](https://www.youtube.com/watch?v=4NNmMO8Aykw)后，觉的有点收获，就记录下一些自定义`View`、`ViewGroup`的内容



**View的构造方法**

![014](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/014.png)



**invalidate()**

![015](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/015.png)



**在Drawing时要考虑的东西**

![016](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/016.png)



**测量的过程**

![017](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/017.png)

![018](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/018.png)

![019](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/019.png)

![020](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/020.png)



**Custom Layout Params**

![021](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/021.png)



**一些有用的方法**

![022](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/022.png)



## Counter

如下的`InteractedTallyCounter`

1.在`onMeasure`中，计算尺寸，处理`wrap_content`的情况

2.在`onDraw`中绘制

```java
public class InteractedTallyCounter extends View implements TallyCounter {

    private static final int MAX_COUNT = 9999;
    private static final String MAX_COUNT_STRING = String.valueOf(MAX_COUNT);

    private static final float YX_RATIO_THRESHOLD = 0.4f;
    private static final float SCROLL_INCREMENT_THRESHOLD_DP = 72;

    // State variables
    private int count;
    private String displayedCount;

    // Drawing variables
    private Paint backgroundPaint;
    private Paint linePaint;
    private TextPaint numberPaint;

    private RectF backgroundRect;

    private float cornerRadius;

    // Gesture detector
    private GestureDetector gestureDetector;

    //
    // Constructors/Initialization
    //

    /**
     * Simple constructor to use when creating a view using the {@code new} operator.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public InteractedTallyCounter(Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public InteractedTallyCounter(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Set up paints for canvas drawing.
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        // Allocate objects needed for canvas drawing here.
        backgroundRect = new RectF();

        // Get an array containing TallyCount attributes from XML.
        final TypedArray typedArray = context
            .obtainStyledAttributes(attrs, R.styleable.TallyCounter, 0, 0);

        // Get the background color from the attributes.
        final int backgroundColor = typedArray.getColor(R.styleable.TallyCounter_backgroundColor,
            ContextCompat.getColor(context, R.color.colorPrimary));
        backgroundPaint.setColor(backgroundColor);

        // Get the baseline color and width from the attributes.
        final int baselineColor = typedArray.getColor(R.styleable.TallyCounter_baselineColor,
            ContextCompat.getColor(context, R.color.colorAccent));
        final int baselineWidth =
            typedArray.getDimensionPixelSize(R.styleable.TallyCounter_baselineWidth, 1);
        linePaint.setColor(baselineColor);
        linePaint.setStrokeWidth(baselineWidth);

        // Get the text color and text size from the attributes.
        final int textColor = typedArray.getColor(R.styleable.TallyCounter_android_textColor,
            ContextCompat.getColor(context, android.R.color.white));
        final float textSize = Math.round(
            typedArray.getDimensionPixelSize(R.styleable.TallyCounter_android_textSize,
                Math.round(64f * getResources().getDisplayMetrics().scaledDensity)));

        numberPaint.setColor(textColor);
        numberPaint.setTextSize(textSize);

        // Get the corner radius.
        cornerRadius = typedArray.getDimensionPixelSize(R.styleable.TallyCounter_cornerRadius,
            Math.round(2f * getResources().getDisplayMetrics().density));

        // Recycle the TypeArray. Always do this!
        typedArray.recycle();

        // Do initial count setup.
        setCount(0);

        // Initialize gesture detector.
        gestureDetector = new GestureDetector(context,
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    increment();
                    return true;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    increment();
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    final boolean velocityUp = velocityY < 0;
                    if (!velocityUp) {
                        return false;
                    }
                    if (Math.abs(velocityY / velocityX) > YX_RATIO_THRESHOLD) {
                        increment();
                        return true;
                    }

                    return false;
                }
            });
    }

    //
    // Counter interface
    //

    @Override
    public void reset() {
        setCount(0);
    }

    @Override
    public void increment() {
        setCount(count + 1);
    }

    @Override
    public void setCount(int count) {
        count = Math.min(count, MAX_COUNT);
        this.count = count;
        this.displayedCount = String.format(Locale.getDefault(), "%04d", count);
        invalidate();
    }

    @Override
    public int getCount() {
        return count;
    }

    /**
     * Reconcile a desired size for the view contents with a {@link android.view.View.MeasureSpec}
     * constraint passed by the parent.
     *
     * Simplified version of {@link View#resolveSizeAndState(int, int, int)}.
     *
     * @param contentSize Size of the view's contents.
     * @param measureSpec A {@link android.view.View.MeasureSpec} passed by the parent.
     * @return A size that best fits {@code contentSize} while respecting the parent's constraints.
     */
    private int reconcileSize(int contentSize, int measureSpec) {
        final int mode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                if (contentSize < specSize) {
                    return contentSize;
                } else {
                    return specSize;
                }
            case MeasureSpec.UNSPECIFIED:
            default:
                return contentSize;
        }
    }

    //
    // View overrides
    //

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();

        final float maxTextWidth = numberPaint.measureText(MAX_COUNT_STRING);
        final float maxTextHeight = -fontMetrics.top + fontMetrics.bottom;

        final int desiredWidth = Math.round(maxTextWidth + getPaddingLeft() + getPaddingRight());
        final int desiredHeight = Math.round(maxTextHeight * 2f + getPaddingTop() +
            getPaddingBottom());

        final int measuredWidth = reconcileSize(desiredWidth, widthMeasureSpec);
        final int measuredHeight = reconcileSize(desiredHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Grab canvas dimensions.
        final int canvasWidth = canvas.getWidth();
        final int canvasHeight = canvas.getHeight();

        // Calculate horizontal center.
        final float centerX = canvasWidth * 0.5f;

        // Draw the background.
        backgroundRect.set(0f, 0f, canvasWidth, canvasHeight);
        canvas.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint);

        // Draw baseline.
        final float baselineY = Math.round(canvasHeight * 0.6f);
        canvas.drawLine(0, baselineY, canvasWidth, baselineY, linePaint);

        // Draw text.
        final float textWidth = numberPaint.measureText(displayedCount);
        final float textX = Math.round(centerX - textWidth * 0.5f);
        canvas.drawText(displayedCount, textX, baselineY, numberPaint);
    }
}
```

效果如下：

![023](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/023.png)



## 自定ViewGroup

这个`SimpleListItem`继承自`ViewGroup`，其表示的是`RecyclerView`中的一个列表项`View`，很有借鉴意义

重写了`generateDefaultLayoutParams()`等方法

`ViewGroup`中对这些方法的说明如下：

![024](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/024.png)



`SimpleListItem`内容如下：

```java
/**
 * Custom {@link ViewGroup} for displaying the example list items from the launch screen but
 * without the extra measure/layout pass from the {@link android.widget.RelativeLayout}.
 */
public class SimpleListItem extends ViewGroup {
    //
    // Fields
    //

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.subtitle)
    TextView subtitleView;

    //
    // Constructors
    //

    /**
     * Constructor.
     *
     * @param context The current context.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public SimpleListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    //
    // ViewGroup implementation
    //

    /**
     * Validates if a set of layout parameters is valid for a child of this ViewGroup.
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }


    /**
     * @return A set of default layout parameters when given a child with no layout parameters.
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * @return A set of layout parameters created from attributes passed in XML.
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * Called when {@link #checkLayoutParams(LayoutParams)} fails.
     *
     * @return A set of valid layout parameters for this ViewGroup that copies appropriate/valid
     * attributes from the supplied, not-so-good parameters.
     */
    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // Measure icon.
        measureChildWithMargins(icon, widthMeasureSpec, 0, heightMeasureSpec, 0);

        // Figure out how much width the icon used.
        MarginLayoutParams lp = (MarginLayoutParams) icon.getLayoutParams();
        int widthUsed = icon.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

        int heightUsed = 0;

        // Measure title
        measureChildWithMargins(
            titleView,
            // Pass width constraints and width already used.
            widthMeasureSpec, widthUsed,
            // Pass height constraints and height already used.
            heightMeasureSpec, heightUsed
        );

        // Measure the Subtitle.
        measureChildWithMargins(
            subtitleView,
            // Pass width constraints and width already used.
            widthMeasureSpec, widthUsed,
            // Pass height constraints and height already used.
            heightMeasureSpec, titleView.getMeasuredHeight());

        // Calculate this view's measured width and height.

        // Figure out how much total space the icon used.
        int iconWidth = icon.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        int iconHeight = icon.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        lp = (MarginLayoutParams) titleView.getLayoutParams();

        // Figure out how much total space the title used.
        int titleWidth = titleView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        int titleHeight = titleView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        lp = (MarginLayoutParams) subtitleView.getLayoutParams();

        // Figure out how much total space the subtitle used.
        int subtitleWidth = subtitleView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        int subtitleHeight = subtitleView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

        // The width taken by the children + padding.
        int width = getPaddingTop() + getPaddingBottom() +
            iconWidth + Math.max(titleWidth, subtitleWidth);
        // The height taken by the children + padding.
        int height = getPaddingTop() + getPaddingBottom() +
            Math.max(iconHeight, titleHeight + subtitleHeight);

        // Reconcile the measured dimensions with the this view's constraints and
        // set the final measured width and height.
        setMeasuredDimension(
            resolveSize(width, widthMeasureSpec),
            resolveSize(height, heightMeasureSpec)
        );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        MarginLayoutParams layoutParams = (MarginLayoutParams) icon.getLayoutParams();

        // Figure out the x-coordinate and y-coordinate of the icon.
        int x = getPaddingLeft() + layoutParams.leftMargin;
        int y = getPaddingTop() + layoutParams.topMargin;

        // Layout the icon.
        icon.layout(x, y, x + icon.getMeasuredWidth(), y + icon.getMeasuredHeight());

        // Calculate the x-coordinate of the title: icon's right coordinate +
        // the icon's right margin.
        x += icon.getMeasuredWidth() + layoutParams.rightMargin;

        // Add in the title's left margin.
        layoutParams = (MarginLayoutParams) titleView.getLayoutParams();
        x += layoutParams.leftMargin;

        // Calculate the y-coordinate of the title: this ViewGroup's top padding +
        // the title's top margin
        y = getPaddingTop() + layoutParams.topMargin;

        // Layout the title.
        titleView.layout(x, y, x + titleView.getMeasuredWidth(), y + titleView.getMeasuredHeight());

        // The subtitle has the same x-coordinate as the title. So no more calculating there.

        // Calculate the y-coordinate of the subtitle: the title's bottom coordinate +
        // the title's bottom margin.
        y += titleView.getMeasuredHeight() + layoutParams.bottomMargin;
        layoutParams = (MarginLayoutParams) subtitleView.getLayoutParams();

        // Add in the subtitle's top margin.
        y += layoutParams.topMargin;

        // Layout the subtitle.
        subtitleView.layout(x, y,
            x + subtitleView.getMeasuredWidth(), y + subtitleView.getMeasuredHeight());
    }
}

```

列表项布局内容如下，在布局时，并没有指定view在viewgroup中的位置关系，`list_item_simple.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<randomlytyping.widget.SimpleListItem
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:paddingTop="@dimen/list_item_padding_vertical"
    android:paddingBottom="@dimen/list_item_padding_vertical"
    android:paddingStart="0dp"
    android:paddingEnd="@dimen/activity_horizontal_margin"
    android:background="?attr/selectableItemBackground"
    android:clickable="true"
    android:focusable="true">

    <ImageView
        android:id="@+id/icon"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="@dimen/list_item_icon_margin_start"
        android:layout_marginEnd="@dimen/list_item_icon_margin_end"
        android:contentDescription="@null" />

    <TextView
        android:id="@+id/title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        style="@style/TextAppearance.AppCompat.Subhead" />

    <TextView
        android:id="@+id/subtitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        style="@style/TextAppearance.AppCompat.Body1"
        android:textColor="?android:attr/textColorSecondary" />

</randomlytyping.widget.SimpleListItem>
```

在`RecyclerView`中的显示效果如下：

![025](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/025.png)































