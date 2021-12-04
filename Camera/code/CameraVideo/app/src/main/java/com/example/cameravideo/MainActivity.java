package com.example.cameravideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    private TextureView mTextureView;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        /**
         * And once the android TextureView is available we will be able to retrieve its width & height dimensions
         * which are required to calculate the application’s preview and video resolutions.
         *
         * TextureView available后，获取width & height，计算preview 和 video resolution
         */
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int width, int height) {
            Toast.makeText(getApplicationContext(), "TextureView is available", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onSurfaceTextureAvailable width " + width + ", height = " + height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

        }
    };

    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();;
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();;
            mCameraDevice = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextureView = (TextureView) findViewById(R.id.textureView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()) {

        } else {
            //If the TextureView is not available the surface texture will need to be setup with the surface texture listener member.
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        super.onPause();
    }

    /**
     * To implement sticky immersion you need to override this method onWindowFocusChanged()
     * 即实现全屏模式
     * 1.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
     * 稳定布局，主要是在全屏和非全屏切换时，布局不要有大的变化。
     * 一般和View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN、View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION搭配使用
     * 2.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY 沉浸模式，用户可以交互的界面。同时，用户上下拉系统栏时，会自动隐藏系统栏
     * 3.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION 隐藏导航栏，用户点击屏幕会显示导航栏
     * 4.View.SYSTEM_UI_FLAG_FULLSCREEN 隐藏状态栏
     * 5.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION 拓展布局到导航栏后面
     * 6.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 拓展布局到状态栏后面
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    /**
     * 释放CameraDevice
     */
    private void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

}