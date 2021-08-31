package com.example.ipcdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mConnectBtn;
    private Button mDisconnectBtn;
    private Button mIsConnectedBtn;

    private IConnectionService connectionServiceProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnectBtn = (Button) findViewById(R.id.btn_connect);
        mDisconnectBtn = (Button) findViewById(R.id.btn_disconnect);
        mIsConnectedBtn = (Button) findViewById(R.id.btn_is_connected);

        mConnectBtn.setOnClickListener(this);
        mDisconnectBtn.setOnClickListener(this);
        mIsConnectedBtn.setOnClickListener(this);

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                connectionServiceProxy = IConnectionService.Stub.asInterface(service);
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
            default:
                break;
        }
    }
}





















