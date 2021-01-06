# AnimatorSet

`ValueAnimator`和ObjectAnimator只能单独实现一个动画

`AnimatorSet`提供了2个方法

+ `playSequentially` - 动画依次播放
+ `playTogether` - 动画一起开始



如下的`playSequentially`，动画依次开始：

```java
ObjectAnimator tv1Animator = ObjectAnimator.ofInt(mTv1, "BackgroundColor", 0xffff00ff, 0xffffff00, 0xffff00ff);
ObjectAnimator tv1TranslateY = ObjectAnimator.ofFloat(mTv1, "translationY", 0, 300, 0);
ObjectAnimator tv2TranslateY = ObjectAnimator.ofFloat(mTv2, "translationY", 0, 400, 0);

AnimatorSet set = new AnimatorSet();
set.playSequentially(tv1Animator, tv1TranslateY, tv2TranslateY);
set.setDuration(2000);
set.start();
```

![029](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/029.gif)



如下的`playTogether`，动画一起开始：

```java
ObjectAnimator tv1Animator = ObjectAnimator.ofInt(mTv1, "BackgroundColor", 0xffff00ff, 0xffffff00, 0xffff00ff);
ObjectAnimator tv1TranslateY = ObjectAnimator.ofFloat(mTv1, "translationY", 0, 300, 0);
ObjectAnimator tv2TranslateY = ObjectAnimator.ofFloat(mTv2, "translationY", 0, 400, 0);

AnimatorSet set = new AnimatorSet();
set.playTogether(tv1Animator, tv1TranslateY, tv2TranslateY);
set.setDuration(2000);
set.start();
```

![030](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/030.gif)



## `AnimatorSet.Builder`

虽然`playSequentially`和`playTogether`可以实现一起开始动画和依次开始动画，但并不能非常自由的组合动画。比如我们有三个动画A、B、C，想先播放C、然后同时播放A和B。

如下的效果，先位移，再改变颜色：

```java
                ObjectAnimator tv1Animator = ObjectAnimator.ofInt(mTv1, "BackgroundColor", 0xffff00ff, 0xffffff00, 0xffff00ff);
                ObjectAnimator tv1TranslateY = ObjectAnimator.ofFloat(mTv1, "translationY", 0, 300, 0);
                ObjectAnimator tv2TranslateY = ObjectAnimator.ofFloat(mTv2, "translationY", 0, 400, 0);

                AnimatorSet set = new AnimatorSet();
                AnimatorSet.Builder builder = set.play(tv1TranslateY);
                builder.with(tv2TranslateY);
                builder.before(tv1Animator);
                set.start();

```

![031](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/031.gif)



> ```java
> //要播放哪个动画
> public Builder play(Animator anim)
> //和前面的动画一起执行
> public Builder with(Animator anim)
> //
> public Builder before(Animator anim)
> //
> public Builder after(Animator anim)
> ```





















