# Divider

参考：

+ [Divider class](https://api.flutter.dev/flutter/material/Divider-class.html)



`Divider`用来实现分隔线效果

构造函数

```dart
  const Divider({
    Key key,
    this.height,
    this.thickness,
    this.indent,
    this.endIndent,
    this.color,
  })
```



官方例子：

```dart
/// Flutter code sample for Divider

// This sample shows how to display a Divider between an orange and blue box
// inside a column. The Divider is 20 logical pixels in height and contains a
// vertically centered black line that is 5 logical pixels thick. The black
// line is indented by 20 logical pixels.
//
// ![](https://flutter.github.io/assets-for-api-docs/assets/material/divider.png)

import 'package:flutter/material.dart';

void main() => runApp(MyApp());

/// This is the main application widget.
class MyApp extends StatelessWidget {
  static const String _title = 'Flutter Code Sample';

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: _title,
      home: Scaffold(
        appBar: AppBar(title: const Text(_title)),
        body: MyStatelessWidget(),
      ),
    );
  }
}

/// This is the stateless widget that the main application instantiates.
class MyStatelessWidget extends StatelessWidget {
  MyStatelessWidget({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        children: <Widget>[
          Expanded(
            child: Container(
              color: Colors.amber,
              child: const Center(
                child: Text('Above'),
              ),
            ),
          ),
          const Divider(
            color: Colors.black,
            height: 0,
            thickness: 0,
            indent: 0,
            endIndent: 0,
          ),
          Expanded(
            child: Container(
              color: Colors.blue,
              child: const Center(
                child: Text('Below'),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

```

![012](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/012.png)

```dart
          const Divider(
            color: Colors.black,
            height: 1,
            thickness: 0,
            indent: 0,
            endIndent: 0,
          ),
```

![013](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/013.png)



```dart
          const Divider(
            color: Colors.black,
            height: 10,
            thickness: 5,
            indent: 20,
            endIndent: 0,
          ),
```

![014](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/014.png)



