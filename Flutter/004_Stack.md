# Stack

参考：

+ [Stack class](https://api.flutter.dev/flutter/widgets/Stack-class.html)
+ [4.5 层叠布局 Stack、Positioned](https://book.flutterchina.club/chapter4/stack.html)
+ [Positioned class](https://api.flutter.dev/flutter/widgets/Positioned-class.html)

这个控件，在层叠显示多个child时很有用

Stack中的child，可分为*positioned*或者*non-positioned*

+ Positioned - 位于`Positioned`组件中
+ Stack会调整自己的size，来包含所有的non-positioned的child。根据`alignment`来定位（在left-to-right环境中默认为左上角）

> The stack paints its children in order with the first child being at the bottom. If you want to change the order in which the children paint, you can rebuild the stack with the children in the new order. If you reorder the children in this way, consider giving the children non-null keys. These keys will cause the framework to move the underlying objects for the children to their new locations rather than recreate them at their new location.
>
> stack根据它children的顺序来绘制，第一个child位于底部。如果先改变child的顺序，要重新构建stack。如果要这样做，考虑给children非空的key

其构造方法：

```dart
Stack({Key key, AlignmentGeometry alignment: AlignmentDirectional.topStart, TextDirection textDirection, StackFit fit: StackFit.loose, Overflow overflow: Overflow.clip, List<Widget> children: const [] })
```

+ alignment - 此参数决定如何去对齐没有定位（没有使用`Positioned`）或部分定位的子组件
+ fit - 此参数用于确定**没有定位**的子组件如何去适应`Stack`的大小，`StackFit.loose`表示使用子组件的大小，`StackFit.expand`表示扩伸到`Stack`的大小
+ overflow - 此属性决定如何显示超出`Stack`显示空间的子组件；值为`Overflow.clip`时，超出部分会被剪裁（隐藏），值为`Overflow.visible` 时则不会



## Positioned

Positioned控制Stack的child的位置。Positioned必须是Stack的后代

如下的例子，Avatar下面有个➕

```dart
      body: Stack(
        children: <Widget>[
          CircleAvatar(
            radius: 30,
            backgroundImage: NetworkImage("https://s3.amazonaws.com/wll-community-production/images/no-avatar.png"),
          ),
          Positioned(
            bottom: 0.0,
            right: 1.0,
            child: Container(
              height: 20,
              width: 20,
              child: Icon(
                Icons.add,
                color: Colors.white,
                size: 15,
              ),
              decoration: BoxDecoration(
                color: Colors.green,
                shape: BoxShape.circle,
              ),
            ),
          )
        ],
      ),
```

![005](https://github.com/winfredzen/Android-Basic/blob/master/Flutter/images/005.png)

























