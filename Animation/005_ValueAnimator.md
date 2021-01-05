# ValueAnimator

属性动画包括：

+ ValueAnimator
+ ObjectAnimator

为什么引入属性动画？

1.弥补视图动画的不足

2.视图动画对指定的控件做动画，属性动画通过改变控件的某一个属性来做动画



`ValueAnimator`不会对控件执行任何操作，通过监听值的改变来自己操控控件

如下的例子，一个位移动画

```java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.tv);
        mButton = (Button) findViewById(R.id.btn);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAnimation();
            }
        });


        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clicked me", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void doAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 400);
        animator.setDuration(1000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (Integer) animation.getAnimatedValue();
                mTextView.layout(curValue, curValue, curValue + mTextView.getWidth(), curValue + mTextView.getHeight());
            }
        });

        animator.start();
    }
```

![019](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/019.gif)

> ```java
> void layout(int l, int t, int r, int b)
> ```
>
> 通过`layout`方法来改变位置，`layout`改变位置是永久性的



**常用方法**

```java
public static ValueAnimator ofInt(int... values)
public static ValueAnimator ofArgb(int... values)
public static ValueAnimator ofFloat(float... values)
```

> 参数类型为可变长参数。`ofInt(2, 90, 45)`表示数字2变化到数字90，再变化到45

```java
Object getAnimatedValue()
```

> 获取当前运动点的值，返回类型为`Object`，要转换

```java
ValueAnimator setDuration(long duration)
```

> 设置动画时长，单位是毫秒

```java
void start()
```

> 开始动画

```java
void cancel()
```

> 取消动画

```java
void setRepeatCount(int value)
```

> 设置循环次数，设置为`INFINITE`表示无限循环

```java
void setRepeatMode(@RepeatMode int value)
```

> 设置循环模式，值为`RESTART`和`REVERSE`



如下的例子：

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.tv);
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepeatAnimator = doRepeatAnim();
                mRepeatAnimator.start();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRepeatAnimator != null) {
                    mRepeatAnimator.cancel();
                }
            }
        });


        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "clicked me", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private ValueAnimator doRepeatAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (Integer) animation.getAnimatedValue();
                mTextView.layout(curValue, curValue, curValue + mTextView.getWidth(), curValue + mTextView.getHeight());
            }
        });

        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(1000);
        animator.start();

        return animator;
    }
```

效果为：

![020](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/020.gif)



























