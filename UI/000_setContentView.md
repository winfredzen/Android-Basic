# setContentView

在`Activity`的`onCreate`方法中，基本都有`setContentView(getLayoutId());`这样的调用

在其它项目的代码中，发现有如下的调用：

```java
    /**
     * 获取 Dialog 的根布局
     */
    public View getContentView() {
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView instanceof ViewGroup &&
                ((ViewGroup) contentView).getChildCount() == 1) {
            return ((ViewGroup) contentView).getChildAt(0);
        }
        return contentView;
    }
```

这里面的`Window.ID_ANDROID_CONTENT`表示什么呢？

在`Window.java`中`Window.ID_ANDROID_CONTENT`定义如下，表示的是*XML 布局文件中的主布局应具有的 ID*

```java
    /**
     * The ID that the main layout in the XML layout file should have.
     */
    public static final int ID_ANDROID_CONTENT = com.android.internal.R.id.content;
```

怎么理解呢？

先参考如下的文章：

+ [setContentView背后的故事](https://juejin.cn/post/6844903511390420999)



`AppCompatActivity`中的`setContentView`

```java
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        initViewTreeOwners();
        getDelegate().setContentView(layoutResID);
    }
```

`getDelegate`方法表示如下：

```java
    /**
     * @return The {@link AppCompatDelegate} being used by this Activity.
     */
    @NonNull
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, this);
        }
        return mDelegate;
    }
```

其中`AppCompatDelegate.create(this, this)`返回的是一个`AppCompatDelegateImpl`

```java
    @NonNull
    public static AppCompatDelegate create(@NonNull Activity activity,
            @Nullable AppCompatCallback callback) {
        return new AppCompatDelegateImpl(activity, callback);
    }
```

所以看下`AppCompatDelegateImpl`中的`setContentView`方法

```java
    @Override
    public void setContentView(View v) {
        ensureSubDecor();
        ViewGroup contentParent = mSubDecor.findViewById(android.R.id.content);
        contentParent.removeAllViews();
        contentParent.addView(v);
        mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    @Override
    public void setContentView(int resId) {
        ensureSubDecor();
        ViewGroup contentParent = mSubDecor.findViewById(android.R.id.content);
        contentParent.removeAllViews();
        LayoutInflater.from(mContext).inflate(resId, contentParent);
        mAppCompatWindowCallback.getWrapped().onContentChanged();
    }
```

**可见是将布局加载到mContentParent中**



借用上面文章的图片：

最后继承AppCompatActivity的布局结构：

![038](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/038.png)















































