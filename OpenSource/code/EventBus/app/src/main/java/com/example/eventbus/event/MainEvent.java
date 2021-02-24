package com.example.eventbus.event;

public class MainEvent {
    public final String threadInfo;

    public MainEvent(String threadInfo) {
        this.threadInfo = threadInfo;
    }
}
