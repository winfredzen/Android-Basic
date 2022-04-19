package com.wz.triangle_jni;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.wz.triangle_jni.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTag";
    GLSurfaceView mGLSurfaceView;
    private boolean rendererSet = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        /*
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
         */
        // Even though the latest emulator supports OpenGL ES 2.0,
        // it has a bug where it doesn't set the reqGlEsVersion so
        // the above check doesn't work. The below will detect if the
        // app is running on an emulator, and assume that it supports
        // OpenGL ES 2.0.
        final boolean supportsEs2 =
                configurationInfo.reqGlEsVersion >= 0x20000
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));

        Log.d(TAG, "supportsEs2 = " + supportsEs2);

        if (supportsEs2) {
            mGLSurfaceView.setEGLContextClientVersion(2);
            mGLSurfaceView.setRenderer(new TriangleRender());
            rendererSet = true;
        } else {
            Toast.makeText(this, "不支持OpenGL 2.0", Toast.LENGTH_SHORT).show();
        }

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) {
            mGLSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) {
            mGLSurfaceView.onResume();
        }
    }

    // Used to load the 'triangle_jni' library on application startup.
    static {
        System.loadLibrary("triangle_jni");
    }

    /**
     * A native method that is implemented by the 'triangle_jni' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}