package com.wz.bluetoothtest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothTag";
    private static final int REQUEST_ENABLE_BT = 1000;
    private static final int MY_PERMISSION_REQUEST_CODE = 1001;

    //权限
    private String[] permissions = new String[]{
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private EditText logEditText;
    private BluetoothAdapter bluetoothAdapter;


    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d(TAG, "BroadcastReceiver onReceive");
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                logEditText.append("deviceName = " + deviceName + ", deviceHardwareAddress = " + deviceHardwareAddress + "\n");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }



    private void init() {
        logEditText = findViewById(R.id.log_edit_text);
        registerReceiver();
        requestPermissions();
        setUpBluetooth();
    }


    private void registerReceiver() {
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }


    public void requestPermissions() {
        // 检查是否有相应的权限
        boolean isAllGranted = checkPermissionsAllGranted(permissions);
        Log.d(TAG, "requestPermissions isAllGranted = " + isAllGranted);
        // 如果这3个权限全部拥有，则直接执行备份
        if (isAllGranted) {
            return;
        }

        // 请求多个权限，如果有他有权限已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                permissions,
                MY_PERMISSION_REQUEST_CODE
        );
    }

    private void setUpBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.d(TAG, "设备不支持蓝牙");
        }
        Log.d(TAG, "设备支持蓝牙");
        logEditText.append("设备支持蓝牙\n");
        //启用蓝牙
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            logEditText.append("已启用蓝牙\n");
        }
    }

    /**
     * 查询已配对设备.
     *
     * @param view
     */
    public void getBondedDevices(View view) {
        logEditText.setText("");
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                logEditText.append("deviceName = " + deviceName + ", deviceHardwareAddress = " + deviceHardwareAddress + "\n");
            }
        }
    }

    /**
     * 发现设备.
     *
     * @param view
     */
    public void startDiscovery(View view) {
        Log.d(TAG, "startDiscovery");
        logEditText.setText("");
        boolean b = bluetoothAdapter.startDiscovery();
        Log.d(TAG, "startDiscovery b = " + b);
    }

    //------------------ 打开or关闭蓝牙 --------------------//

    public boolean setBluetoothEnabled(View view) {
        boolean enabled = bluetoothAdapter.isEnabled();
        boolean success = enabled ? bluetoothAdapter.disable() : bluetoothAdapter.enable();
        return success;
    }


    //------------------ 打开or关闭蓝牙 --------------------//


    /**
     * 检查是否拥有指定的所有权限
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissionsAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授权，则直接返回false
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            Log.d(TAG, "onActivityResult 蓝牙");
            logEditText.append("已启用蓝牙\n");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver);
    }

}