package com.getui.demo.template;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getui.demo.R;
import com.getui.demo.widget.FlowLayoutView;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Time：2019/9/20
 * Description:Tag设置页面
 * Author:jimlee.
 */
public class TagTemplate extends AbstractTemplate implements View.OnClickListener {

    private final String TAG = TagTemplate.class.getSimpleName();

    private FlowLayoutView tagView;


    public TagTemplate(Activity activity) {
        super(activity);
    }

    public static void showKeyBoard(final EditText editText) {
        if (editText != null) {

            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        InputMethodManager inputManager =
                                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(editText, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, 300);

        }
    }

    @Override
    public void initTemplate() {
        activity.setContentView(R.layout.template_set_tag);
        TextView tvTitle = activity.findViewById(R.id.tv_title_bar);
        tvTitle.setText(activity.getResources().getString(R.string.set_tag));
        tagView = activity.findViewById(R.id.tagView);
        FrameLayout framAddTag = activity.findViewById(R.id.fram_add_tag);
        framAddTag.setOnClickListener(this);
        Button confirmBtn = activity.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(this);
        ImageView ivBack = activity.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
    }

    private void showAddTagDialog() {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_set_tag, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setContentView(view);
        final EditText edTag = dialog.findViewById(R.id.ed_tag);
        edTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyBoard(edTag);
            }
        });

        //确认
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edTag.getText().toString()) && edTag.getText().toString().length() <= 40) {
                    addTag(edTag.getText().toString());
                    dialog.dismiss();
                } else if (edTag.getText().toString().length() > 40) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.tag_too_long), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.tag_empty), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消
        dialog.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void addTag(String tag) {
        final View child = LayoutInflater.from(activity).inflate(R.layout.item_tag, tagView, false);
        ((TextView) child.findViewById(R.id.tv_tag)).setText(tag);
        child.findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagView.removeView(child);
            }
        });
        tagView.addView(child);
    }

    private void confirmTags() {

        Tag[] tagParam = new Tag[tagView.getChildCount()];
        for (int i = 0; i < tagView.getChildCount(); i++) {
            View child = tagView.getChildAt(i);
            String tagContent = ((TextView) child.findViewById(R.id.tv_tag)).getText().toString();
            Tag t = new Tag();
            t.setName(tagContent);
            tagParam[i] = t;

        }
        PushManager.getInstance().setTag(activity, tagParam, String.valueOf(System.currentTimeMillis()));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                confirmTags();
                break;
            case R.id.fram_add_tag:
                showAddTagDialog();
                break;
            case R.id.iv_back:
                activity.finish();
                break;
        }
    }


    @Override
    public void onDestroy() {

    }
}
