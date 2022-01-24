# 自定义EditText

实现一个带删除图标的EditText

1.自定义控件属性

`attrs.xml`中的内容如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="CustomEditText">

        <attr name="ic_delete" format="reference" />

    </declare-styleable>
</resources>
```

2.`CustomEditText`内容如下：

```java
public class CustomEditText extends EditText {
    private Drawable mDeleteDrawable;
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        // 获取控件资源
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        int ic_deleteResID = typedArray.getResourceId(R.styleable.CustomEditText_ic_delete,R.drawable.delete);
        mDeleteDrawable =  getResources().getDrawable(ic_deleteResID);
        mDeleteDrawable.setBounds(0, 0, 80, 80);
        typedArray.recycle();
    }


    /**
     * 复写EditText本身的方法：onTextChanged（）
     * 调用时刻：当输入框内容变化时
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setDeleteIconVisible(hasFocus() && text.length() > 0);
        // hasFocus()返回是否获得EditTEXT的焦点，即是否选中
        // setDeleteIconVisible（） = 根据传入的是否选中 & 是否有输入来判断是否显示删除图标->>关注1
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
        switch (event.getAction()) {
        // 判断动作 = 手指抬起时
        case MotionEvent.ACTION_UP:

            if (mDeleteDrawable != null && event.getX() <= (getWidth() - getPaddingRight())
                    && event.getX() >= (getWidth() - getPaddingRight() - mDeleteDrawable.getBounds().width())) {

                // 判断条件说明
                // event.getX() ：抬起时的位置坐标
                // getWidth()：控件的宽度
                // getPaddingRight():删除图标图标右边缘至EditText控件右边缘的距离
                // 即：getWidth() - getPaddingRight() = 删除图标的右边缘坐标 = X1
                // getWidth() - getPaddingRight() - drawable.getBounds().width() = 删除图标左边缘的坐标 = X2
                // 所以X1与X2之间的区域 = 删除图标的区域
                // 当手指抬起的位置在删除图标的区域（X2=<event.getX() <=X1），即视为点击了删除图标 = 清空搜索框内容
                setText("");

            }
            break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 复写EditText本身的方法：onFocusChanged（）
     * 调用时刻：焦点发生变化时
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setDeleteIconVisible(focused && length() > 0);
        // focused = 是否获得焦点
        // 同样根据setDeleteIconVisible（）判断是否要显示删除图标->>关注1
    }


    /**
     * 关注1
     * 作用：判断是否显示删除图标 & 设置分割线颜色
     */
    private void setDeleteIconVisible(boolean deleteVisible) {
        setCompoundDrawables(null, null,
                deleteVisible ?  mDeleteDrawable: null, null);
        invalidate();
    }
}

```

![185](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/185.png)
