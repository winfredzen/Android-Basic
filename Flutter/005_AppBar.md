# AppBar

参考：

+ [AppBar class](https://api.flutter.dev/flutter/material/AppBar-class.html)

一些属性说明；

+ flexibleSpace - 一个显示在AppBar下方的组件，高度和AppBar高度一样，可以实现一些特殊的效果

+ bottom - 位于flexibleSpace下方

如下图所示：

![007](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/007.png)

```dart
      appBar: AppBar(
        title: Text(widget.title),
        leading: IconButton(
          icon: Icon(Icons.menu),
          onPressed: (){},
        ),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.search),
            onPressed: (){},
          ),
          IconButton(
            icon: Icon(Icons.more_vert),
            onPressed: (){},
          ),
        ],
        flexibleSpace: SafeArea(
          child: Icon(
            Icons.photo_camera,
            size: 75.0,
            color: Colors.white70,
          ),
        ),
        bottom: PreferredSize(
          child: Container(
            color: Colors.lightGreen.shade100, height: 75.0,
            width: double.infinity,
            child: Center(
              child: Text('Bottom'), ),
          ),
          preferredSize: Size.fromHeight(75.0),
        ),
      ),
```

这里用到了`SafeArea`，`SafeArea`主要是针对刘海屏，效果如下：

![008](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/008.png)

















