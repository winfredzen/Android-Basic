package com.getui.demo.ui.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.getui.demo.BuildConfig;
import com.getui.demo.DemoApplication;
import com.getui.demo.config.Config;
import com.getui.demo.net.RetrofitManager;
import com.getui.demo.net.request.LinkNotificationRequest;
import com.getui.demo.net.request.TransmissionRequest;
import com.getui.demo.net.response.NotificationResp;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Time：2020-03-10 on 14:15.
 * Decription:.
 * Author:jimlee.
 */
public class HomeInteractor implements BaseInteractor {

    private final String TAG = this.getClass().getSimpleName();
    private CompositeDisposable disposables;
    private int payloadCount = 0;
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public HomeInteractor() {
        disposables = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        disposables.clear();
    }

    public void sendNotification(final NotificationListener listener) {
        if ( TextUtils.isEmpty(Config.appid) || TextUtils.isEmpty(BuildConfig.MASTERSECRET)) {
            listener.onSendNotificationFailed("应用参数不能为空，请检查!");
            return;
        }
        LinkNotificationRequest request = createLinkNotificationRequest();
        disposables.add(RetrofitManager.sendNotification(request).subscribe(new Consumer<NotificationResp>() {
            @Override
            public void accept(NotificationResp notificationResp) throws Exception {
                Log.i(TAG, "notification status = " + notificationResp.status);
                if (notificationResp.result.equals("ok")) {
                    listener.onSendNotificationSuccess("通知发送成功：cid" +
                            (notificationResp.status.equals("successed_online") ? "在线" : "离线"));
                } else {
                    listener.onSendNotificationSuccess(notificationResp.result);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "notification error = " + throwable.toString());
                listener.onSendNotificationFailed(throwable.getMessage());
            }
        }));
    }

    public void sendTransmission(final NotificationListener listener) {
        if ( TextUtils.isEmpty(Config.appid) || TextUtils.isEmpty(BuildConfig.MASTERSECRET)) {
            listener.onSendNotificationFailed("应用参数不能为空，请检查!");
            return;
        }
        TransmissionRequest request = createTransmission();
        disposables.add(RetrofitManager.sendTransmission(request).subscribe(new Consumer<NotificationResp>() {
            @Override
            public void accept(NotificationResp notificationResp) throws Exception {
                if (notificationResp.result.equals("ok")) {
                    listener.onSendNotificationSuccess("透传请求发送成功：当前cid " +
                            (notificationResp.status.equals("successed_online") ? "在线" : "离线"));
                } else {
                    listener.onSendNotificationSuccess(notificationResp.result);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                listener.onSendNotificationFailed(throwable.getMessage());
            }
        }));
    }

    private TransmissionRequest createTransmission() {
        LinkNotificationRequest.Message message = new LinkNotificationRequest.Message();
        message.appkey = Config.appkey;
        message.is_offline = false;
        message.msgtype = "transmission";

        TransmissionRequest.Transmission transmission = new TransmissionRequest.Transmission();
        transmission.transmission_type = false;

        transmission.transmission_content = String.format("收到一条透传内容，发送时间：%s - %d",
                sdf.format(new Date()), ++payloadCount);

        TransmissionRequest request = new TransmissionRequest();
        request.message = message;
        request.transmission = transmission;
        request.cid = DemoApplication.cid;
        request.requestid = createRequestId();

        return request;
    }

    private LinkNotificationRequest createLinkNotificationRequest() {
        LinkNotificationRequest.Message message = new LinkNotificationRequest.Message();
        message.appkey = Config.appkey;
        message.is_offline = false;
        message.msgtype = "link";

        LinkNotificationRequest.Style style = new LinkNotificationRequest.Style();
        style.type = 0;
        style.text = "这是一条点击跳转链接通知";
        style.title = "这是一条点击跳转链接通知";
        style.logo = "push.png";
        style.big_style = 2;
        style.big_text = "长文本内容";
        style.is_clearable = true;
        style.is_vibrate = true;
        style.is_ring = true;


        LinkNotificationRequest.LinkTemplate linkTemplate = new LinkNotificationRequest.LinkTemplate();
        linkTemplate.url = "http://www.getui.com/";
        linkTemplate.style = style;


        LinkNotificationRequest request = new LinkNotificationRequest();
        request.message = message;
        request.link = linkTemplate;
        request.cid = DemoApplication.cid;
        request.requestid = createRequestId();

        return request;
    }

    private String createRequestId() {
        long r = 0;
        r = (long) ((Math.random() + 1) * 1000);
        return System.currentTimeMillis() + String.valueOf(r).substring(1);
    }

    interface NotificationListener {
        void onSendNotificationSuccess(String msg);

        void onSendNotificationFailed(String msg);
    }


}
