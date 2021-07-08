# buildTypes

在Android Stuido右侧的build variants下有2个默认的选项debug、release

参考[配置 build 变体](https://developer.android.com/studio/build/build-variants?hl=zh-cn):

> 当您创建新模块时，Android Studio 会自动为您创建“debug”build 类型和“release”build 类型。虽然“debug”build 类型没有出现在构建配置文件中，但 Android Studio 会使用 [`debuggable true`](https://google.github.io/android-gradle-dsl/current/com.android.build.gradle.internal.dsl.BuildType.html#com.android.build.gradle.internal.dsl.BuildType:debuggable) 配置它。这样，您就可以在安全的 Android 设备上调试应用，并使用常规调试密钥库配置 APK 签名。



**signingConfigs**

为什么要使用`signingConfigs`？我自己尝试了，在`build variants`下，选择release，准备直接在真机上运行，发现并不能这样做

![005](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/005.png)

这个时候`signingConfigs`就派的上用场了，参考：

+ [Android Studio Build Variants 切换 release模式，不能运行](https://blog.csdn.net/lijia1201900857/article/details/89397403)
+ [android 使用signingConfigs进行打包](https://blog.csdn.net/bzlj2912009596/article/details/78188570)

```groovy
android {
    signingConfigs {
        release {
            storeFile file('/Users/wangzhen/Documents/Android/BroadcastDemo/release.jks')
            storePassword '123456'
            keyAlias 'key0'
            keyPassword '123456'
        }
    }
    
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
        debug {
            applicationIdSuffix ".debug"
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

进行如上的设置后就可以在手机上跑release版本了

-----

如下的内容，参考自：

+ [Android Build Types](https://www.journaldev.com/21533/android-build-types-product-flavors)

```groovy
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
        debug {
            applicationIdSuffix ".debug"
            versionNameSuffix "-debug"
        }
        beta {
            applicationIdSuffix ".beta"
            versionNameSuffix "-beta"
        }
    }
```

现在有3个build variants

![006](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/006.png)

`applicationIdSuffix`在默认的应用 ID 上追加一段，可参考：

+ [设置应用 ID](https://developer.android.com/studio/build/application-id?hl=zh-cn)

> `versionName` does the same on the version name present in the defaultConfig.

选择`debug`在手机上运行下，在“应用信息”中，查看应用信息，如下：

![007](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/007.jpg)



## BuildConfig

可参考：

+ [BuildConfig与build.gradle的关系](https://www.jianshu.com/p/3d9b23afe514)
+ [Gradle之BuildConfig自定义常量](https://www.jianshu.com/p/274c9d95cf76)



> The `BuildConfig.java` class is auto-generated when different buildFlavors are created.

`buildType`生成在`app\build\generated\source\buildConfig\debug(release)\`包名下

如下的配置：

```groovy
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
        debug {
            applicationIdSuffix ".debug"
            versionNameSuffix "-debug"
            buildConfigField "String", "TYPE", '"I AM A DEBUG NINJA"'
        }
        beta {
            signingConfig signingConfigs.release
            applicationIdSuffix ".beta"
            versionNameSuffix "-beta"
            buildConfigField "String", "TYPE", '"I AM A BETA NINJA"'
        }
    }
```

build后生成的`BuildConfig.java`

![008](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/008.png)

如beta对一个的`BuildConfig.java`，内容如下：

```java
public final class BuildConfig {
  public static final boolean DEBUG = false;
  public static final String APPLICATION_ID = "com.example.broadcastdemo.beta";
  public static final String BUILD_TYPE = "beta";
  public static final int VERSION_CODE = 1;
  public static final String VERSION_NAME = "1.0-beta";
  // Field from build type: beta
  public static final String TYPE = "I AM A BETA NINJA";
}
```



在beta下添加`resValue "string", "my_name", "Anupam Beta"`，会在`resources | strings.xml`下自动创建

![009](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/009.png)









































