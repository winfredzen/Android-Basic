# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
# 最小版本要求
cmake_minimum_required(VERSION 3.4.1)

set(var hello)
message(${var})
set(var 111)
message(${var})

message(${CMAKE_CURRENT_LIST_FILE})
#cmake所在文件夹的路径
message(${CMAKE_CURRENT_LIST_DIR})

IF(TRUE)
    message("this is true")
ENDIF()

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

add_library( # Sets the name of the library.
        people-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        people/People.cpp)

add_library( # Sets the name of the library.
        dynamic-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        jni/jni_dynamic_load.cpp
        jni/jni_basic_type.cpp
        jni/jni_string.cpp
        base/jvm.cpp
        base/base.cpp
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

include_directories(${CMAKE_SOURCE_DIR}/base)
include_directories(people/)

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
        people-lib
        dynamic-lib
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
