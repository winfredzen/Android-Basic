package com.wz.myapplication.load;

/**
 * create by wangzhen 2022/3/14
 */
public class JNIDynamicLoad {
    static {
        System.loadLibrary("dynamic-lib");
    }
    public native int sum(int x, int y);

    public native String getNativeString();
}
