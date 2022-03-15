package com.wz.myapplication.jni;

import com.wz.myapplication.utils.LogUtil;

public abstract class BaseOperation {

    static {
        System.loadLibrary("dynamic-lib");
    }

    public abstract void invoke();

    public void print(Object... args) {
        if (args.length == 0) {
            return;
        }

        for (Object arg : args) {
            LogUtil.Companion.d("Java value is " + arg.toString() + "\n");
        }
    }
}
