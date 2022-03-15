package com.xingkong.myapplication.jni;


@SuppressWarnings("ALL")
public class JNIDynamicLoad {
    static {
        System.loadLibrary("native-lib");
    }

    public native int sum(int x,int y);
    public native String getNativeString();
}
