# NumberPickerView

[NumberPickerView](https://github.com/Carbs0126/NumberPickerView)是一个选择器，在我当前的项目中用到了，就了解下吧

so，**如何滚动？**

> `Scroller` + `VelocityTracker` + `onDraw(Canvas canvas)`

[VelocityTracker](https://developer.android.com/reference/android/view/VelocityTracker):

> Helper for tracking the velocity of touch events, for implementing flinging and other such gestures. Use `obtain()` to retrieve a new instance of the class when you are going to begin tracking. Put the motion events you receive into it with `addMovement(android.view.MotionEvent)`. When you want to determine the velocity call `computeCurrentVelocity(int)` and then call `getXVelocity(int)` and `getYVelocity(int)` to retrieve the velocity for each pointer id.

可参考：

+ [Android VelocityTracker获取滑动速度](https://www.jianshu.com/p/e77704b59379)

