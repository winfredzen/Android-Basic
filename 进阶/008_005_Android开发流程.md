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



**使用MultiDex突破方法数65536的限制**

MultiDex就是多Dex的意思，由于一个Dex放不下工程所有方法，所以要使用多Dex去放置

用法：

**小于21**

![109](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/109.png)

![110](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/110.png)

![111](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/111.png)

如果已集成其它Applicaiton，且不能被修改，可以使用如下的形式

![112](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/112.png)

**大于等于21**

![113](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/113.png)



采用MultiDex后安装包中的dex文件变多了

![114](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/114.png)



### 代码混淆

**为什么使用代码混淆？**

比如一些重要的方法，如果没有代码混淆，在编译后的apk中，可以直观的看到一些重要方法的名称

![115](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/115.png)



> 代码混淆，指的是在编译后，将代码中的类/方法/变量等信息进行重命名，把其改成毫无意义、较短的名字，同时也可以移除未被使用的类、方法、变量等。
>
> 在Android开发领域，之前主要是采用**Proguard**工具，进行代码混淆，不过在Android Gradle 插件 3.4.0 或者更高版本中，内部不再使用 ProGuard 执行编译时代码优化，而是与**R8编译器**协同工作。不过也不用太担心工具的更新带来太大的工作量，R8 支持所有现有 ProGuard 规则文件，因此在更新 Android Gradle 插件以使用 R8 时，并不需要更改现有的混淆规则。



好处

+ 安全性
+ 缩小apk的体积



开启混淆相对简单，通常是针对需要用于发布的 `release` 版本去开启，在app子工程中添加如下代码即可：

```groovy
android {
    buildTypes {
        release {
            // 启用 代码 的缩减、混淆、优化功能
            minifyEnabled true

            // 启用资源的 缩减功能，主要用于移除无用资源
            // 注意需要在 minifyEnabled 为 true 时，本选项才会发挥作用
            shrinkResources true

            // 配置混淆规则文件，用于自定义需要保留的代码
            proguardFiles getDefaultProguardFile(
                    'proguard-android-optimize.txt'),
                    'proguard-rules.pro'
        }
    }
    ...
}

```



混淆后，dex文件

![116](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/116.png)



一些类和类的方法不需要混淆，所以需要配置

如下，不混淆`MainActivity`的`importantMethodTwo`方法

```groovy
-keepclasseswithmembers class com.imooc.demo.MainActivity {
    private void importantMethodTwo();
}
```

![117](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/117.png)



> 例如，你有一个类，是需要在运行时反射去调用的，那么就不能混淆，这时候可以在自定义的混淆配置文件中添加一行：
>
> ```groovy
> -keep public class MyClass
> ```



**代码混淆之后，Crash堆栈难以定位问题，这时候怎么办？**

> 在APK编译完成后，可以在build目录下，拿到一个 `mapping.txt` 文件，其中列出了经过混淆处理的类、方法和字段的名称与原始名称的映射关系。
>
> 我们在后续在分析Crash堆栈时，就需要利用这个文件，配合`retrace` 工具去将堆栈信息还原成与源码对应的形式。
>
> 另外注意，为确保还原的堆栈轨迹更清晰，与源代码中的行号能一一对应，建议在自定义的混淆规则文件中添加下面内容：
>
> ```groovy
> -keepattributes LineNumberTable,SourceFile
> ```

![118](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/118.png)



`retrace` 工具位于sdk路径下的`/tools/proguard/bin/retrace.sh`

![119](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/119.png)



## 打包发布

> 如何对应用进行**打包**？也就是 如何将其生成APK给到用户使用？
>
> 在Android Studio中，虽然也有对应的功能（`Build - Build APK(s)`），但是它更多是用在开发调试阶段，对于打包发布阶段并不适用，例如，企业级项目一般会采用Jenkins等持续集成工具，自动构建并生成APK，这个时候更多是使用**命令行**去执行打包的过程。
>
> 在命令行中，执行assemble任务，就可以生成目标APK：
>
> ```groovy
> ./gradlew :app:assembleRelease  // mac/linux/unix
> gradlew :app:assembleRelease  // windows
> ```



### APK签名

APK需要被签名后，才能够安装到手机上。在开发阶段，通常使用的是`debug`版本的构建类型，Android插件会使用默认的签名文件对应用进行签名，所以各位发现这个阶段不自己去配置签名，也可以正常安装运行。

默认的 `debug.keystore` 可以在下面路径找到：

