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

![007](https://github.com/winfredzen/Android-Basic/blob/master/images/008.png)







