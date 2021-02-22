/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.lifecycles.step1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.SystemClock;
import android.widget.Chronometer;

import com.example.android.codelabs.lifecycle.R;


public class ChronoActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Chronometer1ViewModel chronometer1ViewModel = new ViewModelProvider(this).get(Chronometer1ViewModel.class);

        Chronometer chronometer = findViewById(R.id.chronometer);

        if (chronometer1ViewModel.getStartTime() == null ) {
            long startTime = SystemClock.elapsedRealtime(); //返回系统启动到现在的时间，包含设备深度休眠的时间
            chronometer1ViewModel.setStartTime(startTime);
            chronometer.setBase(startTime);
        } else {
            chronometer.setBase(chronometer1ViewModel.getStartTime());
        }

        chronometer.start();
    }
}
