package com.xingkong.myapplication.jni;

import com.xingkong.myapplication.thread.ICallbackMethod;
import com.xingkong.myapplication.thread.IThreadCallback;

public class JNIInvokeMethod extends BaseOperation {
    @Override
    public void invoke() {

    }

    public native void NativeCallback(ICallbackMethod mICallbackMethod );
    public native void NativeThreadCallback(IThreadCallback mIThreadCallback);
}
