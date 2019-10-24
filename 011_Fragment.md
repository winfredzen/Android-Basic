# Fragment

[fragment](https://developer.android.com/reference/android/app/Fragment):

> A Fragment is a piece of an application's user interface or behavior that can be placed in an `Activity`. Interaction with fragments is done through `FragmentManager`, which can be obtained via `Activity#getFragmentManager()` and `Fragment#getFragmentManager()`

托管:activity在其视图层级里提供一处位置，用来放置fragment视图 ,fragment本身没有在屏幕上显示视图的能力。因此，只有将它的视图放置在activity的视图层级结构中，fragment视图才能显示在屏幕上 

![019](https://github.com/winfredzen/Android-Basic/raw/master/images/019.png)

**两类 fragment** 

一个是系统内置的`android.app.Fragment`，一个是`support-v4`库中的`android.support.v4.app.Fragment`，建议使用支持库的

## 托管 UI fragment

为托管UI fragment，activity必须：

+ 在其布局中为fragment的视图安排位置 
+ 管理fragment实例的生命周期 

### Fragment生命周期

fragment生命周期与activity生命周期的一个关键区别就在于，**fragment的生命周期方法由托管activity而不是操作系统调用**。操作系统不关心activity用来管理视图的fragment。fragment的使用是activity内部的事情 

状态：

+ 运行状态 - 当一个碎片是可见的，并且它所关联的活动正处于运行状态时，该碎片也处于运行状态
+ 暂停状态 - 当一个活动进入暂停状态时(由于另一个未占满屏幕的活动被添加到了栈顶)，与它相关联的可见碎片就会进入到暂停状态
+ 停止状态 - 当一个活动进入停止状态，与它相关联的碎片就会进入到停止状态，或者通过调用`FragmentTransition`的`remove()`、 `repleace()`方法将碎片从活动中移除，但如果在事务提交前调用`addToBackStack()`方法，这时的碎片也会进入到停止状态。进入停止状态的碎片对用户来说是完全不可见的，有可能会被系统回收
+ 销毁状态 - 碎片总是依附于活动而存在，因此当活动被销毁时，与它相关联的碎片就会进入到销毁状态。或者通过调用`FragmentTransition`的`remove()`、 `repleace()`方法将碎片从活动中移除，当在事物提交之前并没有调用`addToBackStack()`方法，这时的碎片也会进入到销毁状态

生命周期，来自官网[片段](https://developer.android.com/guide/components/fragments):

![022](https://github.com/winfredzen/Android-Basic/raw/master/images/022.png)

![023](https://github.com/winfredzen/Android-Basic/raw/master/images/023.png)

