# ViewPager+Tab特效实现微信主界面

参考：

+ [ViewPager+Tab特效实现微信主界面](https://www.imooc.com/learn/1116)



Fragment的初始化方法最佳实践，通过`newInstance`来创建一个Fragment，通过`setArguments`来传递参数，如下面对的例子：

```java
    private static final String BUNDLE_KEY_TITLE = "key_title";
    public static TabFragment newInstance(String title){
        //传递参数
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TITLE, title);

        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle); //在界面的销毁和恢复过程中有很重要的作用
        return tabFragment;

    }
```



为什么不通过new来创建，通过set方法来设置参数呢？如

![001](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/001.png)

这样做的缺点是：应用在后台时，可能会被kill。用户回到应用时，应用会有一个自动恢复的机制。会调用默认的构造方法，然后走生命周期。但系统没法去调用set方法来设置参数。但系统在重建的时候，`setArguments`里面的是可以恢复的



## FragmentPagerAdapter vs FragmentStatePagerAdapter

重写`onDestroyView()`和`onDestroy()`等方法，如下：

```java
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        L.d("onDestroyView, title = " + mTitle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d("onDestroy, title = " + mTitle);
    }

```

当为`FragmentPagerAdapter`时，滑动ViewPager，输出结果如下：

```xml
2020-07-20 15:34:56.356 5476-5476/com.example.imooc_wechat D/wangzhen: onCreate, title = 微信
2020-07-20 15:34:56.357 5476-5476/com.example.imooc_wechat D/wangzhen: onCreate, title = 通讯录
2020-07-20 15:34:56.357 5476-5476/com.example.imooc_wechat D/wangzhen: onCreateView, title = 微信
2020-07-20 15:34:56.364 5476-5476/com.example.imooc_wechat D/wangzhen: onCreateView, title = 通讯录
2020-07-20 15:34:59.816 5476-5476/com.example.imooc_wechat D/wangzhen: onCreate, title = 发现
2020-07-20 15:34:59.817 5476-5476/com.example.imooc_wechat D/wangzhen: onCreateView, title = 发现
2020-07-20 15:35:01.121 5476-5476/com.example.imooc_wechat D/wangzhen: onCreate, title = 我
2020-07-20 15:35:01.123 5476-5476/com.example.imooc_wechat D/wangzhen: onDestroyView, title = 微信
2020-07-20 15:35:01.126 5476-5476/com.example.imooc_wechat D/wangzhen: onCreateView, title = 我
```

可见在`onDestroyView`被调用了



当为`FragmentStatePagerAdapter`时

```xml
2020-07-20 15:37:13.652 5731-5731/com.example.imooc_wechat D/wangzhen: onCreate, title = 微信
2020-07-20 15:37:13.653 5731-5731/com.example.imooc_wechat D/wangzhen: onCreate, title = 通讯录
2020-07-20 15:37:13.654 5731-5731/com.example.imooc_wechat D/wangzhen: onCreateView, title = 微信
2020-07-20 15:37:13.659 5731-5731/com.example.imooc_wechat D/wangzhen: onCreateView, title = 通讯录
2020-07-20 15:37:19.009 5731-5731/com.example.imooc_wechat D/wangzhen: onCreate, title = 发现
2020-07-20 15:37:19.010 5731-5731/com.example.imooc_wechat D/wangzhen: onCreateView, title = 发现
2020-07-20 15:37:20.417 5731-5731/com.example.imooc_wechat D/wangzhen: onCreate, title = 我
2020-07-20 15:37:20.418 5731-5731/com.example.imooc_wechat D/wangzhen: onDestroyView, title = 微信
2020-07-20 15:37:20.420 5731-5731/com.example.imooc_wechat D/wangzhen: onDestroy, title = 微信
2020-07-20 15:37:20.420 5731-5731/com.example.imooc_wechat D/wangzhen: onCreateView, title = 我
```

可见`onDestroyView`、`onDestroy`都被调用了



区别：

+ `FragmentPagerAdapter` - Fragment并没有被销毁
  + `onDestroyView`
  + `onCreateView`
+ `FragmentStatePagerAdapter` - Fragment被销毁
  + `onDestroyView`
  + `onDestroy`
  + `onCreate`
  + `onCreateView`



通过设置`mVpMain.setOffscreenPageLimit(mTitles.size())`来设置缓存



## Activity调用Fragment

在Fragment中暴露给外部调用的方法，这些方法要操作内部的view时，注意要判断

```java
    public void changeTitle(String title) {
        if (!isAdded()) {//注意要判断
            return;
        }
        mTvTitle.setText(title);
    }
```



如何获取Fragment？

在原来，可能会使用如下的方式来创建Fragment，如下，一开始就创建了4个`TabFragment`：

![002](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/002.png)

启动时输出如下：

```xml
2020-07-20 15:54:27.583 6360-6360/com.example.imooc_wechat D/wangzhen: activity onCreate
2020-07-20 15:54:27.626 6360-6360/com.example.imooc_wechat D/wangzhen: Fragement getItem i = 0
2020-07-20 15:54:27.628 6360-6360/com.example.imooc_wechat D/wangzhen: Fragement getItem i = 1
2020-07-20 15:54:27.629 6360-6360/com.example.imooc_wechat D/wangzhen: Fragement getItem i = 2
2020-07-20 15:54:27.630 6360-6360/com.example.imooc_wechat D/wangzhen: Fragement getItem i = 3
```

但此时若屏幕发生了旋转：

```xml
2020-07-20 15:59:53.034 6360-6360/com.example.imooc_wechat D/wangzhen: activity onCreate
```

可见`onCreate`有被重新调用了，此时`mFragments`列表中，也会重新创建4个新的`TabFragment`，而且`FragmentPagerAdapter`也不会调用`getItem`方法。此时`mFragments.get(0);`去取第0个Fragment，和正在屏幕上显示的第0个，完全不是同一个Fragment

为什么会这样？

> FragmentPagerAdapter中有个FragmentManager，通过FragmentManager来管理Fragment。Fragment有一个特性，在发生旋转重建时，是可以去恢复上一次的Fragment的。



可以使用如下的方式，使用`SparseArray`（类似于map），和`FragmentPagerAdapter`里面的方法来保存Fragment

```java
private SparseArray<TabFragment> mFragments = new SparseArray<>();
```

![003](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/003.png)

取Fragment：

```xml
        mBtnWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabFragment fragment = mFragments.get(0);
                if (fragment != null) {
                    fragment.changeTitle("微信 changed!");
                }
            }
        });
```

































