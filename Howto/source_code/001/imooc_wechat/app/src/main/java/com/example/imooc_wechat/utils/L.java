package com.example.imooc_wechat.utils;

import android.util.Log;

import com.example.imooc_wechat.BuildConfig;

public class L {

    private static final String TAG = "wangzhen";

    private static boolean sDebug = BuildConfig.DEBUG;

    public static void d(String msg, Object... args) {

        if (!sDebug) {
            return;
        }

        Log.d(TAG, String.format(msg, args));
    }

}
