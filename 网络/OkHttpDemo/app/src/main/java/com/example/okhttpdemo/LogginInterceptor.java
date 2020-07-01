package com.example.okhttpdemo;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogginInterceptor implements Interceptor {

    private static final String TAG = "LogginInterceptor";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();

        long t1 = System.nanoTime();//纳秒
        Log.d(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        /**
         * 1e6d is 1 * 10^6 represented as a double
         */

        long t2 = System.nanoTime();
        Log.d(TAG ,String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }

}
