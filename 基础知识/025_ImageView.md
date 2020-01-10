# ImageView

**1.通过图片的名称，来设置图片**

通过`int getIdentifier(String name, String defType, String defPackage)`方法获取resID

```java
Resources res = getResources();
String mDrawableName = "logo_default";
int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
```

通过`setImageResource(int resId)`来设置图片，或者通过`setImageDrawable(Drawable drawable)`来设置图片

```java
Drawable drawable = res.getDrawable(resID );
icon.setImageDrawable(drawable );
```

```kotlin
itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
```

