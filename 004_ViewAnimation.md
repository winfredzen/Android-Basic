# ViewAnimation

这里的ViewAnimation指的是[Tween animation](https://developer.android.com/guide/topics/resources/animation-resource.html#Tween)

主要有四种动画：

|   类型    |           说明           |
| :-------: | :----------------------: |
|   scale   |   渐变尺寸伸缩动画效果   |
| translate | 画面转换位置移动动画效果 |
|  rotate   |   画面转移旋转动画效果   |
|   alpha   |    渐变透明度动画效果    |

animation的xml文件位于`res/anim/`文件夹下，文件必须只有一个单一根元素，可以为`<alpha>`, `<scale>`, `<translate>`, `<rotate>` 或者`<set>`

下面的例子来自[Make your apps come alive! Use Animation](https://www.101apps.co.za/articles/using-view-animations-in-your-apps-a-tutorial.html)

属性值的说明可参考：

+ [alpha、scale、translate、rotate、set 的 xml 属性及用法](http://wiki.jikexueyuan.com/project/android-animation/1.html)

## sacle

scale进行缩放，如下的例子，在`res/anim/`文件下创建`scale_animation.xml`文件，内容如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<scale
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="5000"
    android:fromXScale="0.1"
    android:fromYScale="0.1"
    android:interpolator="@android:anim/accelerate_interpolator"
    android:pivotX="50%"
    android:pivotY="50%"
    android:toXScale="1.0"
    android:toYScale="1.0">


</scale>
```

效果如下：

![001](![001](https://github.com/winfredzen/Android-Basic/raw/master/images/002.gif))

属性说明：

+ android:pivotX - 缩放起点 X 轴坐标，可以是数值、百分数、百分数 p 三种样式，比如 50、50%、50%p，当为数值时，表示在当前 View 的左上角，即原点处加上 50px，做为起始缩放点；如果是 50%，表示在当前控件的左上角加上自己宽度的 50%做为起始点；如果是 50%p，那么就是表示在当前的左上角加上父控件宽度的 50%做为起始点 x 轴坐标

```java
    public void doScale(View view) {

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.eye);
        imageView.clearAnimation(); //取消view动画


        //居中
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(layoutParams);

        Animation scaleAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_animation);
        imageView.setAnimation(scaleAnimation);
    }
```



## Rotate

这里使用了`set`标签，表示将2个旋转动画放在一起，`rotate_animation.xml`如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <rotate xmlns:android="http://schemas.android.com/apk/res/android"
        android:duration="5000"
        android:fromDegrees="0"
        android:interpolator="@android:anim/accelerate_interpolator"
        android:pivotY="50%"
        android:pivotX="50%"
        android:toDegrees="360">

    </rotate>

    <rotate xmlns:android="http://schemas.android.com/apk/res/android"
        android:duration="2000"
        android:fromDegrees="360"
        android:interpolator="@android:anim/accelerate_interpolator"
        android:pivotY="50%"
        android:pivotX="50%"
        android:startOffset="5000"
        android:toDegrees="0">

    </rotate>
</set>
```

效果如下：

![002](https://github.com/winfredzen/Android-Basic/raw/master/images/003.gif)



## Alpha

alpha进行透明度动画，`alpha_animation.xml`定义如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="5000"
    android:toAlpha="1.0"
    android:fromAlpha="0.0"
    android:interpolator="@android:anim/accelerate_interpolator">

</alpha>
```

效果如下：

![003](https://github.com/winfredzen/Android-Basic/raw/master/images/004.gif)



## Translate

平移动画，例子是使图片从左上角运动到右下角：

```java
    public void doTranslate(View view) {

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.stairs);
        imageView.clearAnimation(); //取消view动画

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        imageView.setLayoutParams(layoutParams);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        int top = relativeLayout.getTop();
        int left = relativeLayout.getLeft();
        int bottom = relativeLayout.getBottom();
        int right = relativeLayout.getRight();

        TranslateAnimation translateAnimation = new TranslateAnimation(left, right, top, bottom);
        translateAnimation.setDuration(4000);
        translateAnimation.setRepeatCount(0);
        translateAnimation.setInterpolator(new AccelerateInterpolator());

        imageView.startAnimation(translateAnimation);

    }
```

效果如下：

![004](https://github.com/winfredzen/Android-Basic/raw/master/images/005.gif)























