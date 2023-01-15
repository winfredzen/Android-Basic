# LoaderTask

`LoaderTask`类注释如下：

```xml
/**
 * Runnable for the thread that loads the contents of the launcher:
 *   - workspace icons
 *   - widgets
 *   - all apps icons
 *   - deep shortcuts within apps
 */
```

表示其用来加载Launcher中的内容：

+ 桌面icon
+ 小部件
+ 所有app icon
+ deep shortcuts within apps



**Launcher是如何调用到LoaderTask的？**

![001](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/001.png)



## run方法

`LoaderTask`是个`Runnable`接口的实现，其`run`方法大致如下：

```java
    public void run() {
        synchronized (this) {
            // Skip fast if we are already stopped.
            if (mStopped) {
                return;
            }
        }

        Object traceToken = TraceHelper.INSTANCE.beginSection(TAG);
        TimingLogger logger = new TimingLogger(TAG, "run");
        LoaderMemoryLogger memoryLogger = new LoaderMemoryLogger();
        try (LauncherModel.LoaderTransaction transaction = mApp.getModel().beginLoader(this)) {
            List<ShortcutInfo> allShortcuts = new ArrayList<>();
            Trace.beginSection("LoadWorkspace");
            try {
                loadWorkspace(allShortcuts, memoryLogger); //加载Workspace
            } finally {
                Trace.endSection();
            }
            logASplit(logger, "loadWorkspace");

            // Sanitize data re-syncs widgets/shortcuts based on the workspace loaded from db.
            // sanitizeData should not be invoked if the workspace is loaded from a db different
            // from the main db as defined in the invariant device profile.
            // (e.g. both grid preview and minimal device mode uses a different db)
            if (mApp.getInvariantDeviceProfile().dbFile.equals(mDbName)) {
                verifyNotStopped();
                sanitizeData();
                logASplit(logger, "sanitizeData");
            }

            verifyNotStopped();
            mResults.bindWorkspace(true /* incrementBindId */);
            logASplit(logger, "bindWorkspace");

            mModelDelegate.workspaceLoadComplete();
            // Notify the installer packages of packages with active installs on the first screen.
            sendFirstScreenActiveInstallsBroadcast();
            logASplit(logger, "sendFirstScreenActiveInstallsBroadcast");

            // Take a break
            waitForIdle();
            logASplit(logger, "step 1 complete");
            verifyNotStopped();

            // second step
            Trace.beginSection("LoadAllApps");
            List<LauncherActivityInfo> allActivityList;
            try {
               allActivityList = loadAllApps(); //加载AllApps
            } finally {
                Trace.endSection();
            }
            logASplit(logger, "loadAllApps");

            verifyNotStopped();
            mResults.bindAllApps(); //回调到Launcher.java
            logASplit(logger, "bindAllApps");

            verifyNotStopped();
            IconCacheUpdateHandler updateHandler = mIconCache.getUpdateHandler();
            setIgnorePackages(updateHandler);
            updateHandler.updateIcons(allActivityList,
                    LauncherActivityCachingLogic.newInstance(mApp.getContext()),
                    mApp.getModel()::onPackageIconsUpdated);
            logASplit(logger, "update icon cache");

            if (FeatureFlags.ENABLE_DEEP_SHORTCUT_ICON_CACHE.get()) {
                verifyNotStopped();
                logASplit(logger, "save shortcuts in icon cache");
                updateHandler.updateIcons(allShortcuts, new ShortcutCachingLogic(),
                        mApp.getModel()::onPackageIconsUpdated);
            }

            // Take a break
            waitForIdle();
            logASplit(logger, "step 2 complete");
            verifyNotStopped();

            // third step
            List<ShortcutInfo> allDeepShortcuts = loadDeepShortcuts();
            logASplit(logger, "loadDeepShortcuts");

            verifyNotStopped();
            mResults.bindDeepShortcuts();
            logASplit(logger, "bindDeepShortcuts");

            if (FeatureFlags.ENABLE_DEEP_SHORTCUT_ICON_CACHE.get()) {
                verifyNotStopped();
                logASplit(logger, "save deep shortcuts in icon cache");
                updateHandler.updateIcons(allDeepShortcuts,
                        new ShortcutCachingLogic(), (pkgs, user) -> { });
            }

            // Take a break
            waitForIdle();
            logASplit(logger, "step 3 complete");
            verifyNotStopped();

            // fourth step
            List<ComponentWithLabelAndIcon> allWidgetsList =
                    mBgDataModel.widgetsModel.update(mApp, null);
            logASplit(logger, "load widgets");

            verifyNotStopped();
            mResults.bindWidgets();
            logASplit(logger, "bindWidgets");
            verifyNotStopped();

            updateHandler.updateIcons(allWidgetsList,
                    new ComponentWithIconCachingLogic(mApp.getContext(), true),
                    mApp.getModel()::onWidgetLabelsUpdated);
            logASplit(logger, "save widgets in icon cache");

            // fifth step
            if (FeatureFlags.FOLDER_NAME_SUGGEST.get()) {
                loadFolderNames();
            }

            verifyNotStopped();
            updateHandler.finish();
            logASplit(logger, "finish icon update");

            mModelDelegate.modelLoadComplete();
            transaction.commit();
            memoryLogger.clearLogs();
        } catch (CancellationException e) {
            // Loader stopped, ignore
            logASplit(logger, "Cancelled");
        } catch (Exception e) {
            memoryLogger.printLogs();
            throw e;
        } finally {
            logger.dumpToLog();
        }
        TraceHelper.INSTANCE.endSection(traceToken);
    }
```

从源码的注释可以看出，大致分为了5步

其调用的方法：

+ `loadWorkspace`
+ `loadAllApps`



**数据如何回调到Launcher?**

通过`LoaderResults`回调，`LoaderResults`继承自`BaseLoaderResults`

调用步骤大致可以表示如下：

![002](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/002.png)















