package com.example.app_retrofit.bean;

import java.util.HashMap;

public class Result {

    public int errorCode;
    public String errorMsg;
    public Data data;

    public static class Data {
        public String ip;
        public String params;
        public HashMap<String, String> headers;
        public String json;

    }

}
