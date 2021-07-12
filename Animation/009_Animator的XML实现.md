# Animator的XML实现

## `animator`标签

`animator`标签对应`ValueAnimator`

在`res/animator`下创建如下的`animator.xml`文件

```java
<?xml version="1.0" encoding="utf-8"?>
<animator xmlns:android="http://schemas.android.com/apk/res/android"
    android:valueFrom="0"
    android:valueTo="300"
    android:duration="1000"
    android:valueType="intType"
    android:interpolator="@android:anim/linear_interpolator"
    >

</animator>
```

通过`AnimatorInflater`加载动画

```java
 ValueAnimator valueAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.animator);
 valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
     @Override
     public void onAnimationUpdate(ValueAnimator animation) {
         int offset = (int) animation.getAnimatedValue();
         mTextView1.layout(offset, offset, mTextView1.getWidth() + offset, mTextView1.getHeight() + offset);
     }
 });
 valueAnimator.start();
```

效果如下：

![036](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/036.gif)



## `objectAnimator`标签

`objectAnimator`标签对应的是`ObjectAnimator`

如下，在在`res/animator`下创建如下的`object_animator.xml`文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<objectAnimator xmlns:android="http://schemas.android.com/apk/res/android"
    android:propertyName="TranslationY"
    android:duration="2000"
    android:valueFrom="0.0"
    android:valueTo="400.0"
    android:interpolator="@android:anim/accelerate_interpolator"
    android:valueType="floatType"
    android:repeatCount="1"
    android:repeatMode="reverse"
    android:startOffset="2000"
    >

</objectAnimator>
```

通过`AnimatorInflater`加载动画

```java
 ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.object_animator);
 objectAnimator.setTarget(mTextView1);//绑定动画目标
 objectAnimator.start();
```

在单击按钮后，延时2000ms开始动画，然后倒序重复运行1次

效果如下：

![037](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/037.gif)



## `set`标签

`set`标签对应`AnimationSet`

























