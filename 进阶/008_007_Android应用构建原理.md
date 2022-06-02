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
