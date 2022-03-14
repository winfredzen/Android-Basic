package com.wz.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wz.myapplication.databinding.ActivityMainBinding;
import com.wz.myapplication.jni.JNIBasicType;
import com.wz.myapplication.load.JNIDynamicLoad;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'myapplication' library on application startup.
    static {
        System.loadLibrary("myapplication");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
//        tv.setText(stringFromJNI());

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                JNIDynamicLoad jniDynamicLoad = new JNIDynamicLoad();
//                tv.setText(jniDynamicLoad.getNativeString());

                JNIBasicType jniBasicType = new JNIBasicType();
                tv.setText(jniBasicType.callNativeInt(2) + "");

            }
        });


    }

    /**
     * A native method that is implemented by the 'myapplication' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String getString();
}