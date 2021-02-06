package com.example.databindingdemo;

public interface MainActivityContract {

    public interface Presenter {
        void onShowData(TemperatureData temperatureData);
        void onShowList();
    }

    public interface View {
        void showData(TemperatureData temperatureData);
    }

}
