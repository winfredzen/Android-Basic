# Launcher 点击All App启动

可参考：

+ [App启动流程【1】Launcher 启动 App](https://juejin.cn/post/7131999006003036173)



**1.All App界面的点击事件是如何设置的？**

All App列表界面类型为`LauncherAllAppsContainerView`，继承自`AllAppsContainerView`

`AllAppsContainerView`中的内部类`AdapterHolder`，持有`AllAppsGridAdapter`和`AllAppsRecyclerView`

![003](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/003.png)



在`AdapterHolder`的`setup`方法中，将`adapter`设置给`recyclerView`

```java
void setup(@NonNull View rv, @Nullable ItemInfoMatcher matcher) {
    mInfoMatcher = matcher;
    appsList.updateItemFilter(matcher);
    recyclerView = (AllAppsRecyclerView) rv;
    recyclerView.setEdgeEffectFactory(createEdgeEffectFactory());
    recyclerView.setApps(appsList);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter); //设置适配器
    recyclerView.setHasFixedSize(true);
    // No animations will occur when changes occur to the items in this RecyclerView.
    recyclerView.setItemAnimator(null);
    FocusedItemDecorator focusedItemDecorator = new FocusedItemDecorator(recyclerView);
    recyclerView.addItemDecoration(focusedItemDecorator);
    adapter.setIconFocusListener(focusedItemDecorator.getFocusListener());
    applyVerticalFadingEdgeEnabled(verticalFadingEdge);
    applyPadding();
    setupOverlay();
}
```



在`AllAppsGridAdapter`的`onCreateViewHolder`中，给`BubbleTextView`设置`OnClickListener`

![004](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/004.png)



这个`mOnIconClickListener`，是在`AllAppsGridAdapter`的初始化方法中设置的：

![005](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/005.png)



而`BaseDraggingActivity`的`getItemOnClickListener`方法如下：

```java
public OnClickListener getItemOnClickListener() {
    return ItemClickHandler.INSTANCE;
}
```

`ItemClickHandler.INSTANCE`返回的就是一个lambda表达式

```java
/**
 * Instance used for click handling on items
 */
public static final OnClickListener INSTANCE = getInstance(null);
public static final OnClickListener getInstance(String sourceContainer) {
    return v -> onClick(v, sourceContainer);
}
```



**所以最终的点击事件是由`ItemClickHandler`来处理的**

`onClick(View v, String sourceContainer)`方法如下：

![006](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/006.png)

` startAppShortcutOrInfoActivity(View v, ItemInfo item, Launcher launcher, @Nullable String sourceContainer)`方法如下：

![007](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/007.png)



调用过程如下：

![008](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/008.png)









