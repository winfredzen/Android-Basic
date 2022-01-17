package com.getui.demo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getui.demo.R;
import com.getui.demo.config.Config;
import com.getui.demo.ui.activity.GetuiSdkDemoActivity;
import com.igexin.sdk.PushManager;

/**
 * Time：2019/9/17
 * Description:应用信息页面
 * Author:jimlee.
 */
public class AppInfoFragment extends Fragment {



    public static AppInfoFragment newInstance() {
        return new AppInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_app_info, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        TextView tvAppName = view.findViewById(R.id.tv_app_name);
        tvAppName.setText(Config.appName);
        TextView tvAppId = view.findViewById(R.id.tv_appid);
        TextView tvPkgName = view.findViewById(R.id.tv_pkgname);
        tvPkgName.setText(Config.packageName);
        TextView tvSdkVer = view.findViewById(R.id.tv_sdk_ver);
        tvSdkVer.setText(PushManager.getInstance().getVersion(getActivity()));
        TextView tvMasterSecret = view.findViewById(R.id.tv_master_secret);
        /*初始化配置信息*/
        tvAppId.setText(Config.appid);
        /*校验MASTERSECRET*/
        String pattern = "^[a-z0-9A-Z]+$";
        if (GetuiSdkDemoActivity.MASTERSECRET.matches(pattern)) {
            tvMasterSecret.setText(GetuiSdkDemoActivity.MASTERSECRET);
        }
    }


}
