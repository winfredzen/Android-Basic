# SharedPreferences

本质上是一个xml，使用键值对存储数据

位于程序私有目录，位于`data/data/[packageName]/shared_prefs`

即使覆盖安装，文档也会依然存在

**四种操作模式**

```xml
/** 默认操作模式,代表该文件是私有数据,只能被应用本身访问,
  * 在该模式下,写入的内容会覆盖原文件的内容*/
Context.MODE_PRIVATE
/** 该模式会检查文件是否存在,存在就往文件追加内容,否则就创建新文件*/
Context.MODE_APPEND
/** 当前文件可以被其他应用读取*/
Context.MODE_WORLD_READABLE
/** 当前文件可以被其他应用写入*/
Context.MODE_WORLD_WRITEABLE
```

**获取SharedPreferences的两种方式**

+ 调用Context对象的`getSharedPreferences()`方法：获得的`SharedPreferences`对象可以被同一应用程序下的其他组件共享.
+ 调用Activity对象的`getPreferences()`方法：获得的`SharedPreferences`对象只能在该Activity中使用



如下的例子， 存储信息：

```java
SharedPreferences sharedPreferences = getSharedPreferences("myshare", MODE_PRIVATE);
SharedPreferences.Editor editor = sharedPreferences.edit();
editor.putString("account", account);
editor.putString("pwd", pwd);
editor.commit();
```

存储数据的位置和形式：

![003](https://github.com/winfredzen/Android-Basic/blob/master/Database%26Cache/images/003.png)

















