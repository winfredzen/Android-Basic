#  AspectRatio

参考：

+ [AspectRatio class](https://api.flutter.dev/flutter/widgets/AspectRatio-class.html)

AspectRatio作用是设置子元素child的宽高比

+ AspectRatio首先会在布局限制条件允许的范围内尽可能地扩展，Widget的高度是由宽度和比率决定的
+ 如果在满足所有限制条件后无法找到可行的尺寸，AspectRatio最终将会优先适应布局限制条件，而忽略所设置的比率

构造方法

```dart
AspectRatio({Key key, @required double aspectRatio, Widget child })
```

如下的简单的例子：



