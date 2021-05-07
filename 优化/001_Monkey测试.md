# Monkey测试

> Monkey 是Android SDK提供的一个命令行工具， 可以简单，方便地运行在任何版本的Android模拟器和实体设备上。 Monkey会发送伪随机的用户事件流，适合对app做压力测试。
>
> 我们使用Monkey主要用来测试发现crash。
>
> 虽然Monkey测试有部分缺陷，我们无法准确地得知重现步骤， Monkey测试所出现的NullPointException, 都是可以在用户使用时出现的， 何时出现只是时间问题
>
> 理论上来说， Monkey所有的Crash 都需要在发布前修复掉



参考：

+ [UI/Application Exerciser Monkey](https://developer.android.com/studio/test/monkey)
+ [Monkey测试](https://www.jianshu.com/p/a2f884529255)
+ [Android Monkey 压力测试 介绍](https://www.cnblogs.com/TankXiao/p/4815134.html)

常用的命令

```sh
  #禁用通知栏
  adb shell settings put global policy_control immersive.full=*
  adb shell monkey -p com.example.app -s 2333 --pct-touch 70 --pct-motion 30 --ignore-crashes --ignore-timeouts --monitor-native-crashes --throttle 200 -v -v 500
 #解禁通知栏
  adb shell settings put global policy_control null
```



**adb monkey测试屏蔽通知栏**

参考：

+ [无埋点数据收集和adb monkey测试屏蔽通知栏](https://www.cnblogs.com/fuyaozhishang/p/8998038.html)



