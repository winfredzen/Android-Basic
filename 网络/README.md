# OkHttp&Retrofit



## OkHttp

OkHttp的官方教程：

+ [okhttp](https://square.github.io/okhttp/)

[okhttp](https://github.com/square/okhttp)的使用流程

+ 创建请求 - `Request.Builder()` -> `Request`对象
+ 通过`Request`对象得到`Call`对象 - `client.newCall(request)` -> `Call`对象
+ 执行`Call` - 同步`call.execute()`，异步`call.enqueue()`
+ 得到`Response`对象



### Get请求

**1.同步请求**

如下的方式，创建一个同步请求的例子，注意添加权限：

> ```xml
> <uses-permission android:name="android.permission.INTERNET" />
> ```

```java
    public String doGet(String url) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            Response response  = call.execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
```

如果直接在UI线程中，调用`doGet()`方法，会产生如下的异常，原因是网络请求是一个耗时的操作：

![005](https://github.com/winfredzen/Android-Basic/blob/master/%E7%BD%91%E7%BB%9C/images/005.png)

可以将请求的过程更改为如下的形式，使用`Handler`：

```java
private Handler mUiHandler = new Handler(Looper.getMainLooper());
......
new Thread() {
    @Override
    public void run() {
        String content = OkHttpUtils.getInstance().doGet("http://www.imooc.com/api/okhttp/getmethod");
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mTvContent.setText(content);
            }
        });
    }
}.start();
```

还可能出现的一个问题是，**从Android P开始，默认不再允许直接访问HTTP请求**

![006](https://github.com/winfredzen/Android-Basic/blob/master/%E7%BD%91%E7%BB%9C/images/006.png)

**需要通过设置Network Security Configuration支持**，具体的过程可参考官方文档：

+ [网络安全配置](https://developer.android.com/training/articles/security-config?hl=zh-cn)

在res目录下，创建一个xml目录，在xml目录下，创建一个`network_security_config.xml`文件，内容如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" />
            <certificates src="user" />
        </trust-anchors>
    </base-config>
</network-security-config>
```

然后在`AndroidManifest.xml`中引用这个文件

```xml
    <application
        ...
        android:networkSecurityConfig="@xml/network_security_config"
        ...>
```



**2.异步请求**











































