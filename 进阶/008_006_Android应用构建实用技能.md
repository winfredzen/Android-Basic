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



## 脚本的拆分

将上面的代码移动到`read_local_prop.gradle`后

![127](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/127.png)

然后在app模块下的`build.gradle`，通过`apply from: project.file('read_local_prop.gradle')`引入





## 依赖库的管理

**查看工程所有的依赖关系**

`./gradlew app:dependencies`

上面列出的信息太多了，可以对某个构建变体来过滤

`./gradlew app:dependencies --configuration xiaomiDebugCompileClasspath`



```groovy
wangzhen@wangzhendeMacBook-Pro-2 app_flow % ./gradlew app:dependencies --configuration xiaomiDebugCompileClasspath

> Configure project :app
test user name ----- imooc
test user pwd ----- 123456
Test user name = imoocTest password = 123456
> Task :app:dependencies

------------------------------------------------------------
Project :app
------------------------------------------------------------

xiaomiDebugCompileClasspath - Compile classpath for compilation 'xiaomiDebug' (target  (androidJvm)).
+--- project :biz_reading
+--- org.jetbrains.kotlin:kotlin-stdlib:1.3.72
|    +--- org.jetbrains.kotlin:kotlin-stdlib-common:1.3.72
|    \--- org.jetbrains:annotations:13.0
+--- androidx.core:core-ktx:1.2.0 -> 1.3.2
|    +--- org.jetbrains.kotlin:kotlin-stdlib:1.3.71 -> 1.3.72 (*)
|    +--- androidx.annotation:annotation:1.1.0
|    \--- androidx.core:core:1.3.2
|         +--- androidx.annotation:annotation:1.1.0
|         +--- androidx.lifecycle:lifecycle-runtime:2.0.0 -> 2.1.0
|         |    +--- androidx.lifecycle:lifecycle-common:2.1.0
|         |    |    \--- androidx.annotation:annotation:1.1.0
|         |    +--- androidx.arch.core:core-common:2.1.0
|         |    |    \--- androidx.annotation:annotation:1.1.0
|         |    \--- androidx.annotation:annotation:1.1.0
|         \--- androidx.versionedparcelable:versionedparcelable:1.1.0
|              +--- androidx.annotation:annotation:1.1.0
|              \--- androidx.collection:collection:1.0.0 -> 1.1.0
|                   \--- androidx.annotation:annotation:1.1.0
+--- androidx.appcompat:appcompat:1.1.0
|    +--- androidx.annotation:annotation:1.1.0
|    +--- androidx.core:core:1.1.0 -> 1.3.2 (*)
|    +--- androidx.cursoradapter:cursoradapter:1.0.0
|    |    \--- androidx.annotation:annotation:1.0.0 -> 1.1.0
|    +--- androidx.fragment:fragment:1.1.0
|    |    +--- androidx.annotation:annotation:1.1.0
|    |    +--- androidx.core:core:1.1.0 -> 1.3.2 (*)
|    |    +--- androidx.collection:collection:1.1.0 (*)
|    |    +--- androidx.viewpager:viewpager:1.0.0
|    |    |    +--- androidx.annotation:annotation:1.0.0 -> 1.1.0
|    |    |    +--- androidx.core:core:1.0.0 -> 1.3.2 (*)
|    |    |    \--- androidx.customview:customview:1.0.0
|    |    |         +--- androidx.annotation:annotation:1.0.0 -> 1.1.0
|    |    |         \--- androidx.core:core:1.0.0 -> 1.3.2 (*)
|    |    +--- androidx.loader:loader:1.0.0
|    |    |    +--- androidx.annotation:annotation:1.0.0 -> 1.1.0
|    |    |    +--- androidx.core:core:1.0.0 -> 1.3.2 (*)
|    |    |    +--- androidx.lifecycle:lifecycle-livedata:2.0.0
|    |    |    |    +--- androidx.arch.core:core-runtime:2.0.0
|    |    |    |    |    +--- androidx.annotation:annotation:1.0.0 -> 1.1.0
|    |    |    |    |    \--- androidx.arch.core:core-common:2.0.0 -> 2.1.0 (*)
|    |    |    |    +--- androidx.lifecycle:lifecycle-livedata-core:2.0.0
|    |    |    |    |    +--- androidx.lifecycle:lifecycle-common:2.0.0 -> 2.1.0 (*)
|    |    |    |    |    +--- androidx.arch.core:core-common:2.0.0 -> 2.1.0 (*)
|    |    |    |    |    \--- androidx.arch.core:core-runtime:2.0.0 (*)
|    |    |    |    \--- androidx.arch.core:core-common:2.0.0 -> 2.1.0 (*)
|    |    |    \--- androidx.lifecycle:lifecycle-viewmodel:2.0.0 -> 2.1.0
|    |    |         \--- androidx.annotation:annotation:1.1.0
|    |    +--- androidx.activity:activity:1.0.0
|    |    |    +--- androidx.annotation:annotation:1.1.0
|    |    |    +--- androidx.core:core:1.1.0 -> 1.3.2 (*)
|    |    |    +--- androidx.lifecycle:lifecycle-runtime:2.1.0 (*)
|    |    |    +--- androidx.lifecycle:lifecycle-viewmodel:2.1.0 (*)
|    |    |    \--- androidx.savedstate:savedstate:1.0.0
|    |    |         +--- androidx.annotation:annotation:1.1.0
|    |    |         +--- androidx.arch.core:core-common:2.0.1 -> 2.1.0 (*)
|    |    |         \--- androidx.lifecycle:lifecycle-common:2.0.0 -> 2.1.0 (*)
|    |    \--- androidx.lifecycle:lifecycle-viewmodel:2.0.0 -> 2.1.0 (*)
|    +--- androidx.appcompat:appcompat-resources:1.1.0
|    |    +--- androidx.annotation:annotation:1.1.0
|    |    +--- androidx.core:core:1.0.1 -> 1.3.2 (*)
|    |    +--- androidx.vectordrawable:vectordrawable:1.1.0
|    |    |    +--- androidx.annotation:annotation:1.1.0
|    |    |    +--- androidx.core:core:1.1.0 -> 1.3.2 (*)
|    |    |    \--- androidx.collection:collection:1.1.0 (*)
|    |    +--- androidx.vectordrawable:vectordrawable-animated:1.1.0
|    |    |    +--- androidx.vectordrawable:vectordrawable:1.1.0 (*)
|    |    |    +--- androidx.interpolator:interpolator:1.0.0
|    |    |    |    \--- androidx.annotation:annotation:1.0.0 -> 1.1.0
|    |    |    \--- androidx.collection:collection:1.1.0 (*)
|    |    \--- androidx.collection:collection:1.0.0 -> 1.1.0 (*)
|    +--- androidx.drawerlayout:drawerlayout:1.0.0
|    |    +--- androidx.annotation:annotation:1.0.0 -> 1.1.0
|    |    +--- androidx.core:core:1.0.0 -> 1.3.2 (*)
|    |    \--- androidx.customview:customview:1.0.0 (*)
|    \--- androidx.collection:collection:1.0.0 -> 1.1.0 (*)
+--- com.google.android.material:material:1.1.0 -> 1.2.1
|    +--- androidx.annotation:annotation:1.0.1 -> 1.1.0
|    +--- androidx.appcompat:appcompat:1.1.0 (*)
|    +--- androidx.cardview:cardview:1.0.0
|    |    \--- androidx.annotation:annotation:1.0.0 -> 1.1.0
|    +--- androidx.coordinatorlayout:coordinatorlayout:1.1.0
|    |    +--- androidx.annotation:annotation:1.1.0
|    |    +--- androidx.core:core:1.1.0 -> 1.3.2 (*)
|    |    +--- androidx.customview:customview:1.0.0 (*)
|    |    \--- androidx.collection:collection:1.0.0 -> 1.1.0 (*)
|    +--- androidx.core:core:1.2.0 -> 1.3.2 (*)
|    +--- androidx.annotation:annotation-experimental:1.0.0
|    +--- androidx.fragment:fragment:1.0.0 -> 1.1.0 (*)
|    +--- androidx.lifecycle:lifecycle-runtime:2.0.0 -> 2.1.0 (*)
|    +--- androidx.recyclerview:recyclerview:1.0.0 -> 1.1.0
|    |    +--- androidx.annotation:annotation:1.1.0
|    |    +--- androidx.core:core:1.1.0 -> 1.3.2 (*)
|    |    +--- androidx.customview:customview:1.0.0 (*)
|    |    \--- androidx.collection:collection:1.0.0 -> 1.1.0 (*)
|    +--- androidx.transition:transition:1.2.0
|    |    +--- androidx.annotation:annotation:1.1.0
|    |    +--- androidx.core:core:1.0.1 -> 1.3.2 (*)
|    |    \--- androidx.collection:collection:1.0.0 -> 1.1.0 (*)
|    +--- androidx.vectordrawable:vectordrawable:1.1.0 (*)
|    \--- androidx.viewpager2:viewpager2:1.0.0
|         +--- androidx.annotation:annotation:1.1.0
|         +--- androidx.fragment:fragment:1.1.0 (*)
|         +--- androidx.recyclerview:recyclerview:1.1.0 (*)
|         +--- androidx.core:core:1.1.0 -> 1.3.2 (*)
|         \--- androidx.collection:collection:1.1.0 (*)
+--- androidx.constraintlayout:constraintlayout:1.1.3
|    \--- androidx.constraintlayout:constraintlayout-solver:1.1.3
+--- org.jetbrains.kotlin:kotlin-stdlib:{strictly 1.3.72} -> 1.3.72 (c)
+--- androidx.core:core-ktx:{strictly 1.3.2} -> 1.3.2 (c)
+--- androidx.appcompat:appcompat:{strictly 1.1.0} -> 1.1.0 (c)
+--- com.google.android.material:material:{strictly 1.2.1} -> 1.2.1 (c)
+--- androidx.constraintlayout:constraintlayout:{strictly 1.1.3} -> 1.1.3 (c)
+--- org.jetbrains.kotlin:kotlin-stdlib-common:{strictly 1.3.72} -> 1.3.72 (c)
+--- org.jetbrains:annotations:{strictly 13.0} -> 13.0 (c)
+--- androidx.annotation:annotation:{strictly 1.1.0} -> 1.1.0 (c)
+--- androidx.core:core:{strictly 1.3.2} -> 1.3.2 (c)
+--- androidx.cursoradapter:cursoradapter:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.fragment:fragment:{strictly 1.1.0} -> 1.1.0 (c)
+--- androidx.appcompat:appcompat-resources:{strictly 1.1.0} -> 1.1.0 (c)
+--- androidx.drawerlayout:drawerlayout:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.collection:collection:{strictly 1.1.0} -> 1.1.0 (c)
+--- androidx.cardview:cardview:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.coordinatorlayout:coordinatorlayout:{strictly 1.1.0} -> 1.1.0 (c)
+--- androidx.annotation:annotation-experimental:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.lifecycle:lifecycle-runtime:{strictly 2.1.0} -> 2.1.0 (c)
+--- androidx.recyclerview:recyclerview:{strictly 1.1.0} -> 1.1.0 (c)
+--- androidx.transition:transition:{strictly 1.2.0} -> 1.2.0 (c)
+--- androidx.vectordrawable:vectordrawable:{strictly 1.1.0} -> 1.1.0 (c)
+--- androidx.viewpager2:viewpager2:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.constraintlayout:constraintlayout-solver:{strictly 1.1.3} -> 1.1.3 (c)
+--- androidx.versionedparcelable:versionedparcelable:{strictly 1.1.0} -> 1.1.0 (c)
+--- androidx.viewpager:viewpager:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.loader:loader:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.activity:activity:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.lifecycle:lifecycle-viewmodel:{strictly 2.1.0} -> 2.1.0 (c)
+--- androidx.vectordrawable:vectordrawable-animated:{strictly 1.1.0} -> 1.1.0 (c)
+--- androidx.customview:customview:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.lifecycle:lifecycle-common:{strictly 2.1.0} -> 2.1.0 (c)
+--- androidx.arch.core:core-common:{strictly 2.1.0} -> 2.1.0 (c)
+--- androidx.lifecycle:lifecycle-livedata:{strictly 2.0.0} -> 2.0.0 (c)
+--- androidx.savedstate:savedstate:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.interpolator:interpolator:{strictly 1.0.0} -> 1.0.0 (c)
+--- androidx.arch.core:core-runtime:{strictly 2.0.0} -> 2.0.0 (c)
\--- androidx.lifecycle:lifecycle-livedata-core:{strictly 2.0.0} -> 2.0.0 (c)

(c) - dependency constraint
(*) - dependencies omitted (listed previously)

A web-based, searchable dependency report is available by adding the --scan option.

BUILD SUCCESSFUL in 996ms
1 actionable task: 1 executed

```



