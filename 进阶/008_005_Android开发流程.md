# Android开发流程

**Android开发流程**

![102](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/102.png)

**开发三剑客**

1.Gradle - 构建工具

+ 依赖管理 - Gradle提供自动化的依赖
+ 编写/执行构建流程 - Gradle提供插件机制，定制多样化的构建流程
+ 平台无关性 - Gradle基于JVM，具有平台无关性

2.Android Gradle Plugin

+ 运行在Gradle平台上的插件
+ 包含了Android应用构建的各种逻辑

3.Android Studio

+ Android集成开发环境
+ 基于Gradle 和  Android Gradle Plugin



## 开发环境配置

1.JDK

> 若通过命令行执行 `java -version` 可以正常输出信息，则说明JDK安装成功

2.SDK（***Software Development Kit***）

> 安装完成后，建议各位把 `platforms-tools` 以及 `tools` 目录，配置到**系统环境变量**中，后续就可以在命令行中很方便到执行一些 adb 命令、或者启用某些工具。

3.Android Studio，是IDE（***Integrated Development Environment***），即集成开发环境，包含了工程创建、代码编写功能、分析功能、编译功能、调试功能、代码拉取与提交功能等





