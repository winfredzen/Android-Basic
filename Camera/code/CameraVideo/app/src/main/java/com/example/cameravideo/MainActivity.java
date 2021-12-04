package com.example.cameravideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

            setupCamera(width, height);
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

    private String mCameraId;

    /**
     * It is important to setup a background handler to remove time consuming tasks from the main UI thread.
     *
     * Many of the api’s provided by android camera2 provide support for a background thread handler.
     * So it’s recommended to provide a background thread handler if supported.
     */
    private HandlerThread mBackgroundHandlerThread;
    private Handler mBackgroundHandler;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    /**
     * 比较size
     */
    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    /**
     * 预览尺寸大小
     */
    private Size mPreviewSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextureView = (TextureView) findViewById(R.id.textureView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            setupCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            //If the TextureView is not available the surface texture will need to be setup with the surface texture listener member.
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    protected void onPause() {
        closeCamera();
        stopBackgroundThread();
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

    private void setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                // Find the rotation of the device relative to the native device orientation.
                int deviceOrientation = getWindowManager().getDefaultDisplay().getRotation();
                // Find the rotation of the device relative to the camera sensor's orientation.
                int totalRotation = sensorToDeviceRotation(cameraCharacteristics, deviceOrientation);
                // Swap the view dimensions for calculation as needed if they are rotated relative to
                // the sensor.
                boolean swapRotation = totalRotation == 90 || totalRotation == 270;
                int rotatedWidth = width;
                int rotatedHeight = height;
                if(swapRotation) {
                    rotatedWidth = height;
                    rotatedHeight = width;
                }
                //获取到预览尺寸
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), rotatedWidth, rotatedHeight);
                Log.d(TAG, "mPreviewSize = " + mPreviewSize.toString());
                mCameraId = cameraId;
                Log.d(TAG, "mCameraId = " + mCameraId);
                return;

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
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

    private void startBackgroundThread() {
        mBackgroundHandlerThread = new HandlerThread("Camera2VideoImage");
        mBackgroundHandlerThread.start();
        mBackgroundHandler = new Handler(mBackgroundHandlerThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundHandlerThread.quitSafely();
        try {
            mBackgroundHandlerThread.join();//等待mBackgroundHandlerThread线程运行结束
            mBackgroundHandlerThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculate the camera sensor orientation in relation to the device orientation
     * 1.对于后置摄像头
     *  1.1 正常竖直方向 deviceOrientation = 0 sensorOrientation = 90
     *  1.2 正常向左旋转，横屏 deviceOrientation = 90 sensorOrientation = 90
     *  1.3 正常向右旋转，横屏 deviceOrientation = 270 sensorOrientation = 90
     *  //1.4 竖直方向，颠倒 deviceOrientation = 0 sensorOrientation = 90
     * @param c
     * @param deviceOrientation
     * @return
     */
    private static int sensorToDeviceRotation(CameraCharacteristics c, int deviceOrientation) {
        int sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION);
        // Get device orientation in degrees
        deviceOrientation = ORIENTATIONS.get(deviceOrientation);
        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        return (sensorOrientation + deviceOrientation + 360) % 360;
    }

    /**
     * Create a method for matching the TextureView dimensions against the video & preview dimensions
     *
     * @param choices
     * @param width
     * @param height
     * @return
     */
    private static Size chooseOptimalSize(Size[] choices, int width, int height) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<Size>();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * height / width &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }
        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }


}