package com.example.databindingdemo;

import java.util.List;

public class MyAdapter2 extends MyBaseAdpater {

    private List<TemperatureData> data;

    public MyAdapter2(List<TemperatureData> data) {
        this.data = data;
    }

    @Override
    public Object getDataAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public int getLayoutIdForType(int viewType) {
        return R.layout.row_layout;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
