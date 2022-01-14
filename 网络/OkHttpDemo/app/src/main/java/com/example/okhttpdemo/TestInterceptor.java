package com.example.okhttpdemo;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * create by wangzhen 2022/1/14
 */
public class TestInterceptor implements Interceptor {
    private static final String TAG = "TestInterceptor";
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Log.d(TAG, "before chain.proceed");
        Response response = chain.proceed(request);
        Log.d(TAG, "after chain.proceed");
        return response;
    }
}
