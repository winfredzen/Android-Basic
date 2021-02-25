package com.example.imooc_okhttp.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originReqeust  = chain.request();
        Request newRequest = originReqeust.newBuilder()
                .addHeader("author", "wz")
                .build();

        return chain.proceed(newRequest);
    }

}
