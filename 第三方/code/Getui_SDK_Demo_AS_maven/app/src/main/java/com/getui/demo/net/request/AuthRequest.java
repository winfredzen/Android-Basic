package com.getui.demo.net.request;

/**
 * Time：2020-03-09 on 16:34.
 * Decription:.
 * Author:jimlee.
 */
public class AuthRequest {

    private String sign;
    private String timestamp;
    private String appkey;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
}
