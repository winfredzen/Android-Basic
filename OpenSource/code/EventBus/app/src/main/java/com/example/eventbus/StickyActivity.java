package com.example.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.eventbus.event.StickyMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class StickyActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
    }

    @Subscribe(sticky = true)
    public void onStickyMessageEvent(StickyMessageEvent event) {
        setTitle(event.message);
    }
}