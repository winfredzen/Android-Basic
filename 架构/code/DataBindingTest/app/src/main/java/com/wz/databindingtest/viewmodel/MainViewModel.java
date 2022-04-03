package com.wz.databindingtest.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

/**
 * create by wangzhen 2022/4/1
 */
public class MainViewModel extends ViewModel {

    public ObservableBoolean check = new ObservableBoolean(false);


    public void onCheckChange() {
        Log.d("TAG", "onCheckChange");
    }

    public void onMyOnCheckChange(View view, boolean check) {
        Log.d("TAG", "onMyOnCheckChange check = " + check);
    }

}
