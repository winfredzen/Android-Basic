# implementation & api

参考：

+ [Android Studio中Gradle依赖详解](https://www.jianshu.com/p/9db6eaae2829)
+ [Implementation Vs Api in Android Gradle plugin 3.0](https://medium.com/mindorks/implementation-vs-api-in-gradle-3-0-494c817a6fa)



> **1、implementation：**
>
> 依赖包中依赖的library只能在依赖包内部使用，主工程无法访问依赖包依赖的library中的类和方法。使用场景：SDK开发中对第三方library有依赖，希望控制SDK的大小、不想因为和宿主工程引用的同一个依赖包版本不同导致编译冲突时特别适合。
>
> 因为当依赖包依赖的library有改动时，只会重新编译library和依赖包，不需要重新编译宿主，所以构建速度会快一些。
>
> 对于各个渠道还可以单独依赖属于渠道特有的包，通过渠道名+implementation指定，比如debugImplementation、releaseImplementation、testImplementation。
>
> **2、api(原compile)：**
>
> 会将依赖包中依赖的其它library一同编译和打包到apk中，宿主工程可以使用依赖包中依赖的其它library的类和方法
>
> 对于各个渠道还可以单独依赖属于渠道特有的包，通过渠道名+api/compile指定，比如debugApi、releaseApi、testApi
>
> **3、compileOnly(provided)：**
>
> 主要是为了方便程序编译通过的，不会打包到apk中，使用场景：android系统有这个API，但编译时需要引入才能构建通过，比如系统的APK依赖framework.jar、gson库等
>
> **4、runtimeOnly(原apk)：**
>
> 只是打包到apk中，不参与编译，不能在代码中直接调用依赖包的代码，否则会在编译时出错。一般很少使用
