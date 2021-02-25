package com.example.imooc_okhttp.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.imooc_okhttp.net.INetCallBack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;

public class OkHttpUtils {

    private Handler mUiHandler = new Handler(Looper.getMainLooper());

    private OkHttpClient mOkHttpClient;

    private OkHttpUtils() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("WZ", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
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
    public void doGet(String url, INetCallBack callBack) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        executeRequest(callBack, request);
    }

    /**
     * http://www.imooc.com/api/okhttp/postmethod
     * @param url
     * @param params
     * @param callBack
     */
    public void doPost(String url, HashMap<String, String> params, INetCallBack callBack) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        if (params != null) {
            for (String param : params.keySet()) {
                formBodyBuilder.add(param, params.get(param));
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(formBodyBuilder.build())
                .build();


        executeRequest(callBack, request);
    }

    public void doPostMultiPart(String url, HashMap<String, String> params, INetCallBack callBack) {
        MultipartBody.Builder multiPartBodyBuilder = new MultipartBody.Builder();
        multiPartBodyBuilder.setType(MultipartBody.FORM);

        if (params != null) {
            for (String param : params.keySet()) {
                multiPartBodyBuilder.addFormDataPart(param, params.get(param));
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(multiPartBodyBuilder.build())
                .build();


        executeRequest(callBack, request);
    }

    public void doPostJson(String url, String jsonStr, INetCallBack callBack) {
        MediaType jsonMediaType = MediaType.get("application/json");
        RequestBody requestBody = RequestBody.create(jsonStr, jsonMediaType);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        executeRequest(callBack, request);
    }

    private void executeRequest(INetCallBack callBack, Request request) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e);
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String respStr = null;
                try {
                    respStr = response.body().string();
                } catch (IOException e) {
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(e);
                        }
                    });
                }
                String finalRespStr = respStr;
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(finalRespStr);
                    }
                });

            }
        });
    }

}






















