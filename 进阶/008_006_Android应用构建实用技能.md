# Android应用构建实用技能

## 版本号的统一管理

在根目录下的`build.gradle`，可以采用 `ext` 或者是独立脚本的形式

```groovy
ext {
    MIN_SDK_VERSION = 21
    TARGET_SDK_VERSION = 29
    BUILD_TOOLS_VERSION = "29.0.1"

    APP_COMPAT = 'androidx.appcompat:appcompat:1.1.0'
}
```



## 用户密码管理

建议放到`local.properties`中，因为`local.properties`是可以加到`.gitignore`的

如：

![126](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/126.png)

然后在app模块下的`build.gradle`，通过`Properties`获取

```groovy
Properties properties = new Properties()
properties.load(project.rootProject.file("local.properties").newDataInputStream())
//获取
def username = properties.get("USER_NAME")
def password = properties.get("PASSWORD")
print("Test user name = " + username)
print("Test password = " + password)
```





## 依赖库的管理

> 编译时进行依赖检查，主要可以检查是否引用了某些团队禁用的库、或者是SNAPSHOT版本AAR等
>
> 建议各位可以建立一个意识：单靠口头约束力，是很难把某些规则严格落地的，如果这个过程中，可以加入**自动化**的检测手段，就尽可能及早加上，让机器帮助我们去进行规则的校验。**开发-编译-运行**，编译是运行前的最后一道关卡，许许多多的校验（不仅局限于依赖检查）都可以在编译期进行，提前将问题暴露出来。













