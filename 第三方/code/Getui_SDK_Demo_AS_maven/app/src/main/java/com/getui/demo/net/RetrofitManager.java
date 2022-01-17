package com.getui.demo.net;

import com.getui.demo.BuildConfig;
import com.getui.demo.config.Config;
import com.getui.demo.net.interceptor.NetInterceptor;
import com.getui.demo.net.request.AuthRequest;
import com.getui.demo.net.request.LinkNotificationRequest;
import com.getui.demo.net.request.TransmissionRequest;
import com.getui.demo.net.response.AuthResp;
import com.getui.demo.net.response.NotificationResp;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Time：2020-03-09 on 15:36.
 * Decription:.
 * Author:jimlee.
 */
public class RetrofitManager {

    public static final String BASE_URL = BuildConfig.SERVER_URL;

    private static NetApi netApi;

    static {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new NetInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        netApi = retrofit.create(NetApi.class);
    }

    public static Observable<NotificationResp> sendNotification(LinkNotificationRequest request) {
        return netApi.sendNotification(Config.authToken, Config.appid, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<NotificationResp> sendTransmission(TransmissionRequest request) {
        return netApi.sendTransmission(Config.authToken, Config.appid, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<AuthResp> auth(AuthRequest authRequest) {
        return netApi.auth(Config.appid, authRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
