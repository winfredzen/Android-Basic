package com.example.eventbus;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

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
        final String[] items = {"Success", "Failure"}; //设置对话框要显示的一个list，一般用于显示几个命令时
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //succ
                        if (mListener != null) {
                            mListener.onSuccess();
                        }
                        break;
                    case 1:
                        //fail
                        if (mListener != null) {
                            mListener.onFailure();
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
