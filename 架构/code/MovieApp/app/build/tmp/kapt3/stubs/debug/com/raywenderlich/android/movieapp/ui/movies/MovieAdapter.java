package com.raywenderlich.android.movieapp.ui.movies;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\u0018\u0000 \u001c2\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0003\u001c\u001d\u001eB\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u001c\u0010\u0011\u001a\u00020\u00122\n\u0010\u0013\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u0010H\u0016J\u001c\u0010\u0015\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0010H\u0016J\u0016\u0010\u0019\u001a\u00020\u00122\u000e\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u001bR\u0016\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\t\u001a\u00020\n8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u001f"}, d2 = {"Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter$MovieViewHolder;", "moviesClickListener", "Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter$MoviesClickListener;", "(Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter$MoviesClickListener;)V", "data", "", "Lcom/raywenderlich/android/movieapp/framework/network/model/Movie;", "requestOptions", "Lcom/bumptech/glide/request/RequestOptions;", "getRequestOptions", "()Lcom/bumptech/glide/request/RequestOptions;", "requestOptions$delegate", "Lkotlin/Lazy;", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updateData", "newData", "", "Companion", "MovieViewHolder", "MoviesClickListener", "app_debug"})
public final class MovieAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.raywenderlich.android.movieapp.ui.movies.MovieAdapter.MovieViewHolder> {
    private java.util.List<com.raywenderlich.android.movieapp.framework.network.model.Movie> data;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy requestOptions$delegate = null;
    private final com.raywenderlich.android.movieapp.ui.movies.MovieAdapter.MoviesClickListener moviesClickListener = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String POSTER_IMAGE_PATH_PREFIX = "https://image.tmdb.org/t/p/w300";
    public static final com.raywenderlich.android.movieapp.ui.movies.MovieAdapter.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public final com.bumptech.glide.request.RequestOptions getRequestOptions() {
        return null;
    }
    
    public final void updateData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.raywenderlich.android.movieapp.framework.network.model.Movie> newData) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.raywenderlich.android.movieapp.ui.movies.MovieAdapter.MovieViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.raywenderlich.android.movieapp.ui.movies.MovieAdapter.MovieViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public MovieAdapter(@org.jetbrains.annotations.NotNull()
    com.raywenderlich.android.movieapp.ui.movies.MovieAdapter.MoviesClickListener moviesClickListener) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter$MoviesClickListener;", "", "onMovieClicked", "", "movie", "Lcom/raywenderlich/android/movieapp/framework/network/model/Movie;", "app_debug"})
    public static abstract interface MoviesClickListener {
        
        public abstract void onMovieClicked(@org.jetbrains.annotations.NotNull()
        com.raywenderlich.android.movieapp.framework.network.model.Movie movie);
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a8\u0006\t"}, d2 = {"Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter$MovieViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter;Landroid/view/View;)V", "bind", "", "movie", "Lcom/raywenderlich/android/movieapp/framework/network/model/Movie;", "app_debug"})
    public final class MovieViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        
        public final void bind(@org.jetbrains.annotations.Nullable()
        com.raywenderlich.android.movieapp.framework.network.model.Movie movie) {
        }
        
        public MovieViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/raywenderlich/android/movieapp/ui/movies/MovieAdapter$Companion;", "", "()V", "POSTER_IMAGE_PATH_PREFIX", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}