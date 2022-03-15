package com.xingkong.myapplication.jni;

public class JNIBasicType {
    static {
        System.loadLibrary("native-lib");
    }

    public native int callNativeInt(int num);

    private native byte callNativeByte(byte b);

    private native char callNativeChar(char ch);

    private native short callNativeShort(short sh);

    private native long callNativeLong(long l);

    private native float callNativeFloat(float f);

    private native double callNativeDouble(double d);

    private native boolean callNativeBoolean(boolean value);
}
