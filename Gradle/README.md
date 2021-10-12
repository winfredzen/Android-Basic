# Gradle

在官网[配置编译版本](https://developer.android.com/studio/build)有介绍gradle的相关内容

其它教程：

+ [Gradle系列之初识Gradle](https://juejin.cn/post/6844903874629730318)



## Mac安装Gradle

通过Homebrew安装

```shell
brew install gradle
```

安装好后，通过`gradle -v`命令来验证

![015](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/015.png)



## 入门

内容来自《Android Gradle 权威指南》

如下创建一个`build.gralde`

```groovy
task hello {
	doLast {
		println 'Hello world!'
	}
}
```

在终端中，输入`gradle -q hello`

![016](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/016.png)

> **说明：**
>
> + 定义了一个任务（Task），任务名叫做`hello`，并给hello添加了一个动作（Action）
> + `doLast`表示在Task执行完毕之后，要回调`doLast`的这部分闭包的代码实现
> + `gradle -q hello`表示执行`build.gradle`脚本中名为hello的Task，`-q`参数用于控制gradle输出的日志级别





### Gradle Wrapper

就是对Gradle的一层包装，便于团队在开发过程中统一Gradle构建的版本



## Groovy



### 字符串

**在Groovy中，单引号和双引号所包含的内容都是字符串**

单引号没有运算能力，它里面的所有表达式都是常量字符串



## Android Gradle



### Android Gradle插件

分类

+ App插件 - 可以生成一个apk应用，id为`com.android.application`
+ Library插件 - Library库工程，可以生成AAR包，给其它App工程公用，id为`com.android.library`
+ Test插件 - 测试工程，id为`com.android.test`



























