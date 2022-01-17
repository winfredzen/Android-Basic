package com.getui.demo.net.request;

/**
 * Time：2020-03-10 on 13:45.
 * Decription:.
 * Author:jimlee.
 */
public class TransmissionRequest {

    public LinkNotificationRequest.Message message;
    public Transmission transmission;
    public String cid;
    public String requestid;

    public static class Transmission {
        public boolean transmission_type;
        public String transmission_content;//请填写透传内容
        public String duration_begin;
        public String duration_end;
    }
}
