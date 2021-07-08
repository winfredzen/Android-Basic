# buildTypes

在Android Stuido右侧的build variants下有2个默认的选项debug、release

参考[配置 build 变体](https://developer.android.com/studio/build/build-variants?hl=zh-cn):

> 当您创建新模块时，Android Studio 会自动为您创建“debug”build 类型和“release”build 类型。虽然“debug”build 类型没有出现在构建配置文件中，但 Android Studio 会使用 [`debuggable true`](https://google.github.io/android-gradle-dsl/current/com.android.build.gradle.internal.dsl.BuildType.html#com.android.build.gradle.internal.dsl.BuildType:debuggable) 配置它。这样，您就可以在安全的 Android 设备上调试应用，并使用常规调试密钥库配置 APK 签名。



## signingConfigs

为什么要使用`signingConfigs`？我自己尝试了，在`build variants`下，选择release，准备直接在真机上运行，发现并不能这样做

![005](https://github.com/winfredzen/Android-Basic/blob/master/Gradle/images/005.png)

