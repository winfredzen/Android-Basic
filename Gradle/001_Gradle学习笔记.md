# Gradle学习笔记

首先先了解下Groovy，包括其闭包：

+ [Groovy 教程](https://www.w3cschool.cn/groovy/)
+ [读懂Android Studio中的Gradle文件](https://www.jianshu.com/p/dbc682513195)

了解下Gradle

+ [Gradle 教程](https://www.w3cschool.cn/gradle/)



**Gradle 与 Android Studio 的关系**

> Gradle 跟 Android Studio 其实没有关系，但是 Gradle 官方还是很看重 Android 开发的，Google 在推出 AS 的时候选中了 Gradle 作为构建工具，为了支持 Gradle 能在 AS 上使用，Google 做了个 AS 的插件叫 Android Gradle Plugin ，所以我们能在 AS 上使用 Gradle 完全是因为这个插件的原因。在项目的根目录有个 build.gradle 文件，里面有这么一句代码：
>
> ```java
> classpath 'com.android.tools.build:gradle:2.1.2'
> ```



新建的项目中一般有2个`build.gradle`，一个位于app目录下，一个位于项目目录下：

![001](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/001.png)



## app/build.gradle 配置文件

参考：

+ [Android 开发你需要了解的 Gradle 配置](https://zhuanlan.zhihu.com/p/21602684)

如下的一个文件：

```groovy
apply plugin: 'com.android.application'

android {
    compileSdkVersion 28


    defaultConfig {
        applicationId "com.example.myapplication"
        minSdkVersion 19
        targetSdkVersion 28
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])

    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
}
```

该文件可分为三个部分：

+ 最顶部的 apply plugin 声明
+ android {} 节点
+ dependencies {} 节点



### **apply plugin 声明**

`apply plugin: 'com.android.application'`表示代表该项目是一个 Android 项目，而且一个 Android 项目只有一句这个声明

`apply plugin: 'com.android.library'`表示如果你的项目有引用一些 module ，你可以理解成通过源码的方式引用一些 android library



### **dependencies 节点**

这是用来管理依赖的地方

`implementation fileTree(dir: 'libs', include: ['*.jar'])`表示的是将`libs`目录下所有的`.jar`后缀的文件都添加到项目的构建路径当中



`implementation 'androidx.appcompat:appcompat:1.1.0'`就是直接依赖远程项目名字 + 版本号，至于该项目是放在哪里的呢？一般是放在 jcenter 和 maven 仓库的，这个可以在项目根目录下的 `build.gradle` 指定远程仓库地址，甚至可以在本地搭建一个私有仓库，然后指定本地仓库地址



![002](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/002.png)

> 第三种就是类似原始的引用 android library 的方式，一般是你们公司内部的项目，或者改第三方库的源码，同时本地又没有搭建私有仓库，才会选择这种方式。这种方式目前很不推荐了，大部分情况第二种方式完全足够了，但是大家知道这也是一种依赖方式。



### **android 节点**

![003](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/003.jpg)

![004](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/004.jpg)



## Gradle Wrapper

参考：

+ [给 Android 初学者的 Gradle 知识普及](https://zhuanlan.zhihu.com/p/21473540)



> 现在默认新建一个项目，然后点击 AS 上的运行，默认就会直接帮你安装 Gradle ，我们不需要额外的安装 Gradle 了，但是其实这个 Gradle 不是真正的 Gradle ，他叫 Gradle Wrapper ，意为 Gradle 的包装，什么意思呢？假设我们本地有多个项目，一个是比较老的项目，还用着 Gradle 1.0 的版本，一个是比较新的项目用了 Gradle 2.0 的版本，但是你两个项目肯定都想要同时运行的，如果你只装了 Gradle 1.0 的话那肯定不行，所以为了解决这个问题，Google 推出了 Gradle Wrapper 的概念，就是他在你每个项目都配置了一个指定版本的 Gradle ，你可以理解为每个 Android 项目本地都有一个小型的 Gradle ，通过这个每个项目你可以支持用不同的 Gradle 版本来构建项目。



> **./gradlew -v** 版本号































