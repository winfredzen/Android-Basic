package com.getui.demo.ui.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.getui.demo.BuildConfig;
import com.getui.demo.config.Config;
import com.getui.demo.net.RetrofitManager;
import com.getui.demo.net.interceptor.NetInterceptor;
import com.getui.demo.net.request.AuthRequest;
import com.getui.demo.net.response.AuthResp;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Time：2020-03-10 on 14:15.
 * Decription:.
 * Author:jimlee.
 */
public class AuthInteractor implements BaseInteractor {

    private final String TAG = this.getClass().getSimpleName();

    private CompositeDisposable disposables;

    public static volatile Boolean isTimeCorrect;

    private FutureTask futureTask;


    public AuthInteractor() {
        disposables = new CompositeDisposable();
    }

    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

    public void fetchAuthToken(final IAuthFinished authFinished) {
        if (TextUtils.isEmpty(Config.appkey) || TextUtils.isEmpty(BuildConfig.MASTERSECRET)) {
            Log.e(TAG, "appkey | mastersecret is empty, cancel fetchAuthtoken");
            return;
        }
        if (isTimeCorrect == null && futureTask != null && futureTask.isDone()) {
            checkTime(authFinished);
        }
        final AuthRequest authRequest = new AuthRequest();
        String currentTime = String.valueOf(System.currentTimeMillis());
        authRequest.setAppkey(Config.appkey);
        authRequest.setTimestamp(currentTime);
        authRequest.setSign(getSHA256(Config.appkey + currentTime + BuildConfig.MASTERSECRET));
        disposables.add(RetrofitManager
                .auth(authRequest)
                .subscribe(new Consumer<AuthResp>() {
                    @Override
                    public void accept(AuthResp authResp) {
                        Log.i(TAG, "isTimeCorrect = " + isTimeCorrect);
                        if (isTimeCorrect != null && !isTimeCorrect) {
                            authFinished.onAuthFailed("鉴权失败,请同步本地时间后重启应用");
                        } else if (authResp == null || TextUtils.isEmpty(authResp.result)) {
                            authFinished.onAuthFailed("鉴权失败,请检查签名参数及本地时间");

                        } else if (authResp.result.equals("ok")) {
                            authFinished.onAuthFinished(authResp.auth_token);
                        } else {
                            authFinished.onAuthFailed(authResp.result);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        if (throwable instanceof NetInterceptor.NoNetException) {
                            authFinished.onAuthFailed(throwable.getMessage());
                        } else {
                            if (isTimeCorrect != null && !isTimeCorrect) {
                                authFinished.onAuthFailed("鉴权失败,请同步本地时间后重启应用");
                            } else {
                                authFinished.onAuthFailed("鉴权失败,请检查签名参数及本地时间");
                            }
                        }
                    }
                }));
    }


    public interface IAuthFinished {
        void onAuthFinished(String token);

        void onAuthFailed(String msg);
    }


    public void checkTime(final IAuthFinished authFinished) {
        futureTask = new FutureTask(new TimeCheckRunnable(authFinished));
        Thread thread = new Thread(futureTask);
        thread.start();
    }


    private class TimeCheckRunnable implements Callable<Boolean> {


        IAuthFinished authFinished;

        public TimeCheckRunnable(IAuthFinished authFinished) {
            this.authFinished = authFinished;
        }

        @Override
        public Boolean call() throws Exception {
            URL url = null;//取得资源对象
            URLConnection uc = null;
            long timeInterval = 10 * 1000 * 60;
            try {
                url = new URL("http://www.baidu.com");
                uc = url.openConnection();//生成连接对象
                uc.connect(); //发出连接
                long ld = uc.getDate(); //取得网站日期时间
                isTimeCorrect = Math.abs(System.currentTimeMillis() - ld) < timeInterval;
                if (!isTimeCorrect) {
                    authFinished.onAuthFailed("请同步本地时间后重启应用");
                }
            } catch (Exception e) {
                e.printStackTrace();
                isTimeCorrect = false;
            }
            return true;
        }
    }

}
