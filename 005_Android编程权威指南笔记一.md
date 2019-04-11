# Android编程权威指南笔记一

**R.java文件**

可在目录`app/build/generated/not_namespaced_r_class_sources/debug/processDebugResources/r/`下，项目包名称中找到`R.java`文件。`R.java`文件在Android项目编译过程中自动生成

**快捷键Option+Return**

使用`Option+Return`(`Alt+Enter`)组合键，让Android Studio自动为你导入

**资源ID总数int类型**



**配置Android Studio识别成员变量的m前缀**

Mac下，选择`Preferences`->`Editor`->`Code Style`->`Java` 选择Code Generation

在Naming表单的Field一行中，添加m作为前缀

![007](https://github.com/winfredzen/Android-Basic/blob/master/images/007.png)

设置后，需要Android Studio为`mTextResId`(int)生成获取方法时，它生成的是`getTextResId()`而不是`getMTextResId()`;而在为`mAnswerTrue`(boolean)生成获取方法时，生成的是`isAnswerTrue()`而不是`isMAnswerTrue()`。



**图标资源**

drawable-mdpi、drawable-hdpi、drawable-xhdpi和drawable-xxhdpi四个目录

+ mdpi:中等像素密度屏幕(约160dpi)
+ hdpi:高像素密度屏幕(约240dpi)
+ xhdpi:超高像素密度屏幕(约320dpi)
+ xxhdpi:超超高像素密度屏幕(约480dpi)

任何添加到res/drawable目录中、后缀为.png .jpg或者.gif的文件都会自动获得资源ID(注意，文件名必须是小写字母且不能有任何空格符号)



## activity的生命周期

每个Activity都有其生命周期，在其生命周期类，activity在运行、暂停、停止和不存在这四种状态键转换

![008](https://github.com/winfredzen/Android-Basic/blob/master/images/008.png)

| 状态   | 有内存实例 | 用户可见 | 处于前台 |
| ------ | ---------- | -------- | -------- |
| 不存在 | 否         | 否       | 否       |
| 停止   | 是         | 否       | 否       |
| 暂停   | 是         | 是或部分 | 否       |
| 运行   | 是         | 是       | 是       |

![010](https://github.com/winfredzen/Android-Basic/blob/master/images/010.png)

应用启动并创建Activity初始实例后：

+ onCreate()
+ onStart()
+ onResume()

Activity处于运行状态(在内存里，用户可见，活动在前台)

单机后退键

+ onPause()
+ onStop()
+ onDestroy()

Activity处于不存在状态(不在内存里，显然不可见，不会活动在前台)

如果启动后，再单机主屏幕键

+ onPause()
+ onStop() 
+ onSaveInstanceState

Activity已处于停止状态(在内存中，但不可见，不会活动在前台)

然后再点击最近应用键

+ onStart()
+ onResume()

可发现，系统没有调用`onCreate()`



-----

**创建过滤filter**

选择`Edit Filter Configuration`选项

![009](https://github.com/winfredzen/Android-Basic/blob/master/images/009.png)



























