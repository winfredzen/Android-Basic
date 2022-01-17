package com.getui.demo.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.getui.demo.ui.AdvancedFunctionDetailsProxy;

/**
 * Time：2019/9/20
 * Description:高级功能代理Activity.
 * Author:jimlee.
 */
public class AdvancedFunctionDetailsActivity extends Activity {

    private AdvancedFunctionDetailsProxy proxy; //具体模版实现的代理类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String template = getIntent().getStringExtra("template");
        proxy = AdvancedFunctionDetailsProxy.getProxy(this, template);
        proxy.onCreate();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        proxy.onDestroy();
    }
}
