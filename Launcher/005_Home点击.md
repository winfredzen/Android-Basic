# Home点击

在Launcher上点击Home键，可以回到Launcher主页面



## 布局

这个地方有点疑问，看网络上的文章介绍说，与`KeyButtonView`有关，`KeyButtonView`是`frameworks/base/packages/SystemUI`中的

其中的类关系大致如下：

![009](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/009.png)

在`NavigationBarInflaterView`的`createView`方法中，home使用的是`R.layout.home`

![010](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/010.png)

`home.xml`的布局为：

```xml
<com.android.systemui.navigationbar.buttons.KeyButtonView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:systemui="http://schemas.android.com/apk/res-auto"
    android:id="@+id/home"
    android:layout_width="@dimen/navigation_key_width"
    android:layout_height="match_parent"
    android:layout_weight="0"
    systemui:keyCode="3"
    android:scaleType="center"
    android:contentDescription="@string/accessibility_home"
    android:paddingStart="@dimen/navigation_key_padding"
    android:paddingEnd="@dimen/navigation_key_padding"
    />
```

其**keyCode等于3**



## 事件处理

这个地方也有疑问，按文章和教程的说法，SystemUI的导航栏发出来的按键事件，通过一系列的转发到，关键节点窗口管理器`PhoneWindowManager`中

在`KeyButtonView`的`onTouchEvent`方法中，事件会调用`sendEvent`方法

最终会调用 `mInputManager.injectInputEvent`方法

![011](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/011.png)



### PhoneWindowManager

`PhoneWindowManager`的调用过程分析（不一定正确）

对于Home按键（keycode为3），`PhoneWindowManager`会通过`interceptKeyBeforeDispatching`方法进行一些处理

![012](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/012.png)

`RootWindowContainer`中的`startHomeOnTaskDisplayArea`方法，有调用`mService.getHomeIntent()`

其调用的是`ActivityTaskManagerService`中的方法：

```java
    Intent getHomeIntent() {
        Intent intent = new Intent(mTopAction, mTopData != null ? Uri.parse(mTopData) : null);
        intent.setComponent(mTopComponent);
        intent.addFlags(Intent.FLAG_DEBUG_TRIAGED_MISSING);
        if (mFactoryTest != FactoryTest.FACTORY_TEST_LOW_LEVEL) {
            intent.addCategory(Intent.CATEGORY_HOME);
        }
        return intent;
    }
```

```java
String mTopAction = Intent.ACTION_MAIN;
```



## Maybe

在`PhoneWindowManager`中有个 `Intent mHomeIntent`变量，其创建过程如下：

```java
mHomeIntent =  new Intent(Intent.ACTION_MAIN, null);
mHomeIntent.addCategory(Intent.CATEGORY_HOME);
mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
```







## 其它

可参考：

+ [Android R Navigationbar的创建，图标更新，加载流程](https://blog.csdn.net/ChaoLi_Chen/article/details/120398587)























