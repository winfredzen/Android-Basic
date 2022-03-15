package com.xingkong.myapplication.jni;

import com.glumes.cppso.utils.LogUtil;

public abstract class BaseOperation {

    static {
        System.loadLibrary("native-lib");
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
