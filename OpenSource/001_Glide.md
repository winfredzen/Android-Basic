# Glide

如果不使用Glide该怎样加载图片呢？如下的方式：

```java
public class MainActivity extends AppCompatActivity {

    private ImageView mIv;

    private Handler hander = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 200:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    mIv.setImageBitmap(bitmap);
                    break;
                default:
                    mIv.setImageResource(R.mipmap.loader_error);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIv = findViewById(R.id.iv);
    }

    public void onLoadImageClick(View view) {
        loadUrlImage("https://img4.mukewang.com/6013c1a9092857c002720144.png");
    }

    /**
     * 加载网络图片
     * @param img
     */
    private void loadUrlImage(String img) {

        mIv.setImageResource(R.mipmap.loading);

        new Thread() {
            @Override
            public void run() {
                super.run();

                Message message = new Message();

                try {
                    URL url = new URL(img);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        //获取数据流
                        InputStream inputStream = httpURLConnection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        message.obj = bitmap;
                        message.what = 200;

                    } else {
                        message.what = code;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = -1;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = -1;
                } finally {
                    hander.sendMessage(message); //发送消息
                }

            }
        }.start();
    }

}
```

如果网络加载的图片比较大，会出现什么情况呢？

如`http://res.lgdsunday.club/big_img.jpg`，会出现如下的异常：

![009](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/009.png)

还有一个问题是，**如果在Android 9及以上的系统，请求http图片，会失败**

![010](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/010.png)

> 由于 Android P 限制了明文流量的网络请求，非加密的流量请求都会被系统禁止掉。



## 使用

教程：

+ [关于 Glide](https://muyangmin.github.io/glide-docs-cn/)

> [Glide](https://github.com/bumptech/glide) 支持拉取，解码和展示视频快照，图片，和GIF动画。Glide的Api是如此的灵活，开发者甚至可以插入和替换成自己喜爱的任何网络栈。默认情况下，Glide使用的是一个定制化的基于`HttpUrlConnection`的栈，但同时也提供了与Google Volley和Square OkHttp快速集成的工具库。
>
> 虽然Glide 的主要目标是让任何形式的图片列表的滚动尽可能地变得更快、更平滑，但实际上，Glide几乎能满足你对远程图片的拉取/缩放/显示的一切需求。

引入Glid：

```groovy
implementation 'com.github.bumptech.glide:glide:4.12.0'
```

基本的使用方式：

```java
Glide.with(fragment)
    .load(url)
    .into(imageView);
```

+ `.with()` - 创建图片加载实例
+ `.load(url)` - 指定加载的图片资源
+ `.into()` - 指定控件

取消加载：

```java
Glide.with(fragment).clear(imageView);
```



可对`Glide`进行某些配置，如占位图片、失败图片等

```java
    private void glideloadImage(String img) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.loader_error)
                .circleCrop();

        Glide.with(this)
                .load(img)
                .apply(options)
                .into(mIv);
    }
```

![011](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/011.png)



**有个问题是，在许多地方都需要对`RequestOptions`进行配置，该如何处理呢？**

1.创建`Utils`类

```java
public class GlideOptionsUtils {

    public static RequestOptions baseOptions() {
        return new RequestOptions()
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.loader_error);
    }

    public static RequestOptions circleCropOptions() {
        return baseOptions().circleCrop();
    }

}
```



**2.使用Generated API** 

参考：

+ [Generated API](https://muyangmin.github.io/glide-docs-cn/doc/generatedapi.html)



> Generated API 模式的设计出于以下两个目的：
>
> 1. 集成库可以为 Generated API 扩展自定义选项。
>
> 2. 在 Application 模块中可将常用的选项组打包成一个选项在 Generated API 中使用



**使用流程**

+ 引入Generated API的支持库
+ 创建类，继承`AppGlideModule`并添加`@GlideModule`注解
+ 创建类，添加`@GlideExtension`注解，并实现`private`构造函数



1.添加 Glide 注解处理器的依赖：

```groovy
repositories {
  mavenCentral()
}

dependencies {
  annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
}
```

2.创建`MyAppGlideModule`

```java
/**
 * 生成GlideApp对象
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
}
```

3.创建`MyGlideExtension`

```java
@GlideExtension
public class MyGlideExtension {
    private MyGlideExtension() {

    }

    /**
     * 全局统一的配置
     * @param options
     */
    @NonNull
    @GlideOption
    public static BaseRequestOptions<?> injectOptions(BaseRequestOptions<?> options) {
       return options.placeholder(R.mipmap.loading)
                .error(R.mipmap.loader_error)
                .circleCrop();
    }
}
```

使用方式：

```java
    private void glideAppLoadUrlImage(String url) {
        GlideApp.with(this)
                .load(url)
                .injectOptions()
                .into(mIv);
    }
```









































