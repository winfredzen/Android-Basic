# AlarmManager

[AlarmManager](https://developer.android.com/reference/android/app/AlarmManager)提供对系统Alarm服务的访问。允许应用程序在将来的某个时间点运行。

参考：

+ [设置重复闹铃时间](https://developer.android.google.cn/training/scheduling/alarms?hl=zh-cn)



>  **闹钟类型**
>
> + 经过的时间 - 使用“自系统启动以来的时间”作为参考
> + “实时时钟”(RTC) - 使用世界协调时间 (UTC)（挂钟时间）

> 以下为类型列表：
>
> - `ELAPSED_REALTIME` - 基于自设备启动以来所经过的时间触发待定 intent，但不会唤醒设备。经过的时间包括设备处于休眠状态期间的任何时间。
> - `ELAPSED_REALTIME_WAKEUP` - 唤醒设备，并在自设备启动以来特定时间过去之后触发待定 Intent。
> - `RTC` - 在指定的时间触发待定 Intent，但不会唤醒设备。
> - `RTC_WAKEUP` - 唤醒设备以在指定的时间触发待定 Intent。



如：

```java
am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5*1000, pi);
```

```java
am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5*1000, pi);
```



## 例子

参考：

+ [AlarmManager in Android Studio || Notification using AlarmManager is Android Studio](https://www.youtube.com/watch?v=xSrVWFCtgaE) - 代码地址[AlarmManagement](https://github.com/foxandroid/AlarmManagement)



1.设置Alarm

```java
alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
Intent intent = new Intent(this, AlarmReceiver.class);
pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000, pendingIntent);
Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();
```

2.取消Alarm

```java
Intent intent = new Intent(this, AlarmReceiver.class);
pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
if (alarmManager == null) {
    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
}
alarmManager.cancel(pendingIntent);
Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
```



3.`AlarmReceiver`源码

```java
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive......");

        Intent i = new Intent(context, DestinationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "foxandroid")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Foxandroid Alarm Manager")
                .setContentText("Subscribe for android related content")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());
    }
}
```

效果如下：

![006](https://github.com/winfredzen/Android-Basic/blob/master/Android%20Background%20Process/images/006.png)

































