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



`playSequentially`与`playTogether`的**区别**

> `playTogether`函数只负责在同一时间点把门打开，门打开以后，马跑不跑，那是它自己的事情；马回不回来，门也不管
>
> `playSequentially`，当一匹马回来以后，再放另一匹马

如下的例子：

```java
                ObjectAnimator tv1Animator = ObjectAnimator.ofInt(mTv1, "BackgroundColor", 0xffff00ff, 0xffffff00, 0xffff00ff);

                ObjectAnimator tv1TranslateY = ObjectAnimator.ofFloat(mTv1, "translationY", 0, 300, 0);
                tv1TranslateY.setStartDelay(2000);
                tv1TranslateY.setRepeatCount(ValueAnimator.INFINITE);

                ObjectAnimator tv2TranslateY = ObjectAnimator.ofFloat(mTv2, "translationY", 0, 400, 0);
                tv2TranslateY.setStartDelay(2000);

                AnimatorSet set = new AnimatorSet();
                set.playTogether(tv1Animator, tv1TranslateY, tv2TranslateY);
                set.setDuration(2000);
                set.start();
```

![033](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/033.gif)

> 先是tv1的颜色变化，在颜色变化以后，tv2的延时也刚好结束，此时两个TextView开始做位移的变换。最后，TextView-1变化是无限循环的



```java
                ObjectAnimator tv1Animator = ObjectAnimator.ofInt(mTv1, "BackgroundColor", 0xffff00ff, 0xffffff00, 0xffff00ff);
                tv1Animator.setStartDelay(2000);

                ObjectAnimator tv1TranslateY = ObjectAnimator.ofFloat(mTv1, "translationY", 0, 300, 0);
                tv1TranslateY.setRepeatCount(ValueAnimator.INFINITE);

                ObjectAnimator tv2TranslateY = ObjectAnimator.ofFloat(mTv2, "translationY", 0, 400, 0);

                AnimatorSet set = new AnimatorSet();
                set.playSequentially(tv1Animator, tv1TranslateY, tv2TranslateY);
                set.setDuration(2000);
                set.start();

```

![034](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/034.gif)

> 上面的例子，`tv2TranslateY`动画永远不会开始





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
> //先执行这个动画，再执行前面的动画
> public Builder before(Animator anim)
> //先执行前面的动画后才执行该动画
> public Builder after(Animator anim)
> //延迟n毫秒之后执行动画
> public Builder after(long delay)
> ```



## AnimatorSet监听器

```java
    public static interface AnimatorListener {

        /**
         * <p>Notifies the start of the animation as well as the animation's overall play direction.
         * This method's default behavior is to call {@link #onAnimationStart(Animator)}. This
         * method can be overridden, though not required, to get the additional play direction info
         * when an animation starts. Skipping calling super when overriding this method results in
         * {@link #onAnimationStart(Animator)} not getting called.
         *
         * @param animation The started animation.
         * @param isReverse Whether the animation is playing in reverse.
         */
        default void onAnimationStart(Animator animation, boolean isReverse) {
            onAnimationStart(animation);
        }

        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * <p>This method's default behavior is to call {@link #onAnimationEnd(Animator)}. This
         * method can be overridden, though not required, to get the additional play direction info
         * when an animation ends. Skipping calling super when overriding this method results in
         * {@link #onAnimationEnd(Animator)} not getting called.
         *
         * @param animation The animation which reached its end.
         * @param isReverse Whether the animation is playing in reverse.
         */
        default void onAnimationEnd(Animator animation, boolean isReverse) {
            onAnimationEnd(animation);
        }

        /**
         * <p>Notifies the start of the animation.</p>
         *
         * @param animation The started animation.
         */
        void onAnimationStart(Animator animation);

        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animation The animation which reached its end.
         */
        void onAnimationEnd(Animator animation);

        /**
         * <p>Notifies the cancellation of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         *
         * @param animation The animation which was canceled.
         */
        void onAnimationCancel(Animator animation);

        /**
         * <p>Notifies the repetition of the animation.</p>
         *
         * @param animation The animation which was repeated.
         */
        void onAnimationRepeat(Animator animation);
    }

```

















































