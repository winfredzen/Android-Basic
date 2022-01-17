package com.getui.demo.template;

import android.app.Activity;

/**
 * Time：2019/9/20
 * Description:.
 * Author:jimlee.
 */
public abstract class AbstractTemplate {

    protected Activity activity;

    public AbstractTemplate(Activity activity) {
        this.activity = activity;
    }


    public void onCreate() {
        initTemplate();
    }

    public abstract void initTemplate();


    public abstract void onDestroy();
}
