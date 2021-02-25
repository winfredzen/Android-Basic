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

如下封装异步的Get请求

```java
//回调接口
public interface INetCallBack {
    void onSuccess(String response);
    void onFailed(Throwable ex);
}

//网络工具类
public class OkHttpUtils {

    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    private OkHttpUtils() {

    }

    public static OkHttpUtils sInstance = new OkHttpUtils();

    public static OkHttpUtils getInstance() {
        return sInstance;
    }

    /**
     * http://www.imooc.com/api/okhttp/getmethod
     * @param url
     * @return
     */
    public void doGet(String url, INetCallBack callBack) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e);
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String respStr = null;
                try {
                    respStr = response.body().string();
                } catch (IOException e) {
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(e);
                        }
                    });
                }
                String finalRespStr = respStr;
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(finalRespStr);
                    }
                });

            }
        });
    }

}
```

实际调用的方式：

```java
OkHttpUtils.getInstance().doGet("http://www.imooc.com/api/okhttp/getmethod", new INetCallBack() {
    @Override
    public void onSuccess(String response) {
        mTvContent.setText(response);
    }
    @Override
    public void onFailed(Throwable ex) {
        Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
    }
});
```



### Post请求

通常需要对`OkHttpClient`实例进行某些设置，如：

+ `connectTimeout()`设置超时时间
+ `addInterceptor()`添加拦截器



#### Logging Interceptor

参考：

+ [Logging Interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md)

引入Logging Interceptor：

```java
implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
```

参数文档，如下设置logging：

```java
private OkHttpUtils() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            Log.d("WZ", message);
        }
    });
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    mOkHttpClient = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .build();
}
```

此时，网络请求的输出如下：

