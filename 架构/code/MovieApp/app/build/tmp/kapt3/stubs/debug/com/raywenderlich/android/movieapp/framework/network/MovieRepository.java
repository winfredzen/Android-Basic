package com.raywenderlich.android.movieapp.framework.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J!\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00062\u0006\u0010\b\u001a\u00020\tH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0006H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\r"}, d2 = {"Lcom/raywenderlich/android/movieapp/framework/network/MovieRepository;", "", "movieService", "Lcom/raywenderlich/android/movieapp/framework/network/MovieService;", "(Lcom/raywenderlich/android/movieapp/framework/network/MovieService;)V", "fetchMovieByQuery", "", "Lcom/raywenderlich/android/movieapp/framework/network/model/Movie;", "queryText", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchPopularMovies", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class MovieRepository {
    private final com.raywenderlich.android.movieapp.framework.network.MovieService movieService = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchPopularMovies(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.raywenderlich.android.movieapp.framework.network.model.Movie>> p0) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchMovieByQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String queryText, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.raywenderlich.android.movieapp.framework.network.model.Movie>> p1) {
        return null;
    }
    
    @javax.inject.Inject()
    public MovieRepository(@org.jetbrains.annotations.NotNull()
    com.raywenderlich.android.movieapp.framework.network.MovieService movieService) {
        super();
    }
}