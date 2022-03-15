package com.wz.myapplication.jni;

public class JNIString {
    static {
        System.loadLibrary("dynamic-lib");
    }

    public native String callNativeString(String str);

    public native void StringMethod(String str);

    public native String reverseString(String str);

    public native String getHalfString(String str);



}
