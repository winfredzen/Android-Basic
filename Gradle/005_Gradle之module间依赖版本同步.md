# Gradle之module间依赖版本同步

在学习ARouter源码的过程中有遇到如下的代码：

![013](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/012.png)

这是什么意思呢？

可参考：

+ [Gradle之module间依赖版本同步](https://juejin.cn/post/6844903725341868039)



> ## 问题引出
>
> 因为公司项目日渐增大，所以想尝试下组件化，为以后业务扩展做准备。但是拆分之后的模块和之前引入的lib中，都含有相同的导包，以后升级也变得很麻烦，就希望可以在项目中个定义一个统一的资源模块加以引用，做到统一管理。
>
> ## 解决方案
>
> ### 定义配置文件
>
> 在项目的根目录中有一个*gradle.properties* ,其中定义的常量是可以在*gradle* 中直接引用的。我们先定义一些常用的值：
>
> ```groovy
> # Android configuration
> COMPILE_SDK_VERSION=27
> BUILD_TOOLS_VERSION=27.0.3
> TARGET_SDK_VERSION=27
> MIN_SDK_VERSION=16
> VERSION_CODE=1
> VERSION_NAME=1.0
> 
> 
> # Plugin versions
> KOTLIN_VERSION=1.2.71
> SUPPORT_LIBRARY_VERSION=27.1.1
> CONSTRAINT_LAYOUT_VERSION=1.1.3
> RUNNER_VERSION=1.0.2
> ESPRESSO_CORE_VERSION=3.0.2
> JUNIT_VERSION=4.12
> BUTTERKNIFE_VERSION=8.4.0
> GLIDE_VERSION=3.7.0
> 
> ```
>
> 现在我们在gradle文件中引用一下就可以了：
>
> ```groovy
> android {
>     compileSdkVersion Integer.parseInt(COMPILE_SDK_VERSION)
>     buildToolsVersion BUILD_TOOLS_VERSION
>     defaultConfig {
>         applicationId "com.be.www.multimodelproject"
>         minSdkVersion MIN_SDK_VERSION
>         targetSdkVersion TARGET_SDK_VERSION
>         versionCode Integer.parseInt(VERSION_CODE)
>         versionName VERSION_NAME
> 
>         testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
> 
>     }
>     buildTypes {
>         release {
>             minifyEnabled false
>             proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
>         }
>     }
> }
> 
> dependencies {
>     implementation fileTree(dir: 'libs', include: ['*.jar'])
>     implementation "com.android.support:appcompat-v7:$SUPPORT_LIBRARY_VERSION"
>     implementation "com.android.support.constraint:constraint-layout:$CONSTRAINT_LAYOUT_VERSION"
>     testImplementation "junit:junit:$JUNIT_VERSION"
>     androidTestImplementation "com.android.support.test:runner:$RUNNER_VERSION"
>     androidTestImplementation "com.android.support.test.espresso:espresso-core:$ESPRESSO_CORE_VERSION"
> }
> 
> ```
>
> - 需要注意的是：*compileSdkVersion* 和 *versionCode* 必须为Integer类型，而Gradle支持类型类型解析（Gradle的支持语言是Groovy，而Groovy的底层是java，好多类型和语法都和java相近），我们只需要*Integer.parseInt()* 一下就好了。对于implementation，我们要注意使用的是“双引号”（""），因为“单引号”是不支持动态赋值的，之后用美元符（$）引入即可。
>
> 到此来说，已经实现了依赖版本同步的问题。但是如果我每个module中都需要一个*"com.android.support:appcompat-v7:$SUPPORT_LIBRARY_VERSION"* ,也就是我的每个代码块都会存在，显得臃肿和麻烦。我们可以在根目录的gradle中定义一些常用的引用：
>
> ```groovy
> buildscript {
>     ext.kotlin_version = '1.2.41'
>     ext.deps = [
> 
>             'junit' : "junit:junit:$JUNIT_VERSION",
>             'kotlin': [
>                     'kgp'        : "org.jetbrains.kotlin:kotlin-gradle-plugin:${KOTLIN_VERSION}",
>                     'kotlin_jdk7': "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${KOTLIN_VERSION}"
>             ]
>     ]
>     repositories {
>         google()
>         jcenter()
>     }
>     dependencies {
>         classpath 'com.android.tools.build:gradle:3.0.0'
>         classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
> 
>         // NOTE: Do not place your application dependencies here; they belong
>         // in the individual module build.gradle files
>     }
> }
> 
> ```
>
> 语法就是*ext.deps* 样式。引用方式：
>
> ```groovy
>  implementation deps.kotlin.kotlin_jdk7
>  testImplementation deps.junit
> 
> ```
>
> 是不是代码清爽很多。
>
> 我们还可以注意到上边的*ext.kotlin_version = '1.2.41'* ，也就是说常量也可以在gradle中定义。但是个人比较喜欢清单的形式。

