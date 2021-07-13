# ViewPropertyAnimator

`ViewPropertyAnimator`我自己理解，就是使用起来更方便，代码更简洁

如，下面的动画效果：

![040](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/040.gif)

将TextView移动到`(50, 100)`这个位置

如果使用`ObjectAnimator`

```java
ObjectAnimator animX = ObjectAnimator.ofFloat(mTextView, "x", 50f);
ObjectAnimator animY = ObjectAnimator.ofFloat(mTextView, "y", 100f);
AnimatorSet animatorSet = new AnimatorSet();
animatorSet.playTogether(animX, animY);
animatorSet.start();
```

如果使用`PropertyValuesHolder`

```java
PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", 50f);
PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 100f);
ObjectAnimator.ofPropertyValuesHolder(mTextView, pvhX, pvhY).start();
```

而如果使用`ViewPropertyAnimator`，则只需要一行代码：

```java
mTextView.animate().x(50f).y(100f);
```

> `animate()`方法会返回一个`ViewPropertyAnimator`对象



> `ObjectAnimator`可以灵活、方便地为任何对象和属性做动画。但当需要同时为View的多个属性（SDK提供、非自定义扩展）做动画时，`ViewPropertyAnimator`会更方便
>
> 使用`ObjectAnimator`时并不需要太过担心性能，使用反射和JNI等技术所带来的开销相对于整个程序来讲是微不足道的。使用`ViewPropertyAnimator`最大的优势也不在于性能的提升，而是它提供的简明易读的代码书写方式

























































