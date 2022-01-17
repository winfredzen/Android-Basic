package com.getui.demo.net.request;

/**
 * Time：2020-03-09 on 16:24.
 * Decription:.
 * Author:jimlee.
 */
public class LinkNotificationRequest {

    public Message message;
    public LinkTemplate link;
    public String cid;
    public String requestid;


    public static class Message {
        public String appkey;
        public boolean is_offline;
        public String msgtype;

    }

    public static class LinkTemplate {

        public Style style;
        public String url;
        public String duration_begin;
        public String duration_end;


    }

    public static class Style {

        public int type;
        public String text;
        public String title;
        public int big_style;
        public String big_text;
        public String logo;
        public String logourl;
        public boolean is_ring;
        public boolean is_vibrate;
        public boolean is_clearable;
        public String notify_id;
    }


}
