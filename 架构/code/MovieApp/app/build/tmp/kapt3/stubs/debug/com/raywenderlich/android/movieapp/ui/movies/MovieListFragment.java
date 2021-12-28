package com.raywenderlich.android.movieapp.ui.movies;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u0013H\u0002J\u0012\u0010\u0015\u001a\u00020\u00132\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J\u0012\u0010\u0018\u001a\u00020\u00132\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J\u0010\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u0010\u0010\u001c\u001a\u00020\u00132\u0006\u0010\u001d\u001a\u00020\u001eH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\f\u001a\u00020\r8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001f"}, d2 = {"Lcom/raywenderlich/android/movieapp/ui/movies/MovieListFragment;", "Landroidx/fragment/app/Fragment;", "Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter$MoviesClickListener;", "()V", "connectivityLiveData", "Lcom/raywenderlich/android/movieapp/connectivity/ConnectivityLiveData;", "mainViewModel", "Lcom/raywenderlich/android/movieapp/ui/MainViewModel;", "movieAdapter", "Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter;", "searchTextWatcher", "Landroid/text/TextWatcher;", "viewModelFactory", "Landroidx/lifecycle/ViewModelProvider$Factory;", "getViewModelFactory", "()Landroidx/lifecycle/ViewModelProvider$Factory;", "setViewModelFactory", "(Landroidx/lifecycle/ViewModelProvider$Factory;)V", "initialiseObservers", "", "initialiseUIElements", "onActivityCreated", "savedInstanceState", "Landroid/os/Bundle;", "onCreate", "onMovieClicked", "movie", "Lcom/raywenderlich/android/movieapp/framework/network/model/Movie;", "onMovieLoadingStateChanged", "state", "Lcom/raywenderlich/android/movieapp/ui/movies/MovieLoadingState;", "app_debug"})
public final class MovieListFragment extends androidx.fragment.app.Fragment implements com.raywenderlich.android.movieapp.ui.movies.MovieAdapter.MoviesClickListener {
    private com.raywenderlich.android.movieapp.ui.MainViewModel mainViewModel;
    private com.raywenderlich.android.movieapp.ui.movies.MovieAdapter movieAdapter;
    private com.raywenderlich.android.movieapp.connectivity.ConnectivityLiveData connectivityLiveData;
    @org.jetbrains.annotations.NotNull()
    @javax.inject.Inject()
    public androidx.lifecycle.ViewModelProvider.Factory viewModelFactory;
    private final android.text.TextWatcher searchTextWatcher = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.ViewModelProvider.Factory getViewModelFactory() {
        return null;
    }
    
    public final void setViewModelFactory(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.ViewModelProvider.Factory p0) {
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onActivityCreated(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initialiseObservers() {
    }
    
    private final void initialiseUIElements() {
    }
    
    @java.lang.Override()
    public void onMovieClicked(@org.jetbrains.annotations.NotNull()
    com.raywenderlich.android.movieapp.framework.network.model.Movie movie) {
    }
    
    private final void onMovieLoadingStateChanged(com.raywenderlich.android.movieapp.ui.movies.MovieLoadingState state) {
    }
    
    public MovieListFragment() {
        super();
    }
}