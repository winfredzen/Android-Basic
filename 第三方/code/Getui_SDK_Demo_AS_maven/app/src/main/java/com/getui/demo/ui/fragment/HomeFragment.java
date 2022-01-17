package com.getui.demo.ui.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getui.demo.DemoApplication;
import com.getui.demo.R;
import com.getui.demo.receiver.AlarmUtils;
import com.getui.demo.ui.presenter.AuthInteractor;
import com.getui.demo.ui.presenter.HomePresenter;
import com.igexin.sdk.PushManager;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.getui.demo.config.Config.AUTH_ACTION;

/**
 * Time：2019/9/17
 * Decription:主页面 透传/通知栏测试
 * Author:jimlee.
 */
public class HomeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener, HomePresenter.HomeView {

    private static final String TAG = HomeFragment.class.getSimpleName();

    public TextView tvClientId;
    public TextView tvLog;
    public TextView tvCidState;
    SharedPreferences sp;
    private Toast toast;
    private CheckBox cbSdkServer;
    private HomePresenter homePresenter;
    private AlarmManager manager = (AlarmManager) DemoApplication.appContext.getSystemService(ALARM_SERVICE);


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);
        initViews(view);
        // 应用未启动, 个推 service已经被唤醒,显示该时间段内离线消息
        if (DemoApplication.payloadData != null) {
            tvLog.append(DemoApplication.payloadData);
        }
        if (!TextUtils.isEmpty(DemoApplication.isCIDOnLine)) {
            tvCidState.setText(DemoApplication.isCIDOnLine.equals("true") ? getString(R.string.online) : getString(R.string.offline));
        }
        if (!TextUtils.isEmpty(DemoApplication.cid)) {
            tvClientId.setText(DemoApplication.cid);
        }

        return view;
    }

    private void initViews(View view) {
        homePresenter = new HomePresenter(this);
        tvLog = view.findViewById(R.id.tvLog);
        tvLog.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvClientId = view.findViewById(R.id.tv_clientid);
        cbSdkServer = view.findViewById(R.id.cb_sdk_server);
        cbSdkServer.setClickable(true);
        sp = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        cbSdkServer.setChecked(sp.getBoolean("isServiceOn", true));
        cbSdkServer.setOnCheckedChangeListener(this);
        tvCidState = view.findViewById(R.id.tv_cid_state);
        //通知栏
        Button btnNotification = view.findViewById(R.id.btn_notification);
        btnNotification.setOnClickListener(this);
        //透传
        Button btnTransmission = view.findViewById(R.id.btn_transmission);
        btnTransmission.setOnClickListener(this);
        Button btnCopyCid = view.findViewById(R.id.btn_copy_cid);
        btnCopyCid.setOnClickListener(this);
        ImageView btnDeleteLog = view.findViewById(R.id.iv_clear_log);
        btnDeleteLog.setOnClickListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            PushManager.getInstance().turnOnPush(getActivity());
            showToast(getString(R.string.start));
        } else {
            PushManager.getInstance().turnOffPush(getActivity());
            showToast(getString(R.string.stop));
        }
        sp.edit().putBoolean("isServiceOn", isChecked).apply();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notification:
                homePresenter.sendNotification();
                break;
            case R.id.btn_transmission:
                homePresenter.sendTransmission();
                break;
            case R.id.btn_copy_cid:
                copyCid();
                break;
            case R.id.iv_clear_log:
                tvLog.setText("");
                DemoApplication.payloadData.delete(0, DemoApplication.payloadData.length());
                showToast(getString(R.string.log_cleared));
                break;
        }
    }

    private void copyCid() {
        String content = tvClientId.getText().toString();
        if (TextUtils.isEmpty(content) || content.equals(getString(R.string.no_clientid))) {
            showToast(getString(R.string.cid_empty));
            return;

        }
        ClipboardManager cm = getActivity() == null ? null : (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
            ClipData mClipData = ClipData.newPlainText("Label", content);// 将文本内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            showToast(getString(R.string.copy_successed));
        }
    }


    private void showToast(String content) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0以上toast直接用原生的方法即可，并不用setText防止重复的显示的问题
            Toast.makeText(DemoApplication.appContext, content, Toast.LENGTH_SHORT).show();
            return;
        }
        if (toast != null) {
            toast.setText(content);
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(DemoApplication.appContext, content, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    public void onNotificationSended(String msg) {
        showToast(msg);
        Log.i(TAG, "onNotificationSendFailed = " + msg);
        if (msg.equals("not_auth")) {
            if (AuthInteractor.isTimeCorrect != null && !AuthInteractor.isTimeCorrect) {
                showToast("鉴权失败，请同步本地时间后重启应用");
                return;
            }
            showToast("鉴权失败,请检查签名参数及本地时间");
            AlarmUtils.startAuthAlram(manager,false,5);
        }
    }

    @Override
    public void onNotificationSendFailed(String msg) {
        showToast(msg);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (homePresenter != null) {
            homePresenter.onDestroy();
        }
        Intent intent = new Intent(AUTH_ACTION);
        PendingIntent pendingintent =
                PendingIntent.getBroadcast(DemoApplication.appContext, 0, intent, PendingIntent.FLAG_NO_CREATE);
        if (pendingintent != null) {
            manager.cancel(pendingintent);
        }

    }


}
