package com.example.eventbus.event;

public class MainOrderEvent {
    public final String threadInfo;

    public MainOrderEvent(String threadInfo) {
        this.threadInfo = threadInfo;
    }
}
