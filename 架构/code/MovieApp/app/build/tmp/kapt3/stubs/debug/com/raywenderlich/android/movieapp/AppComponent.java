package com.raywenderlich.android.movieapp;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/raywenderlich/android/movieapp/AppComponent;", "", "inject", "", "mainActivity", "Lcom/raywenderlich/android/movieapp/ui/MainActivity;", "movieListFragment", "Lcom/raywenderlich/android/movieapp/ui/movies/MovieListFragment;", "app_debug"})
@dagger.Component(modules = {com.raywenderlich.android.movieapp.AppModule.class, com.raywenderlich.android.movieapp.framework.network.MovieApiModule.class, com.raywenderlich.android.movieapp.ui.ViewModelModule.class})
@javax.inject.Singleton()
public abstract interface AppComponent {
    
    public abstract void inject(@org.jetbrains.annotations.NotNull()
    com.raywenderlich.android.movieapp.ui.MainActivity mainActivity);
    
    public abstract void inject(@org.jetbrains.annotations.NotNull()
    com.raywenderlich.android.movieapp.ui.movies.MovieListFragment movieListFragment);
}