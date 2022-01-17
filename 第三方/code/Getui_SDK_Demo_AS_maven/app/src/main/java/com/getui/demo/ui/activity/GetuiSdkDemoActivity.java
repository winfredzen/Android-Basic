package com.getui.demo.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.getui.demo.BuildConfig;
import com.getui.demo.DemoApplication;
import com.getui.demo.R;
import com.getui.demo.config.Config;
import com.getui.demo.receiver.AlarmReceiver;
import com.getui.demo.ui.fragment.AdvancedFunctionFragment;
import com.getui.demo.ui.fragment.AppInfoFragment;
import com.getui.demo.ui.fragment.HomeFragment;

import java.io.File;
import java.lang.ref.WeakReference;


/**
 * Time：2019/9/17
 * Description:.
 * Author:jimlee.
 */
public class GetuiSdkDemoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MASTERSECRET = BuildConfig.MASTERSECRET;
    private static final String TAG = "GetuiSdkDemo";
    private static final int REQUEST_PERMISSION = 0;
    private final String HOME = "home";
    private final String ADVANCED_FUNCTION = "advanced_function";
    private final String APP_INFO = "app_info";
    public HomeFragment homeFragment; //用于保存状态
    private FrameLayout tabHome;
    private FrameLayout tabAdvancedFunction;
    private FrameLayout tabAppInfo;
    private Fragment currentFragment;
    private TextView tvTitle;
    private ImageView ivLogo;
    private AlarmReceiver alarmReceiver = new AlarmReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getui_main);
        requestPermissions();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            checkNotificationEnabled();
        }
        init();
        checkSo();
        selectFragment(HOME);
        registerAlramReceiver();

    }

    private void registerAlramReceiver() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Config.AUTH_ACTION);
        registerReceiver(alarmReceiver, intentFilter);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void checkNotificationEnabled() {
        boolean isEnabled = isNotificationEnabled(this);
        Log.i(TAG, "is notification enabled: " + isEnabled);
        if (!isEnabled) {
            goSetting();
        }
    }

    private void goSetting() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_go_setting, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setContentView(view);
        //确认
        dialog.findViewById(R.id.tv_go_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= 26) {
                    // android 8.0引导
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                } else if (Build.VERSION.SDK_INT >= 21) {
                    // android 5.0-7.0
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("app_package", getPackageName());
                    intent.putExtra("app_uid", getApplicationInfo().uid);
                } else {
                    // 其他
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        //取消
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }


    private void checkSo() {
        // 检查 so 是否存在
        File file = new File(this.getApplicationInfo().nativeLibraryDir + File.separator + "libgetuiext3.so");
        Log.e(TAG, "libgetuiext3.so exist = " + file.exists());
    }

    private void requestPermissions() {

        PackageManager pkgManager = getPackageManager();

        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && (!sdCardWritePermission || !phoneSatePermission)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PERMISSION);
        }

    }


    private void selectFragment(String tag) {
        selectTab(tag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        if ((getSupportFragmentManager().findFragmentByTag(tag)) == null) {

            if (tag.equals(HOME)) {
                currentFragment = homeFragment = HomeFragment.newInstance();
            } else if (tag.equals(ADVANCED_FUNCTION)) {
                currentFragment = AdvancedFunctionFragment.newInstance();
            } else if (tag.equals(APP_INFO)) {
                currentFragment = AppInfoFragment.newInstance();
            }
            Log.d(TAG, "currentFragment: " + currentFragment.getClass().getSimpleName());
            transaction.add(R.id.fragment_container, currentFragment, tag);
            transaction.show(currentFragment);
            transaction.commit();
        } else {
            currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
            transaction.show(currentFragment);
            transaction.commit();
        }

    }

    private void selectTab(String tab) {
        initTabs();
        if (tab.equals(HOME)) {
            tabHome.findViewById(R.id.iv_home).setSelected(true);
            ((TextView) tabHome.findViewById(R.id.tv_home)).setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        } else if (tab.equals(ADVANCED_FUNCTION)) {
            tabAdvancedFunction.findViewById(R.id.iv_advanced_function).setSelected(true);
            ((TextView) tabAdvancedFunction.findViewById(R.id.tv_advanced_function)).setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

        } else if (tab.equals(APP_INFO)) {
            tabAppInfo.findViewById(R.id.iv_app_info).setSelected(true);
            ((TextView) tabAppInfo.findViewById(R.id.tv_app_info)).setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        }
    }

    private void initTabs() {
        tabHome.findViewById(R.id.iv_home).setSelected(false);
        ((TextView) tabHome.findViewById(R.id.tv_home)).setTextColor(getResources().getColor(android.R.color.black));
        tabAdvancedFunction.findViewById(R.id.iv_advanced_function).setSelected(false);
        ((TextView) tabAdvancedFunction.findViewById(R.id.tv_advanced_function)).setTextColor(getResources().getColor(android.R.color.black));
        tabAppInfo.findViewById(R.id.iv_app_info).setSelected(false);
        ((TextView) tabAppInfo.findViewById(R.id.tv_app_info)).setTextColor(getResources().getColor(android.R.color.black));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length != 2 || grantResults[0] == PackageManager.PERMISSION_GRANTED
                    || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                Log.e(TAG, "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void init() {
        DemoApplication.demoActivity = new WeakReference<GetuiSdkDemoActivity>(this);
        tabHome = findViewById(R.id.home_frame);
        tabHome.setOnClickListener(this);
        tabAdvancedFunction = findViewById(R.id.advanced_function_frame);
        tabAdvancedFunction.setOnClickListener(this);
        tabAppInfo = findViewById(R.id.app_info_frame);
        tabAppInfo.setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_title_bar);
        ivLogo = findViewById(R.id.iv_logo);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_frame:
                selectFragment(HOME);
                tvTitle.setText(getResources().getString(R.string.app_title));
                ivLogo.setVisibility(View.VISIBLE);
                break;
            case R.id.advanced_function_frame:
                selectFragment(ADVANCED_FUNCTION);
                tvTitle.setText(getResources().getString(R.string.tab_two));
                ivLogo.setVisibility(View.GONE);
                break;
            case R.id.app_info_frame:
                selectFragment(APP_INFO);
                tvTitle.setText(getResources().getString(R.string.tab_three));
                ivLogo.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alarmReceiver != null) {
            unregisterReceiver(alarmReceiver);
        }
    }
}
