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





























