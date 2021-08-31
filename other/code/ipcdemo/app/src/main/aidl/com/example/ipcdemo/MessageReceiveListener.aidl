// MessageReceiveListener.aidl
package com.example.ipcdemo;
import com.example.ipcdemo.entity.Message;

// Declare any non-default types here with import statements

interface MessageReceiveListener {
    void onReceiveMessage(in Message message);
}