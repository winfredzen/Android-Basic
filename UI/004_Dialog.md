# Dialog

基本使用，参考：

+ [对话框](https://developer.android.com/guide/topics/ui/dialogs?hl=zh-cn)
+ [Android常用对话框大全——Dialog](https://blog.csdn.net/a_zhon/article/details/54578047)



这里指的是完全自定义一个dialog

我参考到的是：[项目需求讨论-Android 自定义Dialog实现步骤及封装](https://www.jianshu.com/p/64446940eccf)

一步步按作者的操作，显示效果如下，本来在layout中，显示效果如下：

![003](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/003.png)

但实际在我的OPPO手机上，显示效果如下：

![004](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/004.png)

按文章进行如下的设置后：

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(context, R.layout.wz_custom_dialog_layout, null);
        setContentView(view);

        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.height = DensityUtil.dp2px(context,250);
        lp.width = DensityUtil.dp2px(context,200);
        win.setAttributes(lp);

    }
```

此时dialog显示效果如下：

![005](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/005.png)



另外在我们的Android App中，好像并没有设置`Window`相关的操作，但其显示也是正常的

![006](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/006.png)

我按照上述的方式设置后，其显示也是正常的



自定义dialog可参考：

+ [Android自定义Dialog对话框](https://www.jianshu.com/p/42cebea746e7)
+ [自定义Dialog的详细步骤（实现自定义样式一般原理）](https://blog.csdn.net/oQiHaoGongYuan/article/details/50958659)















