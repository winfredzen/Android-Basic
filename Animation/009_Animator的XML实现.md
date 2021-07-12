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





































