package com.example.ipcdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ipcdemo.entity.Message;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Button mConnectBtn;
    private Button mDisconnectBtn;
    private Button mIsConnectedBtn;
    private Button mSendMessageBtn;
    private Button mRegisterBtn;
    private Button mUnRegisterBtn;


    private IConnectionService connectionServiceProxy;
    private IMessageService messageServiceProxy;
    private IServiceManager serviceManagerProxy;

    //相当于remote对主进程的调用
    private MessageReceiveListener mMessageReceiveListener = new MessageReceiveListener.Stub() {
        @Override
        public void onReceiveMessage(Message message) throws RemoteException {
            //注意要切换都主线程
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, message.getContent(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnectBtn = (Button) findViewById(R.id.btn_connect);
        mDisconnectBtn = (Button) findViewById(R.id.btn_disconnect);
        mIsConnectedBtn = (Button) findViewById(R.id.btn_is_connected);
        mSendMessageBtn = (Button) findViewById(R.id.btn_send_message);
        mRegisterBtn = (Button) findViewById(R.id.btn_register);
        mUnRegisterBtn = (Button) findViewById(R.id.btn_unregister);

        mConnectBtn.setOnClickListener(this);
        mDisconnectBtn.setOnClickListener(this);
        mIsConnectedBtn.setOnClickListener(this);
        mSendMessageBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mUnRegisterBtn.setOnClickListener(this);

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                serviceManagerProxy = IServiceManager.Stub.asInterface(service);

                try {
                    connectionServiceProxy = IConnectionService.Stub.asInterface(serviceManagerProxy.getService(IConnectionService.class.getSimpleName()));
                    messageServiceProxy = IMessageService.Stub.asInterface(serviceManagerProxy.getService(IMessageService.class.getSimpleName()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                };
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                try {
                    connectionServiceProxy.connect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_disconnect:
                try {
                    connectionServiceProxy.disconnect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_is_connected:
                try {
                    boolean isConnected = connectionServiceProxy.isConnected();
                    Toast.makeText(this, String.valueOf(isConnected), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_send_message:

                try {
                    Message message = new Message();
                    message.setContent("send message from main");
                    messageServiceProxy.sendMessage(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_register:

                try {
                    messageServiceProxy.registerMessageReceiveListener(mMessageReceiveListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_unregister:

                try {
                    messageServiceProxy.unRegisterMessageReceiveListener(mMessageReceiveListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }
}





















