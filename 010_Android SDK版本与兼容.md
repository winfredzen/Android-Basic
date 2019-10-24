# Android SDK版本与兼容

在官网[http://developer.android.com/about/dashboards/index.html ](http://developer.android.com/about/dashboards/index.html )可以各个版本的信息

![017](https://github.com/winfredzen/Android-Basic/blob/master/images/017.png)

`build.gradle`文件中有如下的几个值

+ minSdkVersion - SDK 最低版本 ，以最低版本设置值为标准，操作系统会拒绝将应用安装在系统版本低于标准的设备上

+ targetSdkVersion - SDK 目标版本 

  > 目标版本的设定值告知Android:应用是为哪个API级别设计的。大多数情况下，目标版本即最新发布的Android版本 
  >
  > 什么时候需要降低SDK目标版本呢?新发布的SDK版本会改变应用在设备上的显示方式，甚至连操作系后台运行行为都会受影响。如果应用已开发完成，应确认它在新版本上能否如预期 那样正常运行。查看http://developer.android.com/reference/android/os/Build.VERSION_CODES.html 上的文档，检查可能出现问题的地方。根据分析结果，要么修改应用以适应新版本系统，要么降 低SDK目标版本。 
  >
  > 降低SDK目标版本可以保证的是，即便在高于目标版本的设备上，应用仍然可以正常运行，且 运行行为仍和目标版本保持一致。这是因为新发布版本中的变化已被忽略  

+ minSdkVersion - SDK 编译版本 

  > SDK最低版本和目标版本会通知给操作系统，而SDK编译版本只是你和编译器之间的私有信息 
  >
  > Android的特色功能是通过SDK中的类和方法展现的。在编译代码时，SDK编译版本(即编 译目标)指定具体要使用的系统版本。Android Studio在寻找类包导入语句中的类和方法时，编 译目标确定具体的基准系统版本 
  >
  > 编译目标的最佳选择为最新的API级别(当前级别为25，代号为Nougat)。当然，需要的话，也可以改变应用的编译目标。例如，Android新版本发布时，可能就需要更新编译目标，以便使用新版本引入的方法和类 
  >
  > 可以修改`build.gradle`文件中的SDK最低版本、目标版本以及编译版本。修改完毕，项目和Gradle更改重新同步后才能生效。选择`Tools → Android → Sync Project with Gradle Files`菜单项， 项目随即会重新编译 

  

**安全添加新版本 API 中的代码** 

![018](https://github.com/winfredzen/Android-Basic/blob/master/images/018.png)

可以使用如下的形式来做兼容

```java
                //动画
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
```

`Build.VERSION.SDK_INT`表示的是Android设备的版本号

