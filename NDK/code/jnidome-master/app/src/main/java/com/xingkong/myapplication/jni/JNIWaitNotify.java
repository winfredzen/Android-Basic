package com.xingkong.myapplication.jni;

public class JNIWaitNotify extends BaseOperation {

    @Override
    public void invoke() {

    }

    public native void  waitNativeThread();

    public native void  notifyNativeThread();



}
