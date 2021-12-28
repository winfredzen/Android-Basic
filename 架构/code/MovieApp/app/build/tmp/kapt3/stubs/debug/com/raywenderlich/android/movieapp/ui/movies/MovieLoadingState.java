package com.raywenderlich.android.movieapp.ui.movies;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/raywenderlich/android/movieapp/ui/movies/MovieLoadingState;", "", "(Ljava/lang/String;I)V", "LOADING", "ERROR", "LOADED", "INVALID_API_KEY", "app_debug"})
public enum MovieLoadingState {
    /*public static final*/ LOADING /* = new LOADING() */,
    /*public static final*/ ERROR /* = new ERROR() */,
    /*public static final*/ LOADED /* = new LOADED() */,
    /*public static final*/ INVALID_API_KEY /* = new INVALID_API_KEY() */;
    
    MovieLoadingState() {
    }
}