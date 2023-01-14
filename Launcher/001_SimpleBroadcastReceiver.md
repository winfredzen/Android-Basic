# SimpleBroadcastReceiver

在 `LauncherAppState`中创建`SimpleBroadcastReceiver`

```java
SimpleBroadcastReceiver modelChangeReceiver =
        new SimpleBroadcastReceiver(mModel::onBroadcastIntent);
```

+ `mModel`指的是`LauncherModel`

可以理解为收到广播后，由`LauncherModel`的`onBroadcastIntent`方法处理广播



`SimpleBroadcastReceiver`中注册了很多的广播：

```java
modelChangeReceiver.register(mContext, Intent.ACTION_LOCALE_CHANGED,
        Intent.ACTION_MANAGED_PROFILE_AVAILABLE,
        Intent.ACTION_MANAGED_PROFILE_UNAVAILABLE,
        Intent.ACTION_MANAGED_PROFILE_UNLOCKED,
        ACTION_DEVICE_POLICY_RESOURCE_UPDATED);
```



## 广播说明

1.[ACTION_LOCALE_CHANGED](https://developer.android.com/reference/android/content/Intent#ACTION_LOCALE_CHANGED)

> Broadcast Action: The receiver's effective locale has changed. This happens when the device locale, or the receiving app's locale (set via LocaleManager.setApplicationLocales(LocaleList)) changed. Can be received by manifest-declared receivers.

语种变化？



2.[ACTION_MANAGED_PROFILE_AVAILABLE](https://developer.android.com/reference/android/content/Intent#ACTION_MANAGED_PROFILE_AVAILABLE)

> Broadcast sent to the primary user when an associated managed profile has become available. Currently this includes when the user disables quiet mode for the profile. Carries an extra EXTRA_USER that specifies the UserHandle of the profile. When quiet mode is changed, this broadcast will carry a boolean extra EXTRA_QUIET_MODE indicating the new state of quiet mode. This is only sent to registered receivers, not manifest receivers.
>
> 谷歌翻译如下：
>
> 当关联的托管配置文件可用时，向主要用户发送广播。 目前这包括用户为配置文件禁用静音模式。 携带一个额外的 EXTRA_USER，指定配置文件的 UserHandle。 当安静模式改变时，这个广播将携带一个额外的布尔值 EXTRA_QUIET_MODE 指示安静模式的新状态。 这仅发送给已注册的接收者，而不是清单接收者。



3.[ACTION_MANAGED_PROFILE_UNAVAILABLE](https://developer.android.com/reference/android/content/Intent#ACTION_MANAGED_PROFILE_UNAVAILABLE)

> Broadcast sent to the primary user when an associated managed profile has become unavailable. Currently this includes when the user enables quiet mode for the profile. Carries an extra EXTRA_USER that specifies the UserHandle of the profile. When quiet mode is changed, this broadcast will carry a boolean extra EXTRA_QUIET_MODE indicating the new state of quiet mode. This is only sent to registered receivers, not manifest receivers.
>
> 谷歌翻译如下：
>
> 当关联的托管配置文件变得不可用时，向主要用户发送广播。 目前这包括用户何时为配置文件启用安静模式。 携带一个额外的 EXTRA_USER，指定配置文件的 UserHandle。 当安静模式改变时，这个广播将携带一个额外的布尔值 EXTRA_QUIET_MODE 指示安静模式的新状态。 这仅发送给已注册的接收者，而不是清单接收者。



4.[ACTION_MANAGED_PROFILE_UNLOCKED](https://developer.android.com/reference/android/content/Intent#ACTION_MANAGED_PROFILE_UNLOCKED)

> Broadcast sent to the primary user when the credential-encrypted private storage for an associated managed profile is unlocked. Carries an extra `EXTRA_USER` that specifies the `UserHandle` of the profile that was unlocked. Only applications (for example Launchers) that need to display merged content across both primary and managed profiles need to worry about this broadcast. This is only sent to registered receivers, not manifest receivers.
>
> 谷歌翻译如下：
>
> 当关联的托管配置文件的凭据加密私有存储解锁时，向主要用户发送广播。 带有一个额外的 EXTRA_USER，它指定已解锁的配置文件的 UserHandle。 只有需要在主要配置文件和托管配置文件中显示合并内容的应用程序（例如启动器）才需要担心此广播。 这仅发送给已注册的接收者，而不是清单接收者。



5.[ACTION_DEVICE_POLICY_RESOURCE_UPDATED](https://developer.android.com/reference/android/app/admin/DevicePolicyManager#ACTION_DEVICE_POLICY_RESOURCE_UPDATED)

> Broadcast action: notify system apps (e.g. settings, SysUI, etc) that the device management resources with IDs EXTRA_RESOURCE_IDS has been updated, the updated resources can be retrieved using `DevicePolicyResourcesManager#getDrawable` and `DevicePolicyResourcesManager#getString`.
>
> This broadcast is sent to registered receivers only.
>
> EXTRA_RESOURCE_TYPE will be included to identify the type of resource being updated.
>
> 谷歌翻译如下：
>
> 广播动作：通知系统应用程序（例如设置、SysUI 等）ID 为 `EXTRA_RESOURCE_IDS` 的设备管理资源已更新，可以使用 `DevicePolicyResourcesManager#getDrawable` 和 `DevicePolicyResourcesManager#getString` 检索更新的资源。



## 源码

### SimpleBroadcastReceiver

```java
public class SimpleBroadcastReceiver extends BroadcastReceiver {

    private final Consumer<Intent> mIntentConsumer;

    public SimpleBroadcastReceiver(Consumer<Intent> intentConsumer) {
        mIntentConsumer = intentConsumer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mIntentConsumer.accept(intent);
    }

    /**
     * Helper method to register multiple actions
     */
    public void register(Context context, String... actions) {
        register(context, 0, actions);
    }

    /**
     * Helper method to register multiple actions with one or more {@code flags}.
     */
    public void register(Context context, int flags, String... actions) {
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }
        context.registerReceiver(this, filter, flags);
    }
}
```







