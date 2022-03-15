package com.xingkong.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.glumes.cppso.utils.LogUtil;
import com.xingkong.myapplication.jni.JNIAccessField;
import com.xingkong.myapplication.jni.JNIBasicType;
import com.xingkong.myapplication.jni.JNIBitmap;
import com.xingkong.myapplication.jni.JNIDynamicLoad;
import com.xingkong.myapplication.jni.JNIException;
import com.xingkong.myapplication.jni.JNIInvokeMethod;
import com.xingkong.myapplication.jni.JNIProductAndConsumerThread;
import com.xingkong.myapplication.jni.JNIReference;
import com.xingkong.myapplication.jni.JNIReferenceType;
import com.xingkong.myapplication.jni.JNIString;
import com.xingkong.myapplication.jni.JNIThread;
import com.xingkong.myapplication.jni.JNIWaitNotify;
import com.xingkong.myapplication.model.Animal;
import com.xingkong.myapplication.thread.ICallbackMethod;
import com.xingkong.myapplication.thread.IThreadCallback;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private String[] strings ={"apple","pear","banana"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        final TextView tv = findViewById(R.id.sample_text);
        final JNIDynamicLoad mJNIDynamicLoad= new JNIDynamicLoad();
        final JNIBasicType mJNIBasicType= new JNIBasicType();
        final JNIString mJNIString= new JNIString();
        final JNIReferenceType mJNIReferenceType= new JNIReferenceType();
        final JNIAccessField mJNIAccessField= new JNIAccessField();
        final JNIInvokeMethod mJNIInvokeMethod= new JNIInvokeMethod();
        final JNIReference mJNIReference= new JNIReference();
        final JNIException mJNIException= new JNIException();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                tv.setText(mJNIBasicType.callNativeInt(5)+"");
//                //字符串操作
//                LogUtil.Companion.d( mJNIString.callNativeString("1223"));
//                LogUtil.Companion.d( mJNIString.reverseString("1223"));
//                LogUtil.Companion.d( mJNIString.getHalfString("1223"));
//                mJNIString.StringMethod("StringMethod");
                 //数组操作
//                LogUtil.Companion.i( "array item is "+mJNIReferenceType.callNativeStringArray(strings));
                 //jni 回调java 方法
//                   mJNIAccessField.invoke();
                //jni 子线程回调java 方法
//                mJNIInvokeMethod.NativeCallback(new ICallbackMethod() {
//                    @Override
//                    public void Callback() {
//                        LogUtil.Companion.i("ICallbackMethod name is"+Thread.currentThread().getName());
//                    }
//                });
//                mJNIInvokeMethod.NativeThreadCallback(new IThreadCallback() {
//                    @Override
//                    public void Callback() {
//                        LogUtil.Companion.i("IThreadCallback name is"+Thread.currentThread().getName());
//                    }
//                });
                // 本地引用 全局引用 弱引用
//                mJNIReference.invoke();
                //JAVA 异常处理
//                mJNIException.invoke();

            }
        });
        //线程部分
//        final AppCompatButton button1 = findViewById(R.id.button1);
//        final AppCompatButton button2 = findViewById(R.id.button2);
//        final AppCompatButton button3 = findViewById(R.id.button3);
//        final JNIThread MJNIThread=new JNIThread();
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MJNIThread.createNativeThread();
//
//            }
//        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MJNIThread.createNativeThreadWithArgs();
//            }
//        });
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MJNIThread.joinNativeThread();
//            }
//        });
//        //线程同步问题
//        final AppCompatButton button1 = findViewById(R.id.button1);
//        final AppCompatButton button2 = findViewById(R.id.button2);
//        button1.setText("线程等待");
//        button2.setText("线程通知");
//        final JNIWaitNotify MJNIWaitNotify=new JNIWaitNotify();
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MJNIWaitNotify.waitNativeThread();
//            }
//        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MJNIWaitNotify.notifyNativeThread();
//            }
//        });
//        //生产者 消费者
//        final AppCompatButton button3 = findViewById(R.id.button3);
//        button3.setText("生产者消费者");
//        final JNIProductAndConsumerThread mJNIProductAndConsumerThread=new JNIProductAndConsumerThread();
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mJNIProductAndConsumerThread.startProductAndConsumerThread();
//            }
//        });

        //bitMap的操作
        final AppCompatImageView image = findViewById(R.id.image);
        final Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.aaa);
        final JNIBitmap jniBitmap= new JNIBitmap();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap result= jniBitmap.callNativeMirrorBitmap(bitmap);
                image.setImageBitmap(result);
            }
        });



    }





}
