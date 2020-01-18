# ListTile

参考：

+ [ListTile class](https://api.flutter.dev/flutter/material/ListTile-class.html)

ListTile的基本内容介绍，可参考[Flutter 入门之 ListTile 使用指南](https://juejin.im/post/5c88d6c4f265da2de970bc24)

这里主要看下如何自定义List Item

## 自定义List Item

以官方文档中例子来说

如下效果：

![001](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/001.png)

自定义`CustomListItem`

```dart
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class CustomListItem extends StatelessWidget {
  const CustomListItem({this.thumbnail, this.title, this.user, this.viewCount});

  final Widget thumbnail;
  final String title;
  final String user;
  final int viewCount;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 5.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Expanded(
            flex: 2,
            child: thumbnail,
          ),
          Expanded(
            flex: 3,
            child: _VideoDescription(title: title, user: user, viewCount: viewCount),
          ),
          const Icon(Icons.more_vert, size: 16),
        ],
      ),
    );
  }
}

class _VideoDescription extends StatelessWidget {
  const _VideoDescription({Key key, this.title, this.user, this.viewCount})
      : super(key: key);

  final String title;
  final String user;
  final int viewCount;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(5.0, 0, 0, 0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(title,
              style:
                  const TextStyle(fontWeight: FontWeight.w500, fontSize: 14.0)),
           Padding(
            padding: EdgeInsets.symmetric(vertical: 2.0),
            child: Text(
              user,
              style: const TextStyle(fontSize: 10.0),
            ),
          ),
          Padding(
            padding: EdgeInsets.symmetric(vertical: 1.0),
            child: Text(
              '$viewCount views',
              style: const TextStyle(fontSize: 10.0),
            ),
          ),
        ],
      ),
    );
  }
}

```

在`ListView`中使用`CustomListItem`

```dart
      body: ListView(
        padding: const EdgeInsets.all(8.0),
        itemExtent: 160.0,
        children: <Widget>[
          CustomListItem(
            user: 'Flutter',
            viewCount: 99999,
            thumbnail: Container(
              decoration: const BoxDecoration(color: Colors.blue),
            ),
            title: 'The Flutter YouTube Channel',
          ),
          CustomListItem(
            user: 'Dash',
            viewCount: 884000,
            thumbnail: Container(
              decoration: const BoxDecoration(color: Colors.yellow),
            ),
            title: 'Announcing Flutter 1.0',
          ),
        ],
      ),
```

