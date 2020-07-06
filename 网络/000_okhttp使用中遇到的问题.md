# okhttp使用中遇到的问题

1.提示`java.net.UnknownServiceException: CLEARTEXT communication to xxxxx not permitted by network`

参考：

+ [java.net.UnknownServiceException: CLEARTEXT communication to wanandroid.com not permitted by network](https://blog.csdn.net/jing_80/article/details/89492429)

2.`java.lang.IllegalStateException: closed`

+ [解决okhttp的java.lang.IllegalStateException: closed错误](https://blog.csdn.net/weixin_34138521/article/details/86444021)

> 原因为OkHttp请求回调中response.body().string()只能有效调用一次
