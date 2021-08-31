// IMessageService.aidl
package com.example.ipcdemo;
import com.example.ipcdemo.entity.Message;

// Declare any non-default types here with import statements

// 消息的服务
interface IMessageService {
    void sendMessage(Message message);
}