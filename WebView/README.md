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

但遗憾的是，在我的oppo，Android 11上，又灭有测试成功，请求头中并没有我刚才添加的字段

调试是在chrome中输入`chrome://inspect`，如果有连接设备调试，有网页，效果可能是这样的

![005](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/005.png)

点击inspect后，界面如下：

![006](https://github.com/winfredzen/Android-Basic/blob/master/WebView/images/006.png)

















