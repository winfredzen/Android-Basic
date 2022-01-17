package com.getui.demo.template;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getui.demo.R;
import com.getui.demo.ui.AdvancedFunctionDetailsProxy;
import com.igexin.sdk.PushManager;

/**
 * Time：2019/9/20
 * Description:绑定/解绑别名页面
 * Author:jimlee.
 */
public class AliasTemplate extends AbstractTemplate {

    public final String TAG = AliasTemplate.class.getSimpleName();

    private String type;  //初始化不同模版

    private TextView tvTitle;
    private ImageView ivBack;
    private EditText edTag;
    private Button btnConfirm;
    private TextView tvHint;
    private TextView tvExplain;
    private ImageView ivIcon;

    public AliasTemplate(Activity activity, String type) {
        super(activity);
        this.type = type;
    }


    @Override
    public void initTemplate() {
        activity.setContentView(R.layout.template_alias);
        initView();
    }


    @Override
    public void onDestroy() {

    }

    private void initView() {
        tvExplain = activity.findViewById(R.id.tv_explain);
        ivIcon = activity.findViewById(R.id.iv_icon);
        tvTitle = activity.findViewById(R.id.tv_title_bar);
        edTag = activity.findViewById(R.id.ed_tag);
        btnConfirm = activity.findViewById(R.id.btn_confirm);
        tvHint = activity.findViewById(R.id.tv_hint);
        ivBack = activity.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        if (type.equals(AdvancedFunctionDetailsProxy.TEMPLATE_BIND_ALIAS)) {  //初始化绑定别名事件相关操作
            BindAlias bindAlias = new BindAlias();
            bindAlias.initViews();
        } else if (type.equals(AdvancedFunctionDetailsProxy.TEMPLATE_UNBIND_ALIAS)) {  //初始化解绑别名事件相关操作
            UnBindAlias unbindAlias = new UnBindAlias();
            unbindAlias.initViews();
        }
    }

    class BindAlias {

        protected void initViews() {
            tvExplain.setText(activity.getResources().getString(R.string.alias_instruction));
            ivIcon.setImageResource(R.drawable.bind_alias);
            tvTitle.setText(activity.getResources().getString(R.string.bind_alias));
            tvHint.setText(activity.getResources().getString(R.string.bind_alias_hint));
            btnConfirm.setText(activity.getResources().getString(R.string.confirm_bind));
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bindAlias();
                }
            });
        }

        private void bindAlias() {
            String alias = edTag.getText().toString();
            if (!TextUtils.isEmpty(alias)) {
                PushManager.getInstance().bindAlias(activity, alias);
                Log.d(TAG, "bind alias = " + alias);
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.input_alias), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class UnBindAlias {

        protected void initViews() {
            tvExplain.setText(activity.getResources().getString(R.string.alias_instruction));
            ivIcon.setImageResource(R.drawable.unbind_alias);
            tvTitle.setText(activity.getResources().getString(R.string.unbind_alias));
            tvHint.setText(activity.getResources().getString(R.string.unbind_alias_hint));
            btnConfirm.setText(activity.getResources().getString(R.string.confirm_unbind));
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unBindAlias();
                }
            });
        }

        private void unBindAlias() {
            String alias = edTag.getText().toString();
            if (!TextUtils.isEmpty(alias)) {
                PushManager.getInstance().unBindAlias(activity, alias, false);
                Log.d(TAG, "unbind alias = " + alias);
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.input_alias), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