**编译期监测依赖的安全性**

> 编译时进行依赖检查，主要可以检查是否引用了某些团队禁用的库、或者是SNAPSHOT版本AAR等
>
> 建议各位可以建立一个意识：单靠口头约束力，是很难把某些规则严格落地的，如果这个过程中，可以加入**自动化**的检测手段，就尽可能及早加上，让机器帮助我们去进行规则的校验。**开发-编译-运行**，编译是运行前的最后一道关卡，许许多多的校验（不仅局限于依赖检查）都可以在编译期进行，提前将问题暴露出来。



如下的例子：

```groovy
// 编译期间检测依赖库的合法性
configurations.all {
    resolutionStrategy.eachDependency { details ->
        String dependency = details.requested.toString()
        if (dependency.contains("com.google.android.material")) {
            throw new RuntimeException("不允许使用 $dependency")
        }
    }
}
```



## BuildConfig

`BuildConfig`是安卓编译期间gradle自动生成的一个类，可以定义与当前编译相关的变量

`BuildConfig`可以理解成由编译期去控制运行时某些功能的手段

![128](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/128.png)

![129](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/129.png)

![130](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/130.png)



## 构建速度优化

> 构建速度优化关乎一个团队的开发效率，是所有开发者都应该关注的一个话题，我们主要从分析、定位、解决三个层面展开讲述。
>
> **哪些因素会导致构建速度变慢？**
>
> 首先我们从理论层面，去看看都会有哪些阶段会导致构建耗时过长。
>
> 结合Gradle生命周期的角度，自上而下去分析：
>
> - 初始化阶段：此阶段主要执行settings.gradle 脚本，脚本中若包含过多的子工程或者其他耗时操作，会拖慢这个阶段的执行。
> - 配置阶段：解析执行各个子工程的build.gradle脚本，若脚本中、或者使用的插件的apply方法中等地方存在耗时操作，则可能会导致这个阶段耗时过长，此外，工程依赖的下载和解析，也会占用一定时长。
> - 执行阶段：主要是执行前一阶段配置好的所有的Task。这个流程其实就涉及到了APK的构建流程。过程中会分为资源编译、javac/kotlinc、proguard、dex、打包签名等步骤。这里每一个步骤都有可能会导致耗时慢，例如代码量庞大的时候，javac/kotlinc等阶段的耗时自然也就随之上升。
>
> 了解上述几个阶段后，就可以了解有哪些因素会影响构建速度了，例如网络环境差，会影响配置阶段拉取依赖的过长、工程代码量庞大，会导致javac/kotlinc以及dex等阶段耗时过长等。更多因素，各位可以举一反三去分析。



**用什么手段去优化？**

- 及时更新工具链
- 创建本地开发专用的构建变体

​	![131](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/131.png)

- 离线模式

​	![132](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/132.png)

- 创建库模块



**如何分析构建的性能瓶颈？**

对于实际项目的构建速度优化，还是需要**具体问题具体分析**、“先分析、后动手”，建议使用Gradle的`profile`工具，去生成一份耗时的报告，结合起来优化构建耗时。

```groovy
./gradlew :app:assembleDebug --offline --rerun-tasks --profile
```

![133](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/133.png)



## APK逆向入门

> 掌握简单的逆向知识有助于进行竞品分析、构建产物分析等工作，用到的几个工具如下：
>
> - [ui-automatorviewer](https://developer.android.com/training/testing/ui-automator) ： 用于查看/分析APK的UI布局层级
> - [dex2jar](https://github.com/pxb1988/dex2jar/releases)： 用于将APK包中的Dex文件反编译为包含class字节码的Jar包
> - [jd-gui](https://github.com/java-decompiler/jd-gui/releases)：用于以直观的形式去查看Jar包中的代码



## 安装包大小优化

![134](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/134.png)



























