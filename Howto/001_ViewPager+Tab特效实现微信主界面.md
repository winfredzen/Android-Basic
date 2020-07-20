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