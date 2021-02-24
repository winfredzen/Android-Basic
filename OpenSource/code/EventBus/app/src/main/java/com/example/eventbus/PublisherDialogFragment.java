package com.example.eventbus;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.TimeUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.eventbus.event.FailureEvent;
import com.example.eventbus.event.MainOrderEvent;
import com.example.eventbus.event.PostingEvent;
import com.example.eventbus.event.SuccesssEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

public class PublisherDialogFragment extends DialogFragment {

    private static final String TAG = "PublisherDialogFragment";

    private OnEventListener mListener;

    public interface OnEventListener {
        void onSuccess();
        void onFailure();
    }

    public void setEventListener(OnEventListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Publisher");
        final String[] items = {"Success", "Failure", "Posting", "Main_Ordered"}; //设置对话框要显示的一个list，一般用于显示几个命令时
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        //succ
                        EventBus.getDefault().post(new SuccesssEvent());
                    }
                        break;
                    case 1: {
                        //fail
                        EventBus.getDefault().post(new FailureEvent());
                    }
                        break;
                    case 2: {
                        //启动一个线程发送事件
                        new Thread("posting-002") {
                            @Override
                            public void run() {
                                super.run();
                                EventBus.getDefault().post(new PostingEvent(Thread.currentThread().toString()));
                            }
                        }.start();

                    }
                    case 3: {
                        Log.d(TAG, "onClick: before @" + SystemClock.uptimeMillis());
                        EventBus.getDefault().post(new MainOrderEvent(Thread.currentThread().toString()));
                        try {
                            TimeUnit.SECONDS.sleep(1); //休眠1s
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onClick: after @" + SystemClock.uptimeMillis());
                    }
                    break;
                    default:
                        break;
                }
            }
        });
        return builder.create();
    }
}
