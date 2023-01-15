# Launcher

可参考：

1.[Android -- Launcher 独立编译](https://www.heqiangfly.com/2021/01/06/Android%20Launcher/android-launcher-compile-as/)

https://github.com/heqiangflytosky/QLauncher3/tree/android11 处下载代码，但是下载代码后编译会有问题，需要做一些修改。如：

a.`gradle.properties`中增加`android.jetifier.blacklist = annotation-experimental-1.3.0.aar`

b.`build.gradle`中增加

```groovy
configurations.all {
    resolutionStrategy {
        force 'androidx.core:core:1.6.0'
    }
}
```

2.[https://github.com/Launcher3-dev/Launcher3](https://github.com/Launcher3-dev/Launcher3)



不知道为啥编译需要使用`./gradlew assembleAospWithoutQuickstep`



3.[https://github.com/luohaohaha/launcher3](https://github.com/luohaohaha/launcher3)



## 其它文章

1.[Launcher3源码分析系列](https://fookwood.com/android)



