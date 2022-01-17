package com.getui.demo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.getui.demo.config.Config;
import com.getui.demo.ui.activity.GetuiSdkDemoActivity;
import com.getui.demo.ui.presenter.AuthInteractor;
import com.igexin.sdk.IUserLoggerInterface;
import com.igexin.sdk.PushManager;

import java.lang.ref.WeakReference;

public class DemoApplication extends Application implements AuthInteractor.IAuthFinished {

    private static final String TAG = "GetuiSdkDemo";
    public static WeakReference<GetuiSdkDemoActivity> demoActivity;
    public static String isCIDOnLine = "";
    public static String cid = "";
    public static Context appContext;
    public static boolean isSignError = false;
    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();
    private static DemoHandler handler;

    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        Log.d(TAG, "DemoApplication onCreate");

        Config.init(this);
        if (handler == null) {
            handler = new DemoHandler();
        }
        startAuth();
        initSdk();
    }

    private void initSdk() {
        Log.d(TAG, "initializing sdk...");
        PushManager.getInstance().initialize(this);
        if (BuildConfig.DEBUG) {
            //切勿在 release 版本上开启调试日志
            PushManager.getInstance().setDebugLogger(this, new IUserLoggerInterface() {

                @Override
                public void log(String s) {
                    Log.d(TAG, "s = " + s);
                }
            });
        }
    }

    private void startAuth() {
        AuthInteractor interactor = new AuthInteractor();
        interactor.checkTime(this);
        interactor.fetchAuthToken(this);
    }

    @Override
    public void onAuthFinished(String token) {
        Config.authToken = token;
        Log.i(TAG, "鉴权成功,token = " + token);
    }

    @Override
    public void onAuthFailed(String msg) {
        if (msg.equals("sign_error")) {
            Log.i(TAG, "鉴权失败,请检查签名参数");
            isSignError = true;
        }
        Log.i(TAG, "onAuthFailed = " + msg);
    }


    public static class DemoHandler extends Handler {
        public static final int RECEIVE_MESSAGE_DATA = 0; //接收到消息
        public static final int RECEIVE_CLIENT_ID = 1; //cid通知
        public static final int RECEIVE_ONLINE_STATE = 2; //cid在线状态通知消息
        public static final int SHOW_TOAST = 3; //Toast消息


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_MESSAGE_DATA:  //接收到消息
                    payloadData.append((String) msg.obj);
                    payloadData.append("\n");
                    if (demoActivity != null && demoActivity.get() != null) {
                        if (demoActivity.get().homeFragment != null && demoActivity.get().homeFragment.tvLog != null) {
                            demoActivity.get().homeFragment.tvLog.append(msg.obj + "\n");
                        }
                    }
                    break;

                case RECEIVE_CLIENT_ID:  //cid通知
                    cid = (String) msg.obj;
                    if (demoActivity != null && demoActivity.get() != null) {
                        if (demoActivity.get().homeFragment != null && demoActivity.get().homeFragment.tvClientId != null) {
                            demoActivity.get().homeFragment.tvClientId.setText((String) msg.obj);
                        }
                    }
                    break;
                case RECEIVE_ONLINE_STATE:  //cid在线状态通知
                    isCIDOnLine = (String) msg.obj;
                    if (demoActivity != null && demoActivity.get() != null) {
                        if (demoActivity.get().homeFragment != null && demoActivity.get().homeFragment.tvCidState != null) {
                            demoActivity.get().homeFragment.tvCidState.setText(msg.obj.equals("true") ? demoActivity.get().getResources().getString(R.string.online) : demoActivity.get().getResources().getString(R.string.offline));
                        }
                    }
                    break;
                case SHOW_TOAST:  //需要弹出Toast
                    Toast.makeText(DemoApplication.appContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    }

}
