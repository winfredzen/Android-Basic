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



## 工程建立

项目工程，目录结构一般如下：

```
.                                  // 根工程，相当于各个子工程的容器
├── gradle/wrapper                 // gradle wrapper 配置文件与Jar包
├── app                            // app子工程，实际的代码逻辑都来自于子工程
│   ├── build.gradle               // app子工程的gradle脚本
│   ├── gradle.properties          // gradle参数配置文件
│   ├── proguard-rules.pro         // 混淆规则配置文件
│   └── src                        // 源代码目录 
├── gradle.properties              // gradle参数配置文件（全局，所有子工程共享） 
├── gradlew                        // gradle wrapper 可执行脚本，适用于 unix/linux/mac等
├── gradlew.bat                    // gradle wrapper 可执行脚本，适用于 windows
├── build.gradle                   // 根gradle脚本  
└── settings.gradle                // 配置子工程列表等

```



## 开发调试



### 引用第三方库

- 本地依赖 - 直接将对应的jar或者aar拷贝到工程中，配置其参与编译
- 远程依赖 - 工程中只需要声明所需依赖的库名称和版本号，由Gradle在编译过程中，通过maven服务器去自动下载纳入编译过程

![103](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/103.png)



> 使用implementation所依赖的库 **不会传递** ，只会在当前module中生效。举个例子，某个子工程A，以implementation的方式依赖了子工程B。若另一个子工程C依赖了子工程A，那么，C中是无法直接调用到B中的接口的。也就是说，对于C而言，它只能调用到A公开的接口，其他内部实现细节（如B）是不公开的。使用implementation可以更好的实现不同模块之间的代码隔离。
>
> 此外，implementation带来的另一个好处是，可以加快编译速度，例如，若B中的代码修改了，由于采用了implementation方式去声明依赖，那么在编译期间只需要重新编译B和A模块就可以了，C是不需要重新参与编译的。
>
> api则没有上述好处。这里再强调一下，二者虽然都可以正常把第三方依赖或者是其他子工程引入并使用，但是，除了**明确要向外部暴露当前子工程所依赖的库的接口**的这种场景，建议各位 **平时优先采用implementation** 。



### 多Dex

![104](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/104.png)

![105](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/105.png)

从下图可以看出，多个class文件信息，在重新排布后会放在同一个dex文件中

![106](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/106.png)

![107](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/107.png)

![108](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/108.png)

> Dex文件格式是**专为Andorid上虚拟机设计的一种压缩格式**。可以简单的理解为：一个Dex文件，是多个class文件的集合。
>
> 也就是说，一个Dex文件中，可以包含多个类的多个方法，所有这些方法使用一个c++中的ushort类型来分配索引，在运行的时候，虚拟机会根据方法索引号，去引用对应的方法。而ushort类型的取值范围为 0-65535。
> 因此，单个 DEX 文件内引用的方法个数限制为**65536**。这些方法，包括开发人员自己编写的代码中的方法，以及工程中引用的所有第三方库的代码。随着业务的不断发展，代码量也会跟随急剧膨胀，终有一天会超越这个限制，出现 `method ID not in 65536` 的问题。
>
> 为了解决这个问题，Android官方给出了解决方案 - MultiDex。MultiDex就是多Dex的意思，由于一个Dex放不下工程所有方法，所以要使用多Dex去放置。
>
> 根据工程配置的minSdkVersion的不同，MultiDex有着不同的使用方法。具体使用方式，可以参考视频中的讲解。









