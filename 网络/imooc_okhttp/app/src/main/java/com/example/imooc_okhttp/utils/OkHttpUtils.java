package com.example.imooc_okhttp.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtils {

    private OkHttpUtils() {

    }

    public static OkHttpUtils sInstance = new OkHttpUtils();

    public static OkHttpUtils getInstance() {
        return sInstance;
    }

    /**
     * http://www.imooc.com/api/okhttp/getmethod
     * @param url
     * @return
     */
    public String doGet(String url) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            Response response  = call.execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
