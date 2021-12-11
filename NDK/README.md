# NDK



## 创建 android ndk 项目

![001](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/001.png)

在创建项目后，build一直卡住，有如下的提示：

![002](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/002.png)

按网上的说法，[NDK is not configured issue in android studio](https://stackoverflow.com/questions/29122903/ndk-is-not-configured-issue-in-android-studio)

1.在`local.properties`中配置NDK

如`ndk.dir=/Users/wangzhen/Library/Android/sdk/ndk/23.1.7779620`

2.在`Project Structure`下载配置，但我尝试后，有如下的提示`NDK does not contain any platforms`：

![003](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/003.png)



另外，在官方文档[local.properties 文件中的 ndk.dir 设置已弃用](https://developer.android.com/studio/releases?utm_source=android-studio#4-0-0-ndk-dir)有如下的说明：

![004](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/004.png)

所以参考[在项目中配置特定版本的 NDK](https://developer.android.com/studio/projects/install-ndk#apply-specific-version)，在`build.gradle` 文件中使用 `android.ndkVersion` 属性指定相应的版本

![005](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/005.png)





























