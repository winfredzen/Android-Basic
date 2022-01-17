package com.getui.demo.net;


import com.getui.demo.net.request.AuthRequest;
import com.getui.demo.net.request.LinkNotificationRequest;
import com.getui.demo.net.request.TransmissionRequest;
import com.getui.demo.net.response.AuthResp;
import com.getui.demo.net.response.NotificationResp;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Time：2020-03-09 on 15:36.
 * Decription:.
 * Author:jimlee.
 */
public interface NetApi {

    @POST("v1/{appid}/push_single")
    Observable<NotificationResp> sendNotification(@Header("authtoken") String authtoken, @Path("appid") String appId, @Body LinkNotificationRequest notificationRequest);

    @POST("v1/{appid}/push_single")
    Observable<NotificationResp> sendTransmission(@Header("authtoken") String authtoken, @Path("appid") String appId, @Body TransmissionRequest transmissionRequest);


    @POST("v1/{appid}/auth_sign")
    Observable<AuthResp> auth(@Path("appid") String appId, @Body AuthRequest authRequest);


}