![007](https://github.com/winfredzen/Android-Basic/blob/master/%E7%BD%91%E7%BB%9C/images/007.png)



#### 普通Form形式

即`Content-Type: application/x-www-form-urlencoded`

如下：

```java
    public void doPost(String url, HashMap<String, String> params, INetCallBack callBack) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        if (params != null) {
            for (String param : params.keySet()) {
                formBodyBuilder.add(param, params.get(param));
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(formBodyBuilder.build())
                .build();


        executeRequest(callBack, request);
    }
```

![008](https://github.com/winfredzen/Android-Basic/blob/master/%E7%BD%91%E7%BB%9C/images/008.png)

#### 文件上传Form

即 `Content-Type: multipart/form-data; boundary=xxx`这种形式

```java
        MultipartBody.Builder multiPartBodyBuilder = new MultipartBody.Builder();
        multiPartBodyBuilder.setType(MultipartBody.FORM);

        if (params != null) {
            for (String param : params.keySet()) {
                multiPartBodyBuilder.addFormDataPart(param, params.get(param));
            }
        }
```

![009](https://github.com/winfredzen/Android-Basic/blob/master/%E7%BD%91%E7%BB%9C/images/009.png)



#### json形式

即`Content-Type: application/json; charset=utf-8`

```java
    public void doPostJson(String url, String jsonStr, INetCallBack callBack) {
        MediaType jsonMediaType = MediaType.get("application/json");
        RequestBody requestBody = RequestBody.create(jsonStr, jsonMediaType);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        executeRequest(callBack, request);
    }
```

![010](https://github.com/winfredzen/Android-Basic/blob/master/%E7%BD%91%E7%BB%9C/images/010.png)



### 自定义Header

如下的示例：

```java
    public void doGetWithHeader(String url, HashMap<String, String> headers, INetCallBack callBack) {
        Request.Builder requestBuilder= new Request.Builder();
        if (headers != null) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        Request request = requestBuilder
                .url(url)
                .build();

        executeRequest(callBack, request);
    }
```

![011](https://github.com/winfredzen/Android-Basic/blob/master/%E7%BD%91%E7%BB%9C/images/011.png)



**自定义Interceptor添加Header**

如下的`AuthInterceptor`：

```java
public class AuthInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originReqeust  = chain.request();
        Request newRequest = originReqeust.newBuilder()
                .addHeader("author", "wz")
                .build();

        return chain.proceed(newRequest);
    }

}
```

为`OkHttpClient`添加拦截器，因为要输出，所有注意拦截器的顺序：

```java
mOkHttpClient = new OkHttpClient.Builder()
        .addInterceptor(new AuthInterceptor())
        .addInterceptor(logging)
        .build();
```

查看logging：

![012](https://github.com/winfredzen/Android-Basic/blob/master/%E7%BD%91%E7%BB%9C/images/012.png)



## Retrofit

[retrofit](https://github.com/square/retrofit)是对okhttp的封装，官方文档地址：

+ [Retrofit](https://square.github.io/retrofit/)

引入Retrofit后，不需要再单独引入okhttp，否则可能会造成冲突

如何查看Retrofit依赖的okhttp的版本呢？

> 在External Libaries中找到Retrofit的jar包，右键->reveal in finder后，找到`.pom`文件，可查看依赖的okhttp的版本
>
> ![013](https://github.com/winfredzen/Android-Basic/blob/master/%E7%BD%91%E7%BB%9C/images/013.png)

如果要使用`logging-interceptor`，需要使用Retrofit依赖okhttp的那个版本：

> ```groovy
> implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")
> ```



**Retrofit的使用流程**

+ 创建接口，接口中创建方法

+ 方法及参数上添加注解

  ```java
  public interface GitHubService {
    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);
  }
  ```

+ 通过Retrofit对象得到接口代理对象

  ```java
  Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://api.github.com/")
      .build();
  
  GitHubService service = retrofit.create(GitHubService.class);
  ```

  > Retrofit类生成了`GitHubService`接口的实现

+ 通过代理对象执行

  ```java
  Call<List<Repo>> repos = service.listRepos("octocat");
  ```

  



### Get请求

在注解中指定url的相对路径

```java
@GET("users/list")
```

也可以在url中指定query的参数

```java
@GET("users/list?sort=desc")
```

也可以替换快来更新url，替换块由`{}`包裹，再使用`@Path`注解

```java
@GET("group/{id}/users")
Call<List<User>> groupList(@Path("id") int groupId);
```

添加Query参数：

```java
@GET("group/{id}/users")
Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);
```

对于复杂的参数，可使用`Map`

```java
@GET("group/{id}/users")
Call<List<User>> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);
```



### Post请求

**Form-encoded**

使用`@FormUrlEncoded`和`@Field()`注解

```java
@FormUrlEncoded
@POST("user/edit")
Call<User> updateUser(@Field("first_name") String first, @Field("last_name") String last);
```

**Multipart**

使用`@Multipart`和`@Part()` 

```java
@Multipart
@PUT("user/photo")
Call<User> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);
```

**JSON**

```java
@POST("postjson")
Call<Result> postJson(@Body RequestBody jsonBody);
```



### header

可以在方法上使用`@Headers`注解

```java
@Headers("Cache-Control: max-age=640000")
@GET("widget/list")
Call<List<Widget>> widgetList();
```

也可以使用`@Header`注解来动态的更新header

```java
@GET("user")
Call<User> getUser(@Header("Authorization") String authorization)
```

与query参数类似，也可以使用`@HeaderMap`

```java
@GET("user")
Call<User> getUser(@HeaderMap Map<String, String> headers)
```



如果要为每个request添加header，可使用[OkHttp interceptor](https://github.com/square/okhttp/wiki/Interceptors).



### CONVERTERS

默认情况下，Retrofit只能将HTTP body反序列化为OkHttp的`ResponseBody`类型，并且只能接受`@Body`的`RequestBody`类型

可以添加转换器以支持其他类型

```java
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build();

GitHubService service = retrofit.create(GitHubService.class);
```



### 例子

对Retrofit的简单的封装：

```java
public class RetrofitImpl {

    private static RetrofitImpl sInstance = new RetrofitImpl();

    public RetrofitImpl getInstance() {
        return sInstance;
    }

    private Retrofit mRetrofit;

    public static Retrofit getRetrofit() {
        return sInstance.mRetrofit;
    }

    private RetrofitImpl() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("WZ", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.imooc.com/api/okhttp/")
                .build();

    }

}
```





