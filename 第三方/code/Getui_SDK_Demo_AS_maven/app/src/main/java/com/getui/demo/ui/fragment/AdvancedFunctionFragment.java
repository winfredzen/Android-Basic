package com.getui.demo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.getui.demo.R;
import com.getui.demo.ui.AdvancedFunctionDetailsProxy;
import com.getui.demo.ui.activity.AdvancedFunctionDetailsActivity;

/**
 * Time：2019/9/17
 * Description:高级功能页面
 * Author:jimlee.
 */
public class AdvancedFunctionFragment extends Fragment implements View.OnClickListener {

    private final String TAG = AdvancedFunctionFragment.class.getSimpleName();

    public static AdvancedFunctionFragment newInstance() {
        return new AdvancedFunctionFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_advanced_function, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        LinearLayout llBindAlias = view.findViewById(R.id.ll_bind_alias);
        llBindAlias.setOnClickListener(this);

        RelativeLayout rlUnBindAlias = view.findViewById(R.id.rl_unbind_alias);
        rlUnBindAlias.setOnClickListener(this);

        RelativeLayout rlSetTag = view.findViewById(R.id.rl_set_tag);
        rlSetTag.setOnClickListener(this);

        RelativeLayout rlSetSilentTime = view.findViewById(R.id.rl_set_silent_time);
        rlSetSilentTime.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), AdvancedFunctionDetailsActivity.class);
        switch (v.getId()) {
            case R.id.ll_bind_alias:  //绑定别名
                i.putExtra("template", AdvancedFunctionDetailsProxy.TEMPLATE_BIND_ALIAS);
                startActivity(i);
                break;
            case R.id.rl_unbind_alias: //解绑别名
                i.putExtra("template", AdvancedFunctionDetailsProxy.TEMPLATE_UNBIND_ALIAS);
                startActivity(i);
                break;
            case R.id.rl_set_tag:  //设置tag
                i.putExtra("template", AdvancedFunctionDetailsProxy.TEMPLATE_SET_TAG);
                startActivity(i);
                break;
            case R.id.rl_set_silent_time: //静默时间
                i.putExtra("template", AdvancedFunctionDetailsProxy.TEMPLATE_SET_SILENT_TIME);
                startActivity(i);
                break;
        }
    }
}
