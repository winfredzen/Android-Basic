package com.getui.demo.template;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getui.demo.R;
import com.igexin.sdk.PushManager;

import java.util.regex.Pattern;

/**
 * Time：2019/9/29
 * Description:静默时间设置页面
 * Author:jimlee.
 */
public class SilentTimeConfigTemplate extends AbstractTemplate implements View.OnClickListener {
    private final String TAG = SilentTimeConfigTemplate.class.getSimpleName();

    public SilentTimeConfigTemplate(Activity activity) {
        super(activity);
    }


    @Override
    public void initTemplate() {
        activity.setContentView(R.layout.template_silent_config);
        TextView tvTitle = activity.findViewById(R.id.tv_title_bar);
        tvTitle.setText(activity.getResources().getString(R.string.silent_time_config));
        ImageView ivBack = activity.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        Button btnConfirm = activity.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                activity.finish();
                break;
            case R.id.btn_confirm:
                setSilentTime();
                break;
        }
    }

    private void setSilentTime() {
        String regex = "^[0-9]$|[1][0-9]|[2][0-3]";
        String beginText = ((TextView) activity.findViewById(R.id.ed_begin_time)).getText().toString();
        String durationText = ((TextView) activity.findViewById(R.id.ed_duration_time)).getText().toString();

        try {

            if (!TextUtils.isEmpty(beginText) && !TextUtils.isEmpty(durationText)
                    && Pattern.matches(regex, beginText) && Pattern.matches(regex, durationText)) {
                int beginHour = Integer.valueOf(beginText);
                int durationHour = Integer.valueOf(durationText);
                boolean result = PushManager.getInstance().setSilentTime(activity, beginHour, durationHour);
                if (result) {
                    Toast.makeText(activity, "begin = " + beginHour + ", duration = " + durationHour, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "setSilentime, begin = " + beginHour + ", duration = " + durationHour);
                } else {
                    Toast.makeText(activity, "setSilentime failed, value exceeding", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "setSilentime failed, value exceeding");
                }
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.check_silent_time_format), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
