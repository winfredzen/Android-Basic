# 使用代码创建ConstraintLayout

有时候在使用ConstraintLayout的时候，需要用代码来动态的创建ConstraintLayout的约束

参考：

+ [How to Programmatically Setup a ConstraintLayout with ConstraintLayout.LayoutParams](https://www.youtube.com/watch?v=rzNtcbOR0jE)

按照教程中的说明，有2中方式

1.直接使用`ConstraintLayout.LayoutParams`

2.使用`ConstraintSet`

```java
public class MyConstraintLayout extends ConstraintLayout {
    public MyConstraintLayout(@NonNull Context context) {
        this(context, null);
    }

    public MyConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //设置一些属性
        setId(View.generateViewId());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        setBackgroundColor(getResources().getColor(R.color.purple_200));

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);

        //hardcode2by2GridOfButtons(context, constraintSet);
        hardcode2by2GridOfButtons(context);
        constraintSet.applyTo(this);
    }

    private void hardcode2by2GridOfButtons(Context context) {
        Button button1 = new Button(context);
        button1.setId(View.generateViewId());
        button1.setText("ONE");
        addView(button1);

        Button button2 = new Button(context);
        button2.setId(View.generateViewId());
        button2.setText("TWO");
        addView(button2);

        Button button3 = new Button(context);
        button3.setId(View.generateViewId());
        button3.setText("THREE");
        addView(button3);

        Button button4 = new Button(context);
        button4.setId(View.generateViewId());
        button4.setText("FOUR");
        addView(button4);

        //button1
        //限制button1的宽和高
        ConstraintLayout.LayoutParams layoutParams1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.startToStart = getId();
        layoutParams1.endToStart = button2.getId();
        layoutParams1.topToTop = getId();
        layoutParams1.bottomToTop = button3.getId();
        button1.setLayoutParams(layoutParams1);

        //button2
        ConstraintLayout.LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.startToEnd = button1.getId();
        layoutParams2.endToEnd = getId();
        layoutParams2.topToTop = button1.getId();
        layoutParams2.bottomToBottom = button1.getId();
        button2.setLayoutParams(layoutParams2);

        //button3
        ConstraintLayout.LayoutParams layoutParams3 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams3.startToStart = getId();
        layoutParams3.endToStart = button4.getId();
        layoutParams3.topToBottom = button1.getId();
        layoutParams3.bottomToBottom = getId();
        button3.setLayoutParams(layoutParams3);

        //button4
        ConstraintLayout.LayoutParams layoutParams4 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams4.startToEnd = button3.getId();
        layoutParams4.endToEnd = getId();
        layoutParams4.topToTop = button3.getId();
        layoutParams4.bottomToBottom = button3.getId();
        button4.setLayoutParams(layoutParams4);

    }

    private void hardcode2by2GridOfButtons(Context context, ConstraintSet constraintSet) {
        Button button1 = new Button(context);
        button1.setId(View.generateViewId());
        button1.setText("ONE");
        addView(button1);

        Button button2 = new Button(context);
        button2.setId(View.generateViewId());
        button2.setText("TWO");
        addView(button2);

        Button button3 = new Button(context);
        button3.setId(View.generateViewId());
        button3.setText("THREE");
        addView(button3);

        Button button4 = new Button(context);
        button4.setId(View.generateViewId());
        button4.setText("FOUR");
        addView(button4);

        //button1
        //限制button1的宽和高
        constraintSet.constrainWidth(button1.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(button1.getId(), ConstraintSet.MATCH_CONSTRAINT);
        //button1的水平和垂直限制
        constraintSet.connect(button1.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(button1.getId(), ConstraintSet.END, button2.getId(), ConstraintSet.START);
        constraintSet.connect(button1.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(button1.getId(), ConstraintSet.BOTTOM, button3.getId(), ConstraintSet.TOP);

        //button2
        constraintSet.constrainWidth(button2.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(button2.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.connect(button2.getId(), ConstraintSet.START, button1.getId(), ConstraintSet.END);
        constraintSet.connect(button2.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(button2.getId(), ConstraintSet.TOP, button1.getId(), ConstraintSet.TOP);
        constraintSet.connect(button2.getId(), ConstraintSet.BOTTOM, button1.getId(), ConstraintSet.BOTTOM);

        //button3
        constraintSet.constrainWidth(button3.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(button3.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.connect(button3.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(button3.getId(), ConstraintSet.END, button4.getId(), ConstraintSet.START);
        constraintSet.connect(button3.getId(), ConstraintSet.TOP, button1.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(button3.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        //button4
        constraintSet.constrainWidth(button4.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(button4.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.connect(button4.getId(), ConstraintSet.START, button3.getId(), ConstraintSet.END);
        constraintSet.connect(button4.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(button4.getId(), ConstraintSet.TOP, button3.getId(), ConstraintSet.TOP);
        constraintSet.connect(button4.getId(), ConstraintSet.BOTTOM, button3.getId(), ConstraintSet.BOTTOM);

    }
}
```

当宽度和高度为`LayoutParams.WRAP_CONTENT`时，效果如下：

![075](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/075.png)

当宽度和高度为`ConstraintSet.MATCH_CONSTRAINT`时，效果如下：

![076](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/076.png)



## chain

使用代码创建chain

参考：

+ [How to Programmatically Setup a Android ConstraintLayout Chains and Chain Styles](https://www.youtube.com/watch?v=ZfaaOr7Sv-U)



```java
    private void hardcode2by2GridOfButtons2(Context context, ConstraintSet constraintSet) {
        Button button1 = new Button(context);
        button1.setId(View.generateViewId());
        button1.setText("ONE");
        addView(button1);

        Button button2 = new Button(context);
        button2.setId(View.generateViewId());
        button2.setText("TWO");
        addView(button2);

        Button button3 = new Button(context);
        button3.setId(View.generateViewId());
        button3.setText("THREE");
        addView(button3);

        Button button4 = new Button(context);
        button4.setId(View.generateViewId());
        button4.setText("FOUR");
        addView(button4);

        //button1
        //限制button1的宽和高
        constraintSet.constrainWidth(button1.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(button1.getId(), ConstraintSet.WRAP_CONTENT);
        //button1的水平和垂直限制
        constraintSet.connect(button1.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(button1.getId(), ConstraintSet.END, button2.getId(), ConstraintSet.START);
        constraintSet.connect(button1.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(button1.getId(), ConstraintSet.BOTTOM, button3.getId(), ConstraintSet.TOP);

        //button2
        constraintSet.constrainWidth(button2.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(button2.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(button2.getId(), ConstraintSet.START, button1.getId(), ConstraintSet.END);
        constraintSet.connect(button2.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(button2.getId(), ConstraintSet.TOP, button1.getId(), ConstraintSet.TOP);
        constraintSet.connect(button2.getId(), ConstraintSet.BOTTOM, button1.getId(), ConstraintSet.BOTTOM);

        //button3
        constraintSet.constrainWidth(button3.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(button3.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(button3.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(button3.getId(), ConstraintSet.END, button4.getId(), ConstraintSet.START);
        constraintSet.connect(button3.getId(), ConstraintSet.TOP, button1.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(button3.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        //button4
        constraintSet.constrainWidth(button4.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(button4.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.connect(button4.getId(), ConstraintSet.START, button3.getId(), ConstraintSet.END);
        constraintSet.connect(button4.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(button4.getId(), ConstraintSet.TOP, button3.getId(), ConstraintSet.TOP);
        constraintSet.connect(button4.getId(), ConstraintSet.BOTTOM, button3.getId(), ConstraintSet.BOTTOM);

        int[] horizontalChainIds1 = {button1.getId(), button2.getId()};
        constraintSet.createHorizontalChain(ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                horizontalChainIds1,
                null,
                ConstraintSet.CHAIN_PACKED);
        int[] horizontalChainIds2 = {button3.getId(), button4.getId()};
        constraintSet.createHorizontalChain(ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT,
                horizontalChainIds2,
                null,
                ConstraintSet.CHAIN_PACKED);
        int[] verticalChainIds = {button1.getId(), button3.getId()};
        constraintSet.createVerticalChain(ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                verticalChainIds,
                null,
                ConstraintSet.CHAIN_PACKED);

    }
```

效果如下：

![077](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/077.png)



























