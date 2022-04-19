package com.wz.triangle_jni;

/**
 * create by wangzhen 2022/4/18
 */
public class NativeLibrary {
    static {
        System.loadLibrary("triangle_lib");
    }

    public static native void init(int width, int height);
    public static native void step();

}
