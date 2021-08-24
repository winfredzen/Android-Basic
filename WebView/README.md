# WebView

学习之前可先安装node，再安装[http-server](https://www.npmjs.com/package/http-server)

```js
npm install --global http-server
```

切换到对应的目录下，然后使用`http-server`命令启动接口

![001](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/001.png)



## webview加载⽹⻚的四种⽅式

**1.`loadUrl(String url)`**

直接加载⼀个⽹⻚进来，这个⽹ ⻚可以是⽹络上的地址，也可以 是⼿机本地的html⻚⾯，也可是项⽬资源⽂件下的html⻚⾯

如加载一个网络上的地址，注意添加网络权限和网络安全配置文件：

```java
mWebView.loadUrl("http://www.baidu.com");
mWebView.setWebViewClient(new WebViewClient());
```



加载手机SD卡中的网页，注意添加读取SD卡权限

尝试了一下，一致加载不成功

```java
// 设置允许访问文件数据
mWebView.getSettings().setAllowFileAccess(true);
mWebView.getSettings().setAllowContentAccess(true);
String filePath = "file://" + Environment.getExternalStorageDirectory().getPath() + "/1/index.html";
mWebView.loadUrl(filePath);
mWebView.setWebViewClient(new WebViewClient());
```

效果如下，也不知道为什么

![002](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/002.png)



加载资源目录下的网页，如下的目录结构

![003](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/003.png)

使用如下的方法加载网页

```java
mWebView.loadUrl("file:///android_asset/index.html");
```

效果如下：

![004](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/004.png)



**2.`loadUrl(String url, Map<String, String> additionalHttpHeaders)`**

可在请求头中添加字段

如下的例子，启用了调试，并在请求头中添加了字段：

```java
        //启用调试
        mWebView.setWebContentsDebuggingEnabled(true);

        mWebView.loadUrl("http://192.168.2.104:8080");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Map<String, String> header = new HashMap<>();
                header.put("android-webview-demo", "test");
                mWebView.loadUrl("http://192.168.2.104:8080", header);
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
```

但遗憾的是，在我的oppo，Android 11上，又没有测试成功，请求头中并没有我刚才添加的字段

调试是在chrome中输入`chrome://inspect`，如果有连接设备调试，有网页，效果可能是这样的

![005](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/005.png)

点击inspect后，界面如下：

![006](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/006.png)



**3.`loadData(String data, String mimeType, String encoding)`**

如下的例子：

```java
mWebView.loadData("<h1>Hello loadData</h1>", "text/html", "utf-8");
```

![007](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/007.png)



**4.`loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String failUrl)`**

+ `baseUrl` - 可以指定html中资源的相对跟路径
+ `failUrl` - 表示从webview跳转到其它网页，回退后显示的页面 ？

如下的例子，加载`慕课网`的logo图片

```java
        mWebView.loadDataWithBaseURL("http://www.imooc.com/",
                "<img src=\"static/img/index/logo2020.png\" />",
                "text/html",
                "utf-8",
                ""
        );
```

![008](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/008.png)



`failUrl`在API 30上也没**测试成功**

```java
        mWebView.loadDataWithBaseURL("http://www.imooc.com/",
                "<img src=\"static/img/index/logo2020.png\" /><a href=\"http://www.baidu.com\">跳转到Baidu</a>",
                "text/html",
                "utf-8",
                "http://www.sogou.com"
        );
```



## 网页前进后退

相关方法：

+ `canGoBack ()`
+ `canGoBack ()`
+ `canGoBackOrForward (int steps)`
+ `goBack ()`
+ `goBackOrForward (int steps)`
+ `goForward ()`
+ `clearHistory ()`



## webView状态管理

1.`onPause ()`

> Does a best-effort attempt to pause any processing that can be paused safely, such as animations and geolocation. Note that this call does not pause JavaScript. To pause JavaScript globally, use `pauseTimers()`. To resume WebView, call `onResume()`.
>
> 尽最大努力尝试暂停任何可以安全暂停的处理，例如动画和地理位置。请注意，此调用不会暂停JavaScript。要全局暂停JavaScript，请使用`pauseTimers()`。要恢复WebView，请调用`onResume()`。



2.`onResume ()`

> Resumes a WebView after a previous call to `onPause()`.



3.`pauseTimers ()`

> Pauses all layout, parsing, and JavaScript timers for all WebViews. This is a global requests, not restricted to just this WebView. This can be useful if the application has been paused.
>
> 暂停所有WebView的所有布局、解析和JavaScript计时器。这是一个全局请求，不限于此WebView。如果应用程序已暂停，这可能很有用。



4.`resumeTimers ()`

> Resumes all layout, parsing, and JavaScript timers for all WebViews. This will resume dispatching all timers.
>
> 恢复所有WebView的所有布局、解析和JavaScript计时器。这将恢复调度所有计时器。



5.`destroy ()`



如果网页中有音频在播放，当网页后退后，甚至Activity后退后，音频还在播放，所以要处理WebView的各种状态

```java
    @Override
    protected void onPause() {
        super.onPause();

        mWebView.onPause();
        //mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mWebView.onResume();
        //mWebView.resumeTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mWebView.destroy();
    }
```





## WebView常用类

+ `WebSettings` - 对webview进行配置和管理
+ `WebViewClient` - 处理webview加载时的各种回调通知
+ `WebChromeClient` - 辅助webview去处理JavaScript对话框、进度、标题等



### WebSettings

通过webview的`getSettings()`方法获取`WebSettings`



1.启用执行JavaScript

默认是不启用`JavaScript`，通过`setJavaScriptEnabled(boolean flag)`设置

```java
WebSettings webSettings = mWebView.getSettings();
webSettings.setJavaScriptEnabled(true);
```



2.控制网页的缩放

```java
	setSupportZoom(boolean support)
```

> Sets whether the WebView should support zooming using its on-screen zoom controls and gestures. The particular zoom mechanisms that should be used can be set with `setBuiltInZoomControls(boolean)`. This setting does not affect zooming performed using the `WebView#zoomIn()` and `WebView#zoomOut()` methods. The default is `true`.
>
> 是否支持缩放

```java
setBuiltInZoomControls(boolean)
```

> Sets whether the WebView should use its built-in zoom mechanisms. The built-in zoom mechanisms comprise on-screen zoom controls, which are displayed over the WebView's content, and the use of a pinch gesture to control zooming. Whether or not these on-screen controls are displayed can be set with `setDisplayZoomControls(boolean)`. The default is `false`.
>
> The built-in mechanisms are the only currently supported zoom mechanisms, so it is recommended that this setting is always enabled. However, on-screen zoom controls are deprecated in Android (see `ZoomButtonsController`) so it's recommended to disable `setDisplayZoomControls(boolean)`.
>
> 设置内置的缩放控件

```java
setDisplayZoomControls (boolean enabled)
```

> Sets whether the WebView should display on-screen zoom controls when using the built-in zoom mechanisms. See `setBuiltInZoomControls(boolean)`. The default is `true`. However, on-screen zoom controls are deprecated in Android (see `ZoomButtonsController`) so it's recommended to set this to `false`.
>
> 是否显示原生的缩放控件



```java
webSettings.setSupportZoom(true);
webSettings.setBuiltInZoomControls(true);
webSettings.setDisplayZoomControls(true);
```



3.控制webview的缓存策略

通过`setCacheMode (int mode)`方法设置缓存策略



+ `LOAD_CACHE_ONLY` - 不使用网络，只使用本地缓存，没有缓存则不会加载
+ `LOAD_CACHE_ELSE_NETWORK`  - 有缓存则使用缓存，那怕缓存过期，没有缓存则加载网络
+  `LOAD_DEFAULT` - Default cache usage mode. If the navigation type doesn't impose any specific behavior, use cached resources when they are available and not expired, otherwise load resources from the network. 默认缓存使用模式。如果导航类型未强制任何特定行为，在缓存资源可用且未过期时使用缓存资源，否则从网络加载资源。(即Http Server上设置的缓存策略)
+ `LOAD_NO_CACHE` - 永远不使用缓存，只从网络获取数据



`WebView`有个清除缓存的方法`clearCache (boolean includeDiskFiles)`













