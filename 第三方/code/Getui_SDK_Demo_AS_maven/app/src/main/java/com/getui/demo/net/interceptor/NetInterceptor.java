package com.getui.demo.net.interceptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.getui.demo.DemoApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class NetInterceptor implements Interceptor {

    private static final String TAG = "NetInterceptor~";

    /**
     * 判断网络是否连接.
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) DemoApplication.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo ni : info) {
                if (ni.getState() == NetworkInfo.State.CONNECTED) {
                    Log.d(TAG, "type = " + (ni.getType() == 0 ? "mobile" : ((ni.getType() == 1) ? "wifi" : "none")));
                    return true;
                }
            }

        }

        return false;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!isNetworkConnected()) {
            throw new NoNetException();
        }

        Request request = chain.request();
        return chain.proceed(request);
    }

    public static class NoNetException extends IOException {
        public NoNetException() {
            super("请检查网络连接");
        }
    }
}
