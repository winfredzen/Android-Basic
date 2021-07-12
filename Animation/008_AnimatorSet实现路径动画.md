# AnimationSet实现路径动画

内容来自`《Android自定义控件开发入门与实践》`一书

最终的效果如下：

![035](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/035.gif)

主要代码如下`MainActivity`

```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean mIsMenuOpen = false;
    private Button mMenuButton;
    private Button mItemButton1;
    private Button mItemButton2;
    private Button mItemButton3;
    private Button mItemButton4;
    private Button mItemButton5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMenuButton = findViewById(R.id.menu);
        mItemButton1 = findViewById(R.id.item1);
        mItemButton2 = findViewById(R.id.item2);
        mItemButton3 = findViewById(R.id.item3);
        mItemButton4 = findViewById(R.id.item4);
        mItemButton5 = findViewById(R.id.item5);

        mMenuButton.setOnClickListener(this);
        mItemButton1.setOnClickListener(this);
        mItemButton2.setOnClickListener(this);
        mItemButton3.setOnClickListener(this);
        mItemButton4.setOnClickListener(this);
        mItemButton5.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (!mIsMenuOpen) {
            mIsMenuOpen = true;
            openMenu();
        } else {
            Toast.makeText(this, "你单击了" + v, Toast.LENGTH_SHORT).show();
            mIsMenuOpen = false;
            closeMenu();
        }
    }

    private void openMenu() {
        doAnimateOpen(mItemButton1, 0, 5, 300);
        doAnimateOpen(mItemButton2, 1, 5, 300);
        doAnimateOpen(mItemButton3, 2, 5, 300);
        doAnimateOpen(mItemButton4, 3, 5, 300);
        doAnimateOpen(mItemButton5, 4, 5, 300);
    }

    private void closeMenu() {
        doAnimationClose(mItemButton1, 0, 5, 300);
        doAnimationClose(mItemButton2, 1, 5, 300);
        doAnimationClose(mItemButton3, 2, 5, 300);
        doAnimationClose(mItemButton4, 3, 5, 300);
        doAnimationClose(mItemButton5, 4, 5, 300);
    }

    private void doAnimateOpen(View view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }

        double degree = Math.toRadians(90) / (total - 1) * index;
        int translationX = - (int) (radius * Math.sin(degree));
        int translationY = - (int) (radius * Math.cos(degree));

        AnimatorSet set  = new AnimatorSet();

        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        );
        set.setDuration(500).start();

    }

    private void doAnimationClose(View view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }

        double degree = Math.toRadians(90) / (total - 1) * index;
        int translationX = - (int) (radius * Math.sin(degree));
        int translationY = - (int) (radius * Math.cos(degree));

        AnimatorSet set  = new AnimatorSet();

        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        );
        set.setDuration(500).start();
    }



}
```

布局代码：

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/menu"
        style="@style/MenuStyle"
        android:background="@drawable/menu"
        />

    <Button
        android:id="@+id/item1"
        style="@style/MenuItemStyle"
        android:background="@drawable/circle1"
        android:visibility="invisible"
        />

    <Button
        android:id="@+id/item2"
        style="@style/MenuItemStyle"
        android:background="@drawable/circle2"
        android:visibility="invisible"
        />

    <Button
        android:id="@+id/item3"
        style="@style/MenuItemStyle"
        android:background="@drawable/circle3"
        android:visibility="invisible"
        />

    <Button
        android:id="@+id/item4"
        style="@style/MenuItemStyle"
        android:background="@drawable/circle4"
        android:visibility="invisible"
        />

    <Button
        android:id="@+id/item5"
        style="@style/MenuItemStyle"
        android:background="@drawable/circle5"
        android:visibility="invisible"
        />

</FrameLayout>
```

> 书上说，动画存在问题
>
> > 当菜单关闭以后，再单击菜单展开时所处位置，任然会响应单击事件，把菜单再次打开
> >
> > 原因是：通过`setScaleX()`和`setScaleY()`函数做动画时，将控件缩小到0， 但控件缩小到0以后，对它所做的属性动画并不会实际改变控件的位置

文章中，给出的解决方法是：

1.不把控件缩小到0，缩小到如0.1f

2.监听动画状态，在动画结束时，将控件放大即可























