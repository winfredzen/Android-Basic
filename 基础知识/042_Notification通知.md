# Notification通知



## 在通知栏显示通知

对于`Build.VERSION.SDK_INT >= Build.VERSION_CODES.O`的系统，使用如下的形式：

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
    NotificationChannel notificationChannel = new NotificationChannel("MyNotificationId",
            "MyNotificationName", NotificationManager.IMPORTANCE_DEFAULT);
    NotificationManager manager = getSystemService(NotificationManager.class);
    manager.createNotificationChannel(notificationChannel);
}
Notification.Builder builder = new Notification.Builder(this, "MyNotificationId");
builder.setContentText("老王");
builder.setContentText("Notification Text");
builder.setSmallIcon(R.mipmap.ic_launcher_round);
builder.setAutoCancel(true);

NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
managerCompat.notify(1, builder.build());
```

效果如下：

![078](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/078.png)
