package com.example.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * create by wangzhen 2021/8/31
 */
public class RemoteService extends Service {
    private static final String TAG = "RemoteService";

    private boolean isConnected = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private IConnectionService.Stub connectionService = new IConnectionService.Stub() {
        @Override
        public void connect() throws RemoteException {
            try {
                Log.d(TAG, "connect()");
                Log.d(TAG, "thread name = " + Thread.currentThread().getName());
                Thread.sleep(5000);
                isConnected = true;
                //在主线程中调用
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RemoteService.this, "connect", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void disconnect() throws RemoteException {
            Log.d(TAG, "disconnect()");
            Log.d(TAG, "thread name = " + Thread.currentThread().getName());
            //在主线程中调用
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "disconnect", Toast.LENGTH_SHORT).show();
                }
            });
            isConnected = false;
        }

        @Override
        public boolean isConnected() throws RemoteException {
            Log.d(TAG, "thread name = " + Thread.currentThread().getName());
            //在主线程中调用
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "isConnected", Toast.LENGTH_SHORT).show();
                }
            });
            return isConnected;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return connectionService.asBinder();
    }
}
