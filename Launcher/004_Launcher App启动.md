# Launcher App启动

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













