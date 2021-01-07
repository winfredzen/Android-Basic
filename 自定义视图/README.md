# View

在Android中有2种坐标系

+ Android坐标系
+ View坐标系

**Android坐标系**，屏幕左上角作为坐标系的原点

> 另外在触控事件中，使用`getRawX()`和`getRawY()`方法获得的坐标也是Android坐标系

![010](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/010.png)

**View坐标系**

![011](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/011.png)

**View获取自身的宽和高**

```java
getWidth()
getHeight()
```

**View自身的坐标**

获取View到其父控件的(ViewGroup)的距离

```java
 getTop()
 getLeft()
 getRight()
 getBottom()
```

**MotionEvent提供的方法**

上面*View坐标系*的图中的*原点*，假设就是我们触摸的点。我们知道无论是View还是ViewGroup，最终的点击事件都会由`onTouchEvent(MotionEvent event)`方法来处理。MotionEvent在用户交互中作用重大，其内部提供了很多事件常量，比如我们常用的`ACTION_DOWN`、`ACTION_UP`和`ACTION_MOVE`。此外，`MotionEvent`也提供了获取焦点坐标的各种方法

+ `getX()`：获取点击事件距离控件左边的距离，即视图坐标
+ `getY()`：获取点击事件距离控件顶边的距离，即视图坐标
+ `getRawX()`：获取点击事件距离整个屏幕左边的距离，即绝对坐标
+ `getRawY()`：获取点击事件距离整个屏幕顶边的距离，即绝对坐标



## View的滑动

基本思想：当点击事件传到`View`时，系统记下触摸点的坐标，手指移动时系统记下移动后触摸的坐标并算出偏移量，并通过偏移量来修改`View`的坐标



### layout()方法实现

```java
public class CustomView extends View {

    private int lastX;
    private int lastY;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取手指触摸点的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移位置
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                //调用layout方法来重新放置它的位置
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                break;
        }
        return true;

    }
}
```

![012](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/012.gif)



### offsetLeftAndRight()和offsetTopAndBottom()

使用效果与`layout()`方法差不多

```java
            case MotionEvent.ACTION_MOVE:
                //计算偏移位置
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);
                break;
```



### LayoutParams

将`ACTION_MOVE`下的代码，修改为如下的形式：

```java
            case MotionEvent.ACTION_MOVE:
                //计算偏移位置
                int offsetX = x - lastX;
                int offsetY = y - lastY;

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)getLayoutParams();
                layoutParams.leftMargin = getLeft() + offsetX;
                layoutParams.topMargin = getTop() + offsetY;
                setLayoutParams(layoutParams);
                break;
```

这里因为父控件是`FrameLayout`，所以是`FrameLayout.LayoutParams`

也可以使用`ViewGroup.MarginLayoutParams`，如下：

```java
ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)getLayoutParams();
layoutParams.leftMargin = getLeft() + offsetX;
layoutParams.topMargin = getTop() + offsetY;
setLayoutParams(layoutParams);
```



### scrollTo()与scrollBy()

`scrollTo()`表示移动到一个具体的坐标点，`scrollBy()`表示移动的增量

```java
public void scrollBy(int x, int y) {
    scrollTo(mScrollX + x, mScrollY + y);
}
```

将`ACTION_MOVE`修改为如下的内容，同样可以达到效果：

```java
            case MotionEvent.ACTION_MOVE:
                //计算偏移位置
                int offsetX = x - lastX;
                int offsetY = y - lastY;

                ((View)getParent()).scrollBy(-offsetX, -offsetY);
                break;
```

> **为什么是负值？**
>
> 假设我们正用放大镜来看报纸，放大镜用来显示字的内容。同样我们可以把放大镜看作我们的手机屏幕，它们都是负责显示内容的；而报纸则可以被看作屏幕下的画布，它们都是用来提供内容的。放大镜外的内容，也就是报纸的内容不会随着放大镜的移动而消失，它一直存在。同样，我们的手机屏幕看不到的视图并不代表其不存在，如图3-6所示。画布上有3个控件，即Button、EditText 和 SwichButton。只有 Button 在手机屏幕中显示，它的坐标为（60，60）。现在我们调用scrollBy（50，50），按照字面的意思，这个 Button 应该会在屏幕右下侧，可是事实并非如此。如果我们调用scrollBy（50，50），里面的参数都是正值，我们的手机屏幕向 X 轴正方向，也就是向右边平移 50，然后手机屏幕向 Y 轴正方向，也就是向下方平移 50，平移后的效果如图3-7所示。虽然我们设置的数值是正数并且在X轴和Y轴的正方向移动，但 Button 却向相反方向移动了，这是参考对象不同导致的差异。所以我们用 scrollBy 方法的时候要设置负数才会达到自己想要的效果。
>
> ![013](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/013.png)













































