// IMessageService.aidl
package com.example.ipcdemo;
import com.example.ipcdemo.entity.Message;
import com.example.ipcdemo.MessageReceiveListener;

// Declare any non-default types here with import statements

// 消息的服务
interface IMessageService {
    void sendMessage(inout Message message);

    void registerMessageReceiveListener(MessageReceiveListener messageReceiveListener);

    //取消注册
    void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiveListener);
}