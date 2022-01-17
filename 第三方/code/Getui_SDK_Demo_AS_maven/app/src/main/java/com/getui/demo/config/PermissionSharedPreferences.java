package com.getui.demo.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PermissionSharedPreferences {

    private static final String SPNAME = "demo_per";
    public static final String KEY_ALERTED = "alerted";
    public static final String KEY_POLICYSTRATEGY = "policy";

    public static void saveParam(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(SPNAME, 0);
        Editor editor = sp.edit();
        if (value instanceof String) {
            editor.putString(key, (String)value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer)value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean)value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float)value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long)value);
        }

        editor.apply();
    }

    public static Object getParam(Context context, String key, Object defValue) {
        SharedPreferences sp = context.getSharedPreferences(SPNAME, 0);

        if (defValue instanceof String) {
            return sp.getString(key, (String)defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer)defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean)defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float)defValue);
        } else {
            return defValue instanceof Long ? sp.getLong(key, (Long)defValue) : defValue;
        }
    }
}
