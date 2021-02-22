package com.example.android.lifecycles.step1;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

public class Chronometer1ViewModel extends ViewModel {

    private Long mStartTime;

    @Nullable
    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(final long startTime) {
        this.mStartTime = startTime;
    }
}
