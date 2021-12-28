package com.raywenderlich.android.movieapp;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u0000H\u0002J\b\u0010\u000b\u001a\u00020\fH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006\u000e"}, d2 = {"Lcom/raywenderlich/android/movieapp/MovieApplication;", "Landroid/app/Application;", "()V", "appComponent", "Lcom/raywenderlich/android/movieapp/AppComponent;", "getAppComponent", "()Lcom/raywenderlich/android/movieapp/AppComponent;", "setAppComponent", "(Lcom/raywenderlich/android/movieapp/AppComponent;)V", "initAppComponent", "app", "onCreate", "", "Companion", "app_debug"})
public final class MovieApplication extends android.app.Application {
    @org.jetbrains.annotations.NotNull()
    public com.raywenderlich.android.movieapp.AppComponent appComponent;
    @org.jetbrains.annotations.NotNull()
    private static com.raywenderlich.android.movieapp.MovieApplication application;
    public static final com.raywenderlich.android.movieapp.MovieApplication.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final com.raywenderlich.android.movieapp.AppComponent getAppComponent() {
        return null;
    }
    
    public final void setAppComponent(@org.jetbrains.annotations.NotNull()
    com.raywenderlich.android.movieapp.AppComponent p0) {
    }
    
    private final com.raywenderlich.android.movieapp.AppComponent initAppComponent(com.raywenderlich.android.movieapp.MovieApplication app) {
        return null;
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    public MovieApplication() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R&\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u00048F@BX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/raywenderlich/android/movieapp/MovieApplication$Companion;", "", "()V", "<set-?>", "Lcom/raywenderlich/android/movieapp/MovieApplication;", "application", "getApplication", "()Lcom/raywenderlich/android/movieapp/MovieApplication;", "setApplication", "(Lcom/raywenderlich/android/movieapp/MovieApplication;)V", "app_debug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final synchronized com.raywenderlich.android.movieapp.MovieApplication getApplication() {
            return null;
        }
        
        private final void setApplication(com.raywenderlich.android.movieapp.MovieApplication p0) {
        }
        
        private Companion() {
            super();
        }
    }
}