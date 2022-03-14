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



对上面内容的解释

```cmake
# 指定 CMake 最低版本
cmake_minimum_required(VERSION 3.10.2)
```

设置格式说明 : VERSION 是固定必须写的 , VERSION 后面就是 CMake 的版本号 ;



`project` 命令用于声明工程名称 , 同时还可以指定工程支持的语言 , 其中支持的语言可以忽略, 默认支持所有的语言 

```cmake
# 声明项目并为项目命名.
project("cmake")
```



`add_library` 命令用于设置生成函数库 , 这个函数库包括 so 动态库 和 a 静态库 

`add_library` 命令三个参数说明 :

+ 参数 1 : 设置生成的动态库名称.
+ 参数 2 : 设置生成的函数库类型 : ① 静态库 STATIC ② 动态库 SHARED.
+ 参数 3 : 配置要编译的源文件.

```cmake
# 创建函数库并为函数库命名.
# 函数库类型 : 设置该函数库的类型 ① 静态库 STATIC ② 动态库 SHARED.
# 相对路径 : 指定源码路径, 注意是相对路径.
# 函数库个数据 : 可以定义多个函数库, CMake 会负责构建这些函数库.
# 动态库打包 : Gradle 会自动将动态库打包到 APK 安装包中.
add_library( # 参数 1 : 设置生成的动态库名称.
        native-lib
        # 参数 2 : 设置生成的函数库类型 : ① 静态库 STATIC ② 动态库 SHARED.
        SHARED
        # 参数 3 : 配置要编译的源文件.
        native-lib.cpp )
```



`find_library` 命令的作用是用于搜索函数库 , 找到的函数库的全路径名称保存到第一个参数变量中 

```cmake
# 搜索存储 : 搜索指定的预编译库, 并存储该预编译库的路径到变量中, 这里存储到了 log-lib 变量中.
# 指定库名称 : CMake 的搜索路径默认包含了系统库, 只需要指定想添加的公共 NDK 库的名称即可, 这里指定 log 即可.
#            不需要指定 log 库的完整路径 ndk-bundle\platforms\android-29\arch-arm\usr\lib\liblog.so.
# 验证存在性 : 在完成编译之前, CMake 会验证该函数库是否存在.
# 到预设的目录查找 log 库 , 将找到的路径赋值给 log-lib
#   这个路径是 NDK 的 ndk-bundle\platforms\android-29\arch-arm\usr\lib\liblog.so
#   不同的 Android 版本号 和 CPU 架构 需要到对应的目录中查找 , 此处是 29 版本 32 位 ARM 架构的日志库
find_library( # 设置保存函数库路径的变量名称.
        log-lib
        # 指定 CMake 想要定位的 NDK 库名称
        log )
```



`target_link_libraries` 命令的作用是为目标函数库添加需要链接的函数库 

目标函数库 : 就是在 Java / Kotlin 代码中使用 `System.loadLibrary(“native-lib”)` 加载的动态库 , 这是整个 C/C++ 代码的调用入口 ;

链接多个函数库 : 指定 CMake 应该连接到目标函数库的若干函数库. 可以链接多个函数库, 如使用的 Android NDK 函数库, 预编译的第三方函数库, 系统库等.

链接函数库命令参数说明 :

```cmake
参数 1 : 本构建脚本要生成的动态库目标
参数 2 ~ … : 后面是之前预编译的动态库或静态库 , 或引入的动态库
# 指定 CMake 应该连接到目标函数库的若干函数库.
# 可以链接多个函数库, 如使用的 Android NDK 函数库, 预编译的第三方函数库, 系统库等.
# 链接函数库
#       参数 1 : 本构建脚本要生成的动态库目 标
#       参数 2 ~ ... : 后面是之前预编译的动态库或静态库 , 或引入的动态库
target_link_libraries( # 指定目标函数库.
        native-lib
        # 链接目标库到 NDK 中包含的日志库.
        ${log-lib} )
```



参考：

+ [Android NDK开发CMakeLists文件配置解析](https://wecode.xyz/Android-NDK%E5%BC%80%E5%8F%91CMakeLists%E6%96%87%E4%BB%B6%E9%85%8D%E7%BD%AE%E8%A7%A3%E6%9E%90)

**Gradle 配置解析**

```groovy
android {
  ...
  defaultConfig {...}

  // Encapsulates your external native build configurations.
  externalNativeBuild {
    // Encapsulates your CMake build configurations.
    cmake {
      // Provides a relative path to your CMake build script.
      path "CMakeLists.txt"
    }
  }
}
```

在模块中的`build.gradle`文件的`android{}`块中的 `externalNativeBuild`指定了 `CMakeLists` 文件的相对路径，这里的`CMakeLists`文件是在工程目录所以直接写文件名称。

此外还可以在` defaultConfig`块再指定一个`externalNativeBuild`块，指定一些通用的配置。

```groovy
android {
    ...
    defaultConfig {
        ...
        externalNativeBuild {
            // 这里可以配置一些通用的配置
            cmake {
                // 传递可选的参数到cmake中
                arguments "-DANDROID_ARM_NEON=TRUE", "-DANDROID_TOOLCHAIN=clang"
                cppFlags "-frtti -fexceptions"
                // Gradle 会构建这些ABI配置,但打包时仅把 ndk 块中的 ABI 打包进apk
                abiFilters 'x86', 'x86_64', 'armeabi', 'armeabi-v7a','arm64-v8a'
            }
        }
        ndk {
      		// 打包时仅仅会把此块中的 ABI 打包进apk
      		abiFilters 'armeabi-v7a','arm64-v8a'
    	}
    }
    externalNativeBuild {...}
}
```





