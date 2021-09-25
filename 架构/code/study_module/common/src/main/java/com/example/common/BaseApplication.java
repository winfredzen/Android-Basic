package com.example.common;

import android.app.Application;
import android.util.Log;

import com.example.common.config.BaseConfig;

/**
 * create by wangzhen 2021/9/25
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(BaseConfig.TAG, "common/BaseApplication onCreate:");
    }
}
