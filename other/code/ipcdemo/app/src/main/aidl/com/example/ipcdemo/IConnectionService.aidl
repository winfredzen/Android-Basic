// IConnectionService.aidl
package com.example.ipcdemo;

// Declare any non-default types here with import statements
// 连接服务
interface IConnectionService {
    oneway void connect();

    void disconnect();

    boolean isConnected();
}