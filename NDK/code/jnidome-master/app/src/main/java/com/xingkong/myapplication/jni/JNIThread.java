package com.xingkong.myapplication.jni;

import com.xingkong.myapplication.model.Animal;

public class JNIThread extends BaseOperation {

    @Override
    public void invoke() {

    }

    public native int createNativeThread();

    public native int createNativeThreadWithArgs();

    public native void joinNativeThread();


}
