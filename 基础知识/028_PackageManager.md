# PackageManager

参考：

+ [Android随笔之——PackageManager详解](https://www.cnblogs.com/travellife/p/3932823.html)

+ [APK安装流程详解2——PackageManager简介](https://www.jianshu.com/p/c56376916d5e)
+ [Android PackageManager 详解](https://www.cnblogs.com/a284628487/archive/2013/06/01/3111913.html)

PackageManager是用于获取Android系统中应用程序的信息，查询Application相关信息(application，activity，receiver，service，provider及相应属性等）、查询已安装应用、增加或删除permission、清除用户数据、缓存，代码段等

## 查询应用信息

使用PackageManager获取所有可启动主activity，可启动的activity都带有包含`MAIN`操作和`LAUNCHER`类型的intent过滤器

```xml
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
```

方法

```java
public abstract List<ResolveInfo> queryIntentActivities (Intent intent, 
                int flags)
```

获取匹配的的activity信息

+ intent 查询条件，Activity所配置的action和category
+ flags：MATCH_DEFAULT_ONLY:Category必须带有CATEGORY_DEFAULT的Activity;GET_INTENT_FILTERS:匹配Intent条件即可;GET_RESOLVED_FILTER:匹配Intent条件即可;(本质上是Activity)

如下，获取activity，并按activity标签首字母排序。使用的是隐式Intent获取目标activity

```java
        Intent startIntent = new Intent(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER); 

				PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startIntent, 0);

        Log.i(TAG, "Found " + activities.size() + " activities.");

        //排序
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(), b.loadLabel(pm).toString());
            }
        });
```

列出手机中的其它应用，获取Activity的名称，如下的绑定数据

```java
        public void bindActivity(ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();//名称
            mNameTextView.setText(appName);

            Drawable drawable = mResolveInfo.loadIcon(pm);//图标
            mImageView.setImageDrawable(drawable);
        }
```

![044](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/044.png)

## 启动目标Activity

启动activity，需创建启动activity的显示intent，需要从`ResolveInfo`对象中获取activity的包名与类名

```java
        /*点击监听*/
        @Override
        public void onClick(View v) {

            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            Intent intent = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);

            startActivity(intent);

        }
```

例如，此时点击“58同城”该应用，在overview screen会发现，并没用启动的新的应用，后台只有一个当前的应用`NerdLauncher`

![045](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/045.png)

这是为什么呢？













