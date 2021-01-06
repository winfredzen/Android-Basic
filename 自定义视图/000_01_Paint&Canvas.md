# Paint&Canvas

1.`setStyle(Style style)` - 设置样式

> Style是一个枚举，包括的值有：
>
> + `FILL` - 填充
> + `STROKE` - 描边
> + `FILL_AND_STROKE` - 填充和描边

```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(0xFFFF0000);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(50);

        canvas.drawCircle(200, 200, 150, paint);
    }
```

分别设置的效果：

![007](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/007.png)

![008](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/008.png)

![009](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/009.png)

