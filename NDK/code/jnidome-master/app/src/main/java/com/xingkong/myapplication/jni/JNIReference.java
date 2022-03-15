package com.xingkong.myapplication.jni;

public class JNIReference extends BaseOperation {


    @Override
    public void invoke() {
//        errorCacheLocalReference();
//        cacheWithGlocalReference();
//        useWeakGlocalReference();
        print( errorCacheLocalReference(), cacheWithGlocalReference(),useWeakGlocalReference());

    }

    public native String errorCacheLocalReference();
    public native String cacheWithGlocalReference();
    public native String useWeakGlocalReference();
}
