// IServiceManager.aidl
package com.example.ipcdemo;

// Declare any non-default types here with import statements

interface IServiceManager {
    IBinder getService(String serviceName);
}