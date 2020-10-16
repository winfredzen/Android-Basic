# Layout官方例子

参考：

+ [Building layouts](https://flutter.dev/docs/development/ui/layout/tutorial)

代码如下：

```dart
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {

    Widget titleSection = Container(
      padding: EdgeInsets.all(32),
      child: Row(
        children: [
          Expanded(
              //占据剩余空间
            child: Column(
            crossAxisAlignment: CrossAxisAlignment.start, //左对齐
            children: [
              Container(
                child: Text(
                  'Oeschinen Lake Campground',
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                  ),
                ),
                padding: const EdgeInsets.only(bottom: 8), //底部padding
              ),
              Text(
                'Kandersteg, Switzerland',
                style: TextStyle(
                  color: Colors.grey[500],
                ),
              ),
            ],
          )),
          FavoriteWidget(),
        ],
      ),
    );

    Column _buildButtonColumn(Color color, IconData icon, String label) {
      return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        mainAxisSize: MainAxisSize.min, //?
        children: [
          Icon(icon, color: color),
          Container(
            padding: EdgeInsets.only(top: 8),
            child: Text(
              label,
              style: TextStyle(
                  fontSize: 12,
                  fontWeight: FontWeight.w400,
                  color: color
              ),
            ),
          ),

        ],
      );
    }

    Color color = Theme.of(context).primaryColor;

    Widget buttonSection = Container(
      child: Row(
        children: [
          _buildButtonColumn(color, Icons.call, 'CALL'),
          _buildButtonColumn(color, Icons.near_me, 'ROUTE'),
          _buildButtonColumn(color, Icons.share, 'SHARE'),
        ],
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      ),
    );

    Widget textSection = Container(
      padding: EdgeInsets.all(32),
      child: Text(
        'Lake Oeschinen lies at the foot of the Blüemlisalp in the Bernese '
            'Alps. Situated 1,578 meters above sea level, it is one of the '
            'larger Alpine Lakes. A gondola ride from Kandersteg, followed by a '
            'half-hour walk through pastures and pine forest, leads you to the '
            'lake, which warms to 20 degrees Celsius in the summer. Activities '
            'enjoyed here include rowing, and riding the summer toboggan run.',
        softWrap: true,
      ),
    );

    return MaterialApp(
      title: 'Flutter Layout Demo',
      home: Scaffold(
        appBar: AppBar(
          title: Text("Flutter Layout Demo"),
        ),
        body: ListView( //可以滚动
          children: [
            Image.asset(
              "images/lake.jpg",
              width: 600,
              height: 240,
              fit: BoxFit.cover,
            ),
            titleSection,
            buttonSection,
            textSection,
          ],
        ),
      ),
    );
  }
}

//有状态组件

class FavoriteWidget extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _FavoriteWidgetState();
  }
}

class _FavoriteWidgetState extends State<FavoriteWidget> {

  bool _isFavorited = true;
  int _favoriteCount = 41;

  void _toggleFavorite() {
    setState(() {
      if (_isFavorited) {
        _isFavorited = false;
        _favoriteCount-=1;
      } else {
        _isFavorited = true;
        _favoriteCount+=1;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Container(
          padding: EdgeInsets.all(0),
          child: IconButton(
            padding: EdgeInsets.all(0),
            alignment: Alignment.centerRight,
            icon: (_isFavorited ? Icon(Icons.star) : Icon(Icons.star_border)),
            color: Colors.red[500],
            onPressed: _toggleFavorite,
          ),
        ),
        SizedBox(
          width: 18,
          child: Text('$_favoriteCount'),
        ),
      ],
    );
  }
}

```

效果如下：

![009](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/009.png)



