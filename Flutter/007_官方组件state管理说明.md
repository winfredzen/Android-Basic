# 官方组件state管理说明

参考：

+ [Adding interactivity to your Flutter app](https://flutter.dev/docs/development/ui/interactive)

如何决定用哪种state管理状态的方式？

+ If the state in question is user data, for example the checked or unchecked mode of a checkbox, or the position of a slider, then the state is best managed by the parent widget.

  如果是用户数据，例如checkbox的勾选or没有勾选，slider的位置，此时最好由父组件来管理

+ If the state in question is aesthetic, for example an animation, then the state is best managed by the widget itself.

  如果state是外观的，例如动画，最好由组件自己管理

如何确定不了，那就由父组件来管理



## 组件自己管理state

如下的例子：

```dart
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '状态管理',
      home: Scaffold(
        appBar: AppBar(
          title: Text('组件自己管理状态'),
        ),
        body: Center(
          child: TapboxA(),
        ),
      ),
    );
  }
}

class TapboxA extends StatefulWidget {

  @override
  _TapboxAState createState() => _TapboxAState();

}

class _TapboxAState extends State<TapboxA> {

  bool _active = false;

  void _handleTap() {
    setState(() {
      _active = !_active;
    });
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: _handleTap,
      child: Container(
        child: Center(
          child: Text(
            _active ? 'Active' : 'Inactive',
            style: TextStyle(fontSize: 12.0, color: Colors.white),
          ),
        ),
        width: 200,
        height: 200,
        decoration: BoxDecoration(
          color: _active ? Colors.lightGreen[700] : Colors.grey[600],
        ),
      ),
    );
  }
}
```

效果如下：

![010](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/010.png)

![011](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/011.png)





























