package com.example.databindingdemo;

import android.content.Context;
import android.content.Intent;

public class MainActivityPresenter implements MainActivityContract.Presenter{

    private MainActivityContract.View view;
    private Context ctx;

    public MainActivityPresenter(MainActivityContract.View view, Context ctx) {
        this.view = view;
        this.ctx = ctx;
    }

    @Override
    public void onShowData(TemperatureData temperatureData) {
        view.showData(temperatureData);
    }

    @Override
    public void onShowList() {
        Intent i = new Intent(ctx, SecondActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ctx.startActivity(i);
    }
}

