# SizedBox

参考：

+ [SizedBox class](https://api.flutter.dev/flutter/widgets/SizedBox-class.html)

SizeBox是一个特定大小的box。

构造方法：

```dart
SizedBox({Key key, double width, double height, Widget child })
```

>If given a child, this widget forces its child to have a specific width and/or height (assuming values are permitted by this widget's parent). If either the width or height is null, this widget will size itself to match the child's size in that dimension.
>
>如果有child，该组件会强制其child有指定的宽度或者高度。 如果width或height为null，则此widget将自行调整大小以匹配该dimension中child的大小。

> If not given a child, [SizedBox](https://api.flutter.dev/flutter/widgets/SizedBox-class.html) will try to size itself as close to the specified height and width as possible given the parent's constraints. If [height](https://api.flutter.dev/flutter/widgets/SizedBox/height.html) or [width](https://api.flutter.dev/flutter/widgets/SizedBox/width.html) is null or unspecified, it will be treated as zero.
>
> 如果没有child，SizeBox会在给定parent约束的情况下尝试将自身的尺寸调整为尽可能接近指定的高度和宽度。 如果height或width为null或未指定，则将其视为零。

新的`SizedBox.expand`构造函数可用于制作一个SizedBox，其大小可调整以适合父级。 等效于将width和height设置为`double.infinity`

例如，对一个button我们想设置其高度或者宽度为一个确定的大小，我们将这个button放在SizedBox中

```dart
      body: SizedBox(
        width: 200,
        height: 300,
        child: RaisedButton(
          child: Text('Button'),
          onPressed: (){},
        ),
      ),
```

![002](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/002.png)

如果将宽或者高设置为`double.infinity`，其长度将会扩展为parent的大小

```dart
      body: SizedBox(
        width: double.infinity,
        height: 300,
        child: RaisedButton(
          child: Text('Button'),
          onPressed: (){},
        ),
      ),
```

![003](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/003.png)

`SizedBox.expand`等同与如下的效果

```dart
      body: SizedBox(
        width: double.infinity,
        height: double.infinity,
        child: RaisedButton(
          child: Text('Button'),
          onPressed: (){},
        ),
      ),
```

![004](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/004.png)









