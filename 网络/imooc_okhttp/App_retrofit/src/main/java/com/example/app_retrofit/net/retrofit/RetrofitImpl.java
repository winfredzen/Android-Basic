package com.example.app_retrofit.net.retrofit;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitImpl {

    private static RetrofitImpl sInstance = new RetrofitImpl();

    public RetrofitImpl getInstance() {
        return sInstance;
    }

    private Retrofit mRetrofit;

    public static Retrofit getRetrofit() {
        return sInstance.mRetrofit;
    }

    private RetrofitImpl() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("WZ", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.imooc.com/api/okhttp/")
                .build();

    }

}
