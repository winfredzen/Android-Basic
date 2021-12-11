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



创建项目后的目录结构如下：

![006](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/006.png)





## CMakeLists.txt

`CMakeLists.txt`内容如下：

```java
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("hellojni")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        hellojni

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        hellojni

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
```























