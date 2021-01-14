# Region

构造方法

```java
Region(@NonNull Region region) //通过其他region来复制一个同样的region变量
Region(@NonNull Rect r)//创建一个矩形区域
Region(int left, int top, int right, int bottom)
```

如下的例子，绘制一个region：

```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        Region region = new Region(new Rect(50, 50, 200, 100));
        drawRegion(canvas, region, paint);

    }

    private void drawRegion(Canvas canvas, Region region, Paint paint) {
        RegionIterator iterator = new RegionIterator(region);
        Rect rect = new Rect();

        while (iterator.next(rect)) {
            canvas.drawRect(rect, paint);
        }
    }
```

![041](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/041.png)

由于Canvas中并没有来画`Region`的方法，所以是自己写的一个`drawRegion`来绘制region



## RegionIterator

对于特定的区域，可以使用多个矩形来表示其大致形状。

RegionIterator类就实现了获取组成区域的矩形集功能。

```java
RegionIterator(Region region) 
```

> 构造函数，根据区域构建对应的矩形集

```java
final boolean next(Rect r)
```

> 获取下一个矩形，将结果保存在参数`Rect r`中

由于Canvas中并没有来画`Region`的方法，想要绘制一个区域，就只有通过`RegionIterator`类构造矩形集来逼近显示区域



## 一些方法

```java
boolean setPath(@NonNull Path path, @NonNull Region clip)
```

+ path - 用来构造区域的路径
+ clip - 与前面的path所构成的路径取交集，并将该交集设置为最终的区域

```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        //椭圆曲线
        Path ovalPath = new Path();
        RectF rect = new RectF(50, 50, 200, 500);
        ovalPath.addOval(rect, Path.Direction.CCW);

        //传入一个比ovalPath小的矩形区域，让其取交集
        Region region = new Region();
        region.setPath(ovalPath, new Region(50, 50, 200, 200));

        //画出区域
        drawRegion(canvas, region, paint);

    }
```

![042](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/042.png)

![043](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/043.png)



## 区域相交

Region最重要的功能在区域的相交操作中



**1.`union(@NonNull Rect r)`**

用于与指定的矩形取并集

```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);

        Region region = new Region(10, 10, 200, 100);
        region.union(new Rect(10, 10, 50, 300));
        
        //画出区域
        drawRegion(canvas, region, paint);

    }
```

![044](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/044.png)



**2.区域操作**

```java
boolean op(@NonNull Rect r, @NonNull Op op)
boolean op(int left, int top, int right, int bottom, @NonNull Op op)
boolean op(@NonNull Region region, @NonNull Op op)  
```

> 用当前的Region对象与指定的一个Rect对象或者Region对象执行相交操作，并将结果赋给当前的Region对象。如果计算成功，则返回true，否则返回false
>
> `Op`参数有如下的6个：
>
> ```java
>     public enum Op {
>         DIFFERENCE(0),
>         INTERSECT(1),
>         UNION(2),
>         XOR(3),
>         REVERSE_DIFFERENCE(4),
>         REPLACE(5);
> 
>         Op(int nativeInt) {
>             this.nativeInt = nativeInt;
>         }
> 
>         /**
>          * @hide
>          */
>         @UnsupportedAppUsage
>         public final int nativeInt;
>     }
> ```
>
> ![045](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/045.png)



如下的例子，横、竖两个矩形分别用描边画出来，相交区域用颜色填充

```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect1 = new Rect(100, 100, 400, 200);
        Rect rect2 = new Rect(200, 0, 300, 300);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect1, paint);
        canvas.drawRect(rect2, paint);

        Region region1 = new Region(rect1);
        Region region2 = new Region(rect2);
        region1.op(region2, Region.Op.INTERSECT);//取两个区域的交集

        Paint paint_fill = new Paint();
        paint_fill.setColor(Color.GREEN);
        paint_fill.setStyle(Paint.Style.FILL);

        //画出区域
        drawRegion(canvas, region1, paint_fill);

    }
```

![046](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/046.png)







