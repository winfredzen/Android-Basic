# Android应用构建原理

## 安装包结构解析

> APK由以下主要部分组成：
>
> - `AndroidManifest.xml`：Android应用的清单文件，用于描述应用程序的名称、版本、所需权限、注册的四大组件；注意编译后的实际格式为AXML。
> - `lib/`：存放 so 文件，可能会有 armeabi、armeabi-v7a、arm64-v8a、x86、x86_64、mips 等子文件夹；不过为安装包大小考虑，大多数情况下只需要支持 armeabi 与 x86 的架构即可，如果非必需，甚至可以考虑拿掉x86的部分。so的更多知识，可以参考文章 [《谈谈Android的so》](http://allenfeng.com/2016/11/06/what-you-should-know-about-android-abi-and-so/)
> - `resources.arsc`：编译后的二进制资源文件
> - `res/`：存放编译后的资源文件，例如：drawable、layout等等
> - `assets`：应用程序的资源，应用程序可以使用AssetManager来检索该资源
> - `META-INF/` ：该文件夹一般存放于已经签名的APK中，它包含了APK中所有文件的签名摘要等信
> - `classes(n).dex`：classes文件是Java Class，被DEX编译后可供Dalvik/ART虚拟机所理解的文件格式



## 构建流程

参考Android官方提供的 [应用构建整体流程图](https://developer.android.com/studio/build)

![135](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/135.jpeg)

> 这个流程，可分为三部分来理解：输入、构建、输出。
>
> 首先看输入部分。
>
> 主要分两块，囊括了开发期间在工程添加的所有类型代码：
>
> - application类型的子工程：
>   - 代码：包括.java、.kt文件等
>   - 资源：各种布局对应的xml文件，引用的png、jpg图片等
>   - AIDL文件：用于跨进程通讯等
> - 其他依赖：
>   - AAR包
>   - JAR包
>   - library类型的子工程：同样包括代码、资源、AAR依赖等内容
>
> 构建这部分，亦可分为两块：
>
> - 编译：将输入，变成 Dex文件，以及编译好的二进制资源文件、so文件等。
> - 打包：将上述产物打包到一个 以 .apk 后缀命名 的 zip 压缩包中，然后进行签名，得到最终安装包
>
> 其中，代码和资源的编译，是我们需要了解的重点，同时也是应用最为广泛的部分。



![136](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/136.png)

**资源编译**

> 资源编译主要使用的是 [AAPT2](https://developer.android.google.cn/studio/command-line/aapt2) 工具，存放于 Android SDK的 `build-tools/` 目录下。负责对我们输入的资源进行解析、索引，并将资源编译成适合Android 平台的二进制格式。
>
> AAPT2内部会分成compile和link两个阶段。**compile**阶段，负责编译输入的资源文件，生成 后缀名 为 `.flat` 的中间二进制文件。**link**阶段则将上一步的所有中间文件，统一汇总在一起，为资源分配索引号，处理资源的相互依赖关系，然后生成两个产物，一个是 `resources.ap_` ， 另一个是 `R.java`。
>
> R.java 通常生成在如下目录中：
>
> ```
> build/intermediates/compile_and_runtime_not_namespaced_r_class_jar
> ```
>
> 这个文件好理解，开发者编写代码的时候接触的较多，可以根据 `R.layout.xxx` 的常量引用的方式，去调用对应的资源。
>
> 那么，资源的引用关系是如何对应的？
>
> 这里要说到`resources.ap_` 文件，它其实也是一个压缩包，包含如下三个部分：
>
> - res目录：包含编译后的xml、png等
> - AndroidManifest.xml：编译后的清单文件
> - resources.arsc ：保存所有资源的 索引ID-文件路径 之间的映射关系，通过这些资源索引可以在arsc文件中查取实际的资源路径或资源值;
>
> 感兴趣的同学，可以进一步研究 [resources.arsc的内部结构](http://androidxref.com/9.0.0_r3/xref/frameworks/base/libs/androidfw/include/androidfw/ResourceTypes.h)，资源文件名混淆与重复资源优化等更为进阶的技术对此知识有所运用。
>
> 



**代码编译**

> 主要会经历 **.java/.kt —> .class —> .dex**这一过程。
>
> JVM具有语言无关性，只要某种语言编译后生成的产物，符合 Java虚拟机规范 里面规定的字节码的格式，JVM都可以识别与运行。因此，不论是Kotlin还是Java语言，最后都会被编译成为 .class 字节码文件，
>
> 不过需要注意，Android手机上运行的不是JVM，而是 Android 虚拟机。根据系统版本的不同，有Dalvik和ART虚拟机两种实现。
>
> Android虚拟机不支持直接运行Java字节码。需要在编译期间，将Java字节码再次进行编译，生成Dex文件，才能在运行时被Android虚拟机加载运行。
>
> Dex 文件是很多 .class 文件被处理后的产物。多个Class文件内的信息，经过重新排布之后，被放置到了一个Dex文件内。可以使用 SDK build-tools/ 下的 d8命令行程序生成Dex，如下为d8的使用说明：
>
> ```java
> Usage: d8 [options] <input-files>
>  where <input-files> are any combination of dex, class, zip, jar, or apk files
>  and options are:
>   --debug                 # Compile with debugging information (default).
>   --release               # Compile without debugging information.
>   --output <file>         # Output result in <outfile>.
>                           # <file> must be an existing directory or a zip file.
>   --lib <file>            # Add <file> as a library resource.
>   --classpath <file>      # Add <file> as a classpath resource.
>   --min-api               # Minimum Android API level compatibility
>   --intermediate          # Compile an intermediate result intended for later
>                           # merging.
>   --file-per-class        # Produce a separate dex file per input class
>   --no-desugaring         # Force disable desugaring.
>   --main-dex-list <file>  # List of classes to place in the primary dex file.
>   --version               # Print the version of d8.
>   --help                  # Print this message.
> 
> ```







