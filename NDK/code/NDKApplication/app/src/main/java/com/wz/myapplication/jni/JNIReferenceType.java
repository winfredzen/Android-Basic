package com.wz.myapplication.jni;

public class JNIReferenceType {
    static {
        System.loadLibrary("dynamic-lib");
    }

    public native String callNativeStringArray(String[] strArray);

}
