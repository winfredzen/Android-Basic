# CMake以及NDK基础

内容来自：

+ [Android CMake以及NDK实践基础](https://www.imooc.com/learn/1212)

创建NDK项目

![007](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/007.png)

![008](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/008.png)

project的目录结构

![011](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/011.png)

在app模块下的build.gradle中，也多了一些配置

![012](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/012.png)

创建的project中，有个默认的例子，输出一个字符串

![009](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/009.png)

![010](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/010.png)

编译出来的.so库的路径地址

![013](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/013.png)



## CMake编译

默认创建的工程，cmake的版本是3.10.2，视频博主说有点问题，需要替换为3.6.0

```cmake
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
# 最小版本要求
cmake_minimum_required(VERSION 3.4.1)

# Declares and names the project.

project("myapplication")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
# 将cpp文件编译成动态库的一个语法
add_library( # Sets the name of the library.
        myapplication

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

# 查找一些动态库，查找Android中log库，并命名为log-lib
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

# 动态库关联的语法
target_link_libraries( # Specifies the target library.
        myapplication

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
```

CMake创建变量，使用set，也可以是大写，后面是变量名，再后面是变量值

```cmake
set(var hello)
```

可通过`message(${var})`输出打印内容

在Gradle的Tasks中other条目下，选择`externalNativeBuildDebug`（注意，要先清空缓存，清空`build`和`.cxx`目录下的内容）

![014](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/014.png)

```cmake
set(var hello)
message(${var})
set(var 111)
message(${var})

message(${CMAKE_CURRENT_LIST_FILE})
#cmake所在文件夹的路径
message(${CMAKE_CURRENT_LIST_DIR})
```

![015](https://github.com/winfredzen/Android-Basic/blob/master/NDK/images/015.png)

















