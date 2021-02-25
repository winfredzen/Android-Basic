package com.example.imooc_okhttp.net;

public interface INetCallBack {
    void onSuccess(String response);
    void onFailed(Throwable ex);
}
