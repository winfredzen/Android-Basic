package com.example.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.ipcdemo.entity.Message;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * create by wangzhen 2021/8/31
 */
public class RemoteService extends Service {
    private static final String TAG = "RemoteService";

    private boolean isConnected = false;

    //存储消息监听
//    private ArrayList<MessageReceiveListener> mMessageReceiveListenerArrayList = new ArrayList<>();
    private RemoteCallbackList<MessageReceiveListener> mMessageRemoteCallbackList = new RemoteCallbackList<>();

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;

    private ScheduledFuture mScheduledFuture;

    private IConnectionService connectionService = new IConnectionService.Stub() {
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

                //每个5s收到一条消息
                mScheduledFuture = mScheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
//                        for (MessageReceiveListener messageReceiveListener : mMessageReceiveListenerArrayList) {
//                            try {
//                                Message message = new Message();
//                                message.setContent("this message from remote");
//                                messageReceiveListener.onReceiveMessage(message);
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }

                        int size = mMessageRemoteCallbackList.beginBroadcast();
                        for (int i = 0; i < size; i++) {
                            try {
                                Message message = new Message();
                                message.setContent("this message from remote");
                                mMessageRemoteCallbackList.getBroadcastItem(i).onReceiveMessage(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        mMessageRemoteCallbackList.finishBroadcast();
                    }
                }, 5000, 5000, TimeUnit.MILLISECONDS);

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
            //停止定时任务
            mScheduledFuture.cancel(true);
        }

        @Override
        public boolean isConnected() throws RemoteException {
            Log.d(TAG, "thread name = " + Thread.currentThread().getName());
            //在主线程中调用
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "isConnected = " + isConnected, Toast.LENGTH_SHORT).show();
                }
            });
            return isConnected;
        }
    };

    // 消息服务
    private IMessageService messageService = new IMessageService.Stub() {
        @Override
        public void sendMessage(Message message) throws RemoteException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "sendMessage message = " + message.getContent());
                }
            });
            if (isConnected) {
                message.setSendSuccess(true);
            } else {
                message.setSendSuccess(false);
            }
        }

        @Override
        public void registerMessageReceiveListener(MessageReceiveListener messageReceiveListener) throws RemoteException {
//            if (messageReceiveListener != null) {
//                mMessageReceiveListenerArrayList.add(messageReceiveListener);
//            }

            if (messageReceiveListener != null) {
                mMessageRemoteCallbackList.register(messageReceiveListener);
            }
        }

        @Override
        public void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiveListener) throws RemoteException {
            //这种方式取消无效，传入不是同一个对象
//            if (messageReceiveListener != null) {
//                mMessageReceiveListenerArrayList.remove(messageReceiveListener);
//            }

            if (messageReceiveListener != null) {
                mMessageRemoteCallbackList.unregister(messageReceiveListener);
            }
        }
    };

    private IServiceManager mServiceManager = new IServiceManager.Stub() {
        @Override
        public IBinder getService(String serviceName) throws RemoteException {
            if (IConnectionService.class.getSimpleName().equals(serviceName)) {
                return connectionService.asBinder();
            } else if (IMessageService.class.getSimpleName().equals(serviceName)) {
                return messageService.asBinder();
            } else {
                return null;
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServiceManager.asBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化
        mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }
}
