package com.getui.demo.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.getui.demo.BuildConfig;

/**
 * Time：2020-03-10 on 11:37.
 * Decription:.
 * Author:jimlee.
 */
public class Config {
    public static final String AUTH_ACTION = "com.action.auth";
    private static final String TAG = Config.class.getSimpleName();
    public static String appid = "";
    public static String appkey = BuildConfig.APPKEY;
    public static String appName = "";
    public static String packageName = "";
    public static String authToken;

    public static void init(Context context) {
        parseManifests(context);
    }

    /**
     * 解析Manifests文件，获取到个推的 appid
     *
     * @param context
     */
    private static void parseManifests(Context context) {
        packageName = context.getPackageName();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            appName = appInfo.loadLabel(context.getPackageManager()).toString();
            if (appInfo.metaData != null) {
                appid = appInfo.metaData.getString("GETUI_APPID");
            }
        } catch (Exception e) {
            Log.i(TAG, "parse manifest failed = " + e);
        }
    }


}
