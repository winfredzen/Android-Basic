# 动画

在Android动画中，有2中类型的动画：

+ View Animation 视图动画
  + Tween Animation 补间动画
  + Frame Animation 逐帧动画
+ Property Animation 属性动画
  + ValueAnimation
  + ObjectAnimation

## 视图动画

视图动画标签

+ alpha
+ scale
+ translate
+ rotate
+ set - 动画集



在res目录下创建`anim`文件夹，创建xml文件，通过`R.anim.xxx`来引用这个文件

如下的`scale`动画，`scaleanim.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.0"
    android:toXScale="2.0"
    android:fromYScale="0.0"
    android:toYScale="2.0"
    android:duration="700">
</scale>
```

点击按钮开始动画

```java
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.textView);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scaleanim);
                textView.startAnimation(animation);
            }
        });
```

动画效果如下：

![001](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/001.gif)





























