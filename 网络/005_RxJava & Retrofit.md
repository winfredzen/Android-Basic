# RxJava & Retrofit

参考：

+ [Android：Retrofit 与 RxJava联合使用大合集（含实例教程）！](https://blog.csdn.net/carson_ho/article/details/79125101)
+ [RxJava2开发小记：用CompositeDisposable来“安排”Retrofit网络请求](https://blog.csdn.net/ysy950803/article/details/84930656)



## 设置Retrofit支持RxJava

一般在创建`Retrofit`时，通过`Retrofit.Builder`的`addCallAdapterFactory`方法来设置

该方法的说明是：

> ```java
> Add a call adapter factory for supporting service method return types other than {@link* Call}.
> ```

> 即支持返回其它类型，而不是`Call`

![014](https://github.com/winfredzen/Android-Basic/blob/master/网络/images/014.png)





































