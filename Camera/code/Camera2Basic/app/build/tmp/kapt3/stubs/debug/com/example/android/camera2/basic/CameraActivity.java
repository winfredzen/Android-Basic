package com.example.android.camera2.basic;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \n2\u00020\u0001:\u0001\nB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0014J\b\u0010\t\u001a\u00020\u0006H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/example/android/camera2/basic/CameraActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "activityCameraBinding", "Lcom/example/android/camera2/basic/databinding/ActivityCameraBinding;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "Companion", "app_debug"})
public final class CameraActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.android.camera2.basic.databinding.ActivityCameraBinding activityCameraBinding;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.android.camera2.basic.CameraActivity.Companion Companion = null;
    
    /**
     * Combination of all flags required to put activity into immersive mode
     */
    public static final int FLAGS_FULLSCREEN = 4357;
    
    /**
     * Milliseconds used for UI animations
     */
    public static final long ANIMATION_FAST_MILLIS = 50L;
    public static final long ANIMATION_SLOW_MILLIS = 100L;
    private static final long IMMERSIVE_FLAG_TIMEOUT = 500L;
    
    public CameraActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/example/android/camera2/basic/CameraActivity$Companion;", "", "()V", "ANIMATION_FAST_MILLIS", "", "ANIMATION_SLOW_MILLIS", "FLAGS_FULLSCREEN", "", "IMMERSIVE_FLAG_TIMEOUT", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}