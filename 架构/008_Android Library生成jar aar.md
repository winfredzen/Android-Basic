# Android Library 生成Jar AAR



## 生成Jar

文章[Android将library打包成jar文件或aar文件](https://www.jianshu.com/p/f504192cc00c)有如下gradle代码：

```groovy
task makeJar(type: Copy) {
    //删除存在的
    delete 'build/libs/mysdk.jar'
    //设置拷贝的文件
    from('build/intermediates/bundles/release/')
    //打进jar包后的文件目录
    into('build/libs/')
    //将classes.jar放入build/libs/目录下
    //include ,exclude参数来设置过滤
    //（我们只关心classes.jar这个文件）
    include('classes.jar')
    //重命名
    rename ('classes.jar', 'mysdk.jar')
}
```

在Android Studio中的`Terminal`中输入：

```shell
./gradlew makeJar
```

发现并没有生成jar，原因是生成的`classes.jar`路径不再是上面的位置了

目前的路径如下：

![039](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/039.png)





























