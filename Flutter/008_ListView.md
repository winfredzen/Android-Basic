# ListView

参考：

+ [6.3 ListView](https://book.flutterchina.club/chapter6/listview.html)



## ListView.builder

`ListView.builder`适合列表项比较多（或者无限）的情况，因为只有当子组件真正显示的时候才会被创建

```dart
ListView.builder({
  // ListView公共参数已省略  
  ...
  @required IndexedWidgetBuilder itemBuilder,
  int itemCount,
  ...
})
```

+ `itemBuilder` - `IndexedWidgetBuilder`类型

  ```dart
  typedef IndexedWidgetBuilder = Widget Function(BuildContext context, int index);
  ```



```dart
class HomeContent extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: 100,
      itemExtent: 50, //强制高度为50
      itemBuilder: (BuildContext context, int index) {
        return Text('$index');
      },
    );
  }
}
```



## ListView.separated

> `ListView.separated`可以在生成的列表项之间添加一个分割组件，它比`ListView.builder`多了一个`separatorBuilder`参数，该参数是一个分割组件生成器

`separatorBuilder`类型为`IndexedWidgetBuilder`，跟上面的`itemBuilder`一致

```dart
class HomeContent extends StatelessWidget {

  Widget divider1 = Divider(color: Colors.blue);
  Widget divider2 = Divider(color: Colors.green);

  @override
  Widget build(BuildContext context) {


    return ListView.separated(
        itemBuilder: (BuildContext context, int index) {
          return ListTile(title: Text("$index"));
        },
        separatorBuilder: (BuildContext context, int index) {
          return index % 2 == 0 ? divider1 : divider2;
        },
        itemCount: 100
    );
  }
}
```

![015](https://github.com/winfredzen/Android-Basic/raw/master/Flutter/images/015.png)



```dart

class InfiniteListView extends StatefulWidget {

  @override
  State<StatefulWidget> createState() {
    return _InfiniteListViewState();
  }

}

class _InfiniteListViewState extends State<InfiniteListView> {

  static const loadingTag = "##loading##"; //表尾标记
  var _words = <String>[loadingTag];

  @override
  void initState() {
    super.initState();
    _retrieveData();
  }

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      itemCount: _words.length,
      itemBuilder: (context, index) {
        //如果到了表尾
        if (_words[index] == loadingTag) {
          //不足100条，继续获取数据
          if (_words.length - 1 < 100) {
            //获取数据
            _retrieveData();
            //加载时显示loading
            return Container(
              padding: const EdgeInsets.all(16.0),
              alignment: Alignment.center,
              child: SizedBox(
                  width: 24.0,
                  height: 24.0,
                  child: CircularProgressIndicator(strokeWidth: 2.0)
              ),
            );
          } else {
            //已经加载了100条数据，不再获取数据。
            return Container(
                alignment: Alignment.center,
                padding: EdgeInsets.all(16.0),
                child: Text("没有更多了", style: TextStyle(color: Colors.grey),)
            );
          }
        }
        //显示单词列表项
        return ListTile(title: Text(_words[index]));
      },
      separatorBuilder: (context, index) => Divider(height: .0),
    );
  }


  void _retrieveData() {
    Future.delayed(Duration(seconds: 2)).then((e) {
      setState(() {
        //重新构建列表，在某个位置插入数据
        _words.insertAll(_words.length - 1,
            //每次生成20个单词
            generateWordPairs().take(20).map((e) => e.asPascalCase).toList()
        );
      });
    });
  }

}
```

![016](https://github.com/winfredzen/Android-Basic/raw/master/Flutter/images/016.png)











