package com.getui.demo.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;

import com.getui.demo.DemoApplication;
import com.getui.demo.config.Config;
import com.getui.demo.net.interceptor.NetInterceptor;
import com.getui.demo.ui.presenter.AuthInteractor;

import static android.content.Context.ALARM_SERVICE;

/**
 * Time：2020-03-11 on 09:47.
 * Decription:.
 * Author:jimlee.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();

    private AuthInteractor authInteractor = new AuthInteractor();
    private AlarmManager manager = (AlarmManager) DemoApplication.appContext.getSystemService(ALARM_SERVICE);
    public static final String IS_TRY_ONCEN = "is_try_once";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) ||
                intent.getAction().equals(Config.AUTH_ACTION)) {
            Log.i("AlarmReceiver", "receive action " + intent.getAction());
            if (NetInterceptor.isNetworkConnected() && TextUtils.isEmpty(Config.authToken)) {
                authInteractor.fetchAuthToken(new AuthInteractor.IAuthFinished() {

                    @Override
                    public void onAuthFinished(String token) {
                        Log.i(TAG, "fetch token successed token = " + token);
                        Config.authToken = token;
                    }

                    @Override
                    public void onAuthFailed(String msg) {
                        Log.i(TAG, "onAuthFailed = " + msg);
                        if (!intent.getBooleanExtra(IS_TRY_ONCEN, false)) {
                            Log.i(TAG, "try again in 10 seconds");
                            AlarmUtils.startAuthAlram(manager, true, 10);
                        }
                    }
                });
            }
        }
    }

}