- `~/.android/`（在 Mac 和 Linux 上）
- `C:\Documents and Settings\user.android\`（在 Windows XP 上）
- `C:\Users\user.android\`（在 Windows Vista 以及 Windows 7、8 和 10 上）



生成签名文件可以使用 Android Studio 中 `Generate Signed Bundle/APK` 功能（在`Build`菜单中），跟随界面提示操作即可。

可能会有如下的提示

![120](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/120.png)

按提示操作

![121](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/121.png)



签名的使用，其实在平时我们更多需要关注的只是如何去把签名的信息**传递**给Android Gradle 插件，可以通过下面方式去传递：

```groovy
android {
    // ...
    signingConfigs {
        
        // 配置一个名为release的签名信息
        release {
            keyAlias 'test'
            keyPassword '123456'
            storeFile file('./my-key.jks')
            storePassword '123456'
		}
    }
    
    buildTypes {
        release {
            // 为特定的构建变体指定签名文件
            signingConfig signingConfigs.release
        }

}

```



## 定制不同的apk

构建定制，指的是通过**配置**，在**同一个**工程项目中，生成多个带有不同特性的APK。

例如，某些公司开发的一个APP，对外会发布两个版本：一个是功能有限的免费版本，另一个是包含全部可用功能的付费版本。这个时候，就可以通过构建定制去满足这种需求。

在Android开发中，进行APK的构建定制的主要手段，是配置**构建变体**（***Build Variant***）。

构建变体主要由 构建类型 和 产品变种 组成。

先讲讲这两个概念。

- 构建类型（***Build types***）：用来定义在构建和打包应用时使用的某些属性，如签名信息等。通常针对**开发生命周期的不同阶段**进行配置。debug、release就是最常见的构建类型，你可以根据实际情况，对构建类型进行进一步细分。在每一种构建类型中，每一个选项都可以自由定制，以混淆处理为例，debug通常不会开启，而release版本才会开启。
- 产品变种（***Product flavors***）：可以用于定义面向用户发布的**多个产品（即当前应用的不同版本）**，如本节开头提到的应用的**免费版**和**付费版**。在不同的产品变种中，我们可以配置不同的代码和资源等信息。



构建类型的配置形式如下所示：

```groovy
android {
   
    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }

        debug {
            applicationIdSuffix ".debug"
            debuggable true
        }

        preRelease {
            ...
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
     
    }
}

```

**默认情况下，Android Studio会创建debug和release两种构建类型**



产品变种的配置形式如下所示：

```groovy
android {
    ...
    defaultConfig {...}
    buildTypes {
        debug{...}
        release{...}
    }
    
    //指定产品变种的 纬度，这里只指定了一个。
    flavorDimensions "version"
    
    productFlavors {
        free {
            // Assigns this product flavor to the "version" flavor dimension.
            // If you are using only one dimension, this property is optional,
            // and the plugin automatically assigns all the module's flavors to
            // that dimension.
            // 为当前产品变种指定 version 这一纬度。
            // 注意每个
            dimension "version"
            applicationIdSuffix ".free"
            versionNameSuffix "-free"
        }
        
        paid {
            dimension "version"
            applicationIdSuffix ".paid"
            versionNameSuffix "-paid"
        }
    }
}

```



## 多渠道打包

> 国内Android应用分发市场众多，在发布 APK 时一般需要生成多个渠道包，上传到不同的应用市场。这些渠道包会包含不同的渠道信息，在 APP和后台交互或者数据上报时，会带上各自的渠道信息。这样，我们就能统计到每个分发市场的下载数、用户数等关键数据。
>
> 基于上述定义，我们多渠道打包的核心过程其实分成两步：
>
> - 向APK中，写入渠道号信息
> - 在运行期间，读取渠道号信息
>
> Android官方机制里面，其实是支持多渠道打包的。其基本原理是 `manifestPlaceholders` 与 产品变种 相结合。这是最“正统”的多渠道打包方式
>
> 不过其**问题**在于，每生成一个渠道包，都要重新执行一遍构建流程，**效率太低**，只适用于渠道较少的场景。因此业界衍生了各种多渠道打包的方案。
>
> 例如，其中一个方案是，在assets内部的一个预置一个文件用来存放渠道名信息。在构建出一个母包后，遍历所有渠道名称，替换预置的文件，然后重新签名。
>
> 再比如，在 `META-INF` 中添加一个使用渠道号命名的空文件，这种方式更为高效，不过在 APK Signature Scheme v2 下，已经行不通了。



1.`AndroidManifest.xml`中添加`meta-data`

![122](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/122.png)



2.app目录下的build.gradle，构建变体

![123](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/123.png)



构建，选择`assembleRelease`，双击

![124](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/124.png)



**验证是否写入**

1.使用aapt，工具的路径`/Users/wangzhen/Library/Android/sdk/build-tools/29.0.2`

2.`./aapt dump xmltree /Users/wangzhen/Documents/GitHub/Android-Basic/进阶/code/app_flow/app/build/outputs/apk/xiaomi/release/Demo_xiaomi_1.0.apk AndroidManifest.xml`

![125](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/125.png)



**APK重命名**



```groovy
    applicationVariants.all { variant ->
        variant.outputs.each { output ->
            if (variant.buildType.name.equals('release')) {
                def fileName =
                        "Demo_${variant.productFlavors[0].name}" +
                                "_${defaultConfig.versionName}.apk"
                output.outputFileName = fileName
            }
        }

    }
```



**读取渠道**

```kotlin
try {
    val appInfo = packageManager
        .getApplicationInfo(packageName,PackageManager.GET_META_DATA)
    val channelName = appInfo.metaData.getString("MTA_CHANNEL")
    Log.i("channel_test", "channel = $channelName")
} catch (e: Exception) {
    // ignore
}
```





























