package com.xingkong.myapplication.jni;

public class JNIReferenceType {
    static {
        System.loadLibrary("native-lib");
    }

    public native String callNativeStringArray(String[] strArray);

}
