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



## WebViewClient

`WebViewClient`处理网页加载时的各种回调通知

1.`WebResourceResponse shouldInterceptRequest(WebView view, String url)`

进行资源请求的时候回调，返回为空的时候，webview会自己去处理请求

2.`void onPageStarted(WebView view, String url, Bitmap favicon)`

网页已经开始开始加载时的回调

3.`onLoadResource(WebView view, String url)`

加载网页资源之前回调

4.`void onPageFinished(WebView view, String url)`

网页加载完成时回调



如下的例子：

```java
        mWebView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.d(TAG, "shouldInterceptRequest url = " + request.getUrl().toString());
                return super.shouldInterceptRequest(view, request);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.d(TAG, "shouldInterceptRequest request url = " + url);
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted url = " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.d(TAG, "onLoadResource url = " + url);
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished url = " + url);
                super.onPageFinished(view, url);
            }
        });

```

![009](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/009.png)



5.`boolean shouldOverrideUrlLoading(WebView view, String url)`

webview将要加载新的url时进行回调，例如点击`a`标签打开新的页面时



6.`void onReceivedError(WebView view, int errorCode, String description, String failingUrl)`

在网页发生错误的时候回调



## WebChromeClient

监听网页加载的进度、监听对话框的弹出，获取网页的标题、图标等

1.`void onProgressChanged(WebView view, int newProgress)`

获取网页加载进度

2.`void onReceivedTitle(WebView view, String title)`

获取网页的标题



```java
2021-09-23 14:48:34.796 19077-19077/com.example.webviewdemo D/WebViewActivity: WebChromeClient onProgressChanged newProgress = 23
2021-09-23 14:48:34.799 19077-19077/com.example.webviewdemo D/WebViewActivity: WebChromeClient onReceivedTitle title = 百度一下
```



3.`boolean onJsAlert(WebView view, String url, String message, JsResult result)`

在网页将要打开一个alert警告对话框的时候回调

4.`onJsConfirm(WebView view, String url, String message, JsResult result)`

在网页将要打开一个confirm对话框的时候回调

5.`onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result)`

在网页将要打开一个prompt对话框的时候回调



## WebView与js交互

### Android端调用JS代码

1.WebView通过`loadUrl("javascript:方法名(参数)")`调用

如下的html：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1>Test  001</h1>
    <a href="#" onclick="showAlert()">Show Alert</a>
    <a href="#" onclick="showConfirm()">Show Confirm</a>
    <a href="#" onclick="showPrompt()">Show Prompt</a>


    <script>
        function showAlert() {
            alert('只是alert对话框');
        }

        function showConfirm() {
            var result = confirm('这是confirm对话框');
            if (result) {
                alert('点击了确定按钮');
            } else {
                alert('点击了取消按钮');
            }
        }

        function showPrompt() {
            var res = prompt('这是prompt对话框', '则是默认的提示内容');
            alert(res);
        }
    </script>

</body>
</html>
```

如调用js中的`showAlert()`方法

```java
    public void onCallShowAlert(View view) {
        mWebView.loadUrl("javascript:showAlert()");
    }
```

效果如下：

![010](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/010.png)



**这种方式有个问题，无法获取js方法的返回值**

如果调用的js方法是有返回值的(String类型)，则返回值会替换当前的网页，如下：

```js
        function sum(x, y) {
            var result = x + y;
            return x + '+' + y + ' = ' + result;
        }
```

```java
    public void onCallShowAlert(View view) {
        //mWebView.loadUrl("javascript:showAlert()");
        mWebView.loadUrl("javascript:sum(2, 3)");
    }
```

![011](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/011.png)



那怎么办呢？可以通过`WebChromeClient`的`onJsAlert`回调中的`message`，拿到返回值

如：

```java
mWebView.loadUrl("javascript:alert(sum(2, 3))");
```

输出结果为：

```java
setWebChromeClient onJsAlert message = 2+3 = 5 ,result = android.webkit.JsPromptResult@d1baa9c
```



2.通过`evaluateJavascript`来调用

如：

```java
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript("javascript:sum(2, 3)", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.d(TAG, "onReceiveValue value = " + value);
                }
            });
        }
```

```java
D/WebViewActivity: onReceiveValue value = "2+3 = 5"
```



### JS调用Android代码

1.拦截JavaScript请求的回调方法

通过`shouldOverrideUrlLoading`方法

如下的一个链接

```html
<a href="android://print?msg=通过js调用android方法">通过js调用android方法</a>
```

通过`public boolean shouldOverrideUrlLoading(WebView view, String url)` 拦截，然后调用android的`print`方法

```java
@Override
public boolean shouldOverrideUrlLoading(WebView view, String url) {
    //检测url
    Uri uri = Uri.parse(url);
    if ("android".equals(uri.getScheme())) {
        String functionName = uri.getAuthority();
        if ("print".equals(functionName)) {
            String msg = uri.getQueryParameter("msg");
            print(msg);
        }
    }
    return super.shouldOverrideUrlLoading(view, url);
}
```



2.android代码的返回值如何传递给js？

可以通过`mWebView.loadUrl`

如在上面的print方法中，将返回值传递给js中的alert

```java
    private void print(String msg) {
        Log.d(TAG, "print msg = " + msg);
        String result = "需要返回的值";
        mWebView.loadUrl("javascript:showAlert('" + result + "')");
    }
```

![012](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/012.png)





3.对象映射

```java
void addJavascriptInterface(Object obj, String interfaceName)
```

如下的例子：

1.首先定义Object，`DemoJsObject`，方式使用`@JavascriptInterface`注解

```java
public class DemoJsObject {
    @JavascriptInterface
    public String print(String msg) {
        Log.d("DemoJsObject", "msg = " + msg);
        return "这是Android的返回值";
    }
}
```

2.调用`addJavascriptInterface`

```java
mWebView.addJavascriptInterface(new DemoJsObject(), "android");
```

3.在js中调用Android中的方法，传入参数，将返回值弹出

```
<a href="#" onclick="onAndroidFunction()">通过对象映射，调用android方法</a>

        function onAndroidFunction() {
            var result = android.print('onAndroidFunction');
            alert(result);
        }
```

调用后，在控制台输出了传入的参数

```java
2021-09-23 17:03:53.592 3220-3578/com.example.webviewdemo D/DemoJsObject: msg = onAndroidFunction
```

返回值alert弹出了

![013](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/013.png)















