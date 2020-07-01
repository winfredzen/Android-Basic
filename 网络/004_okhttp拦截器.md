# okhttp拦截器

参考：

+ [Interceptors](https://square.github.io/okhttp/interceptors/)
+ [OkHttp 同步异步操作](https://www.shuzhiduo.com/A/kPzOgxWQzx/)



官方的说明图

![003](https://github.com/winfredzen/Android-Basic/blob/master/网络/images/003.png)

拦截器分为：

+ 应用拦截器
+ 网络拦截器



## Application Interceptors

通过`OkHttpClient.Builder`的`addInterceptor()`方法来注册应用拦截器

如官网中的例子`LogginInterceptor`

```java
public class LogginInterceptor implements Interceptor {

    private static final String TAG = "LogginInterceptor";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();

        long t1 = System.nanoTime();//纳秒
        Log.d(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        /**
         * 1e6d is 1 * 10^6 represented as a double
         */

        long t2 = System.nanoTime();
        Log.d(TAG ,String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }

}
```

注册

```java
OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LogginInterceptor()).build(); //测试拦截器
```

拦截效果如下：

![004](https://github.com/winfredzen/Android-Basic/blob/master/网络/images/004.png)



## Network Interceptors

网络拦截器使用`addNetworkInterceptor()`方法



## 应用拦截器和网络拦截器的比较

> 每个拦截器由它各自的优势。 
> 应用拦截器 
> \- 不需要考虑中间状态的响应，比如重定向或者重试。 
> \- 只会被调用一次，甚至于HTTP响应保存在缓存中。 
> \- 观察应用程序的原意。 
> \- 允许短路，可以不调用Chain.proceed()方法 
> \- 允许重试和发送多条请求，调用Chain.proceed()方法 
> 网络拦截器 
> \- 可以操作中间状态的响应，比如重定向和重试 
> \- 不调用缓存的响应 
> \- 可以观察整个网络上传输的数据 
> \- 获得携带请求的Connection



## 应用

+ [OkHttp 拦截器的一些骚操作](https://juejin.im/post/5afc1706518825426f30f6ec)









