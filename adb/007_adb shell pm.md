# adb shell pm

1.获取所有的package

```shell
list packages [-f] [-d] [-e] [-s] [-3] [-i] [-l] [-u] [-U] [--uid UID] [--user USER_ID] [FILTER]

Prints all packages; optionally only those whose name contains
the text in FILTER.
Options:
  -f: see their associated file
  -d: filter to only show disabled packages
  -e: filter to only show enabled packages
  -s: filter to only show system packages
  -3: filter to only show third party packages
  -i: see the installer for the packages
  -l: ignored (used for compatibility with older releases)
  -U: also show the package UID
  -u: also include uninstalled packages
  --uid UID: filter to only show packages with the given UID
  --user USER_ID: only list packages belonging to the given user
```

如：

```shell
adb shell pm list packages
```

获取到对应的package后，通过如下命令获取启动的Activity

```java
adb shell [enter]
dumpsys package | grep -Eo "^[[:space:]]+[0-9a-f]+[[:space:]]+com.symbol.wfc.voice/[^[:space:]]+" | grep -oE "[^[:space:]]+$"
```

如:

```shell
adb shell dumpsys package | grep -Eo "^[[:space:]]+[0-9a-f]+[[:space:]]+com.android.car.settings/[^[:space:]]+" | grep -oE "[^[:space:]]+$"
```

![011](https://github.com/winfredzen/Android-Basic/blob/master/adb/images/011.png)











