package com.getui.demo.ui;

import android.app.Activity;

import com.getui.demo.template.AbstractTemplate;
import com.getui.demo.template.AliasTemplate;
import com.getui.demo.template.SilentTimeConfigTemplate;
import com.getui.demo.template.TagTemplate;

/**
 * Time：2019/9/20
 * Description:SDK高级功能的代理类
 * Author:jimlee.
 */
public class AdvancedFunctionDetailsProxy {


    public static final String TEMPLATE_UNBIND_ALIAS = "template_unbind_alias";
    public static final String TEMPLATE_BIND_ALIAS = "template_bind_alias";
    public static final String TEMPLATE_SET_TAG = "template_set_tag";
    public static final String TEMPLATE_SET_SILENT_TIME = "template_set_silent_time";

    private Activity context;

    private AbstractTemplate template;

    private AdvancedFunctionDetailsProxy(Activity context, String template) {
        this.context = context;
        this.template = createTemplate(template);
    }

    public static AdvancedFunctionDetailsProxy getProxy(Activity context, String template) {
        return new AdvancedFunctionDetailsProxy(context, template);
    }

    private AbstractTemplate createTemplate(String template) {

        if (template.equals(TEMPLATE_BIND_ALIAS)) {
            return new AliasTemplate(context, TEMPLATE_BIND_ALIAS);
        } else if (template.equals(TEMPLATE_UNBIND_ALIAS)) {
            return new AliasTemplate(context, TEMPLATE_UNBIND_ALIAS);
        } else if (template.equals(TEMPLATE_SET_TAG)) {
            return new TagTemplate(context);
        } else if (template.equals(TEMPLATE_SET_SILENT_TIME)) {
            return new SilentTimeConfigTemplate(context);
        }

        return null;

    }


    public void onCreate() {
        if (template != null) {
            template.onCreate();
        }
    }

    public void onDestroy() {
        if (template != null) {
            template.onDestroy();
        }
    }

}
