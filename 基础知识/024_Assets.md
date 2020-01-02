# Assets

可参考：

+ [Android Studio增加assets目录、raw目录](https://www.jianshu.com/p/5974fcf88170)

Android资源文件分类

+ res目录下存放的可编译的资源文件

	这种资源文件系统会在R.java里面自动生成该资源文件的ID，所以访问这种资源文件比较简单，通过R.XXX.ID即可。存放在`res/raw`目录下

+ assets目录下存放的原生资源文件
	
	放在这个文件夹下面的文件不会被R文件编译，所以不能像第一种那样直接使用。Android提供了一个工具类,方便我们操作获取`assets`文件下的文件`AssetManager`

区别：

+ `assets`目录不会被映射到R中，因此，资源无法通过`R.id`方式获取，必须要通过`AssetManager`进行操作与获取；`res/raw`目录下的资源会被映射到R中，可以通过`getResource()`方法获取资源
+ 多级目录：`assets`下可以有多级目录，`res/raw`下不可以有多级目录

> assets可以看作随应用打包的微型文件系统， 支持任意层次的文件目录结构。因为这个优点，assets常用来加载大量图片和声音资源，如游戏 这样的应用。

`assets`目录中的所有文件都会随应用打包

## 创建assets目录

在`app`目录上右键，选择`New → Folder → Assets Folder`

![006](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/006.png)

不勾选`Change Folder Location`选项，保持`Target Source Set`的`main`选项不变，单击`Finish`按钮完成

![007](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/007.png)

结果如下：

![008](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/008.png)

可以在`assets`目录下创建子目录，`New → Directory`



## 处理assets

使用`AssetManager`类访问`assets`

1.获取`list(String)`方法获取对应path的assets数组

```java
public String[] list (String path)
```

该方法会抛[IOException](https://developer.android.com/reference/java/io/IOException.html)异常

```xml
    private void loadSounds() {
        String[] soundNames;

        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER);
            Log.i(TAG,  "Found " + soundNames.length + " sounds");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not list assets", e);
            return;
        }

        for (String fileName : soundNames) {

            String assetsPath = SOUNDS_FOLDER + "/" + fileName;
            Sound sound = new Sound(assetsPath);

            mSounds.add(sound);

        }

    }
```



