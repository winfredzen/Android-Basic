package com.raywenderlich.android.movieapp.framework.network;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\b\u0001\u0010\u0006\u001a\u00020\u0007H\'J\u0014\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'\u00a8\u0006\t"}, d2 = {"Lcom/raywenderlich/android/movieapp/framework/network/MovieService;", "", "fetchMovieByQueryAsync", "Lkotlinx/coroutines/Deferred;", "Lretrofit2/Response;", "Lcom/raywenderlich/android/movieapp/framework/network/model/MoviesResponse;", "query", "", "fetchPopularMoviesAsync", "app_debug"})
public abstract interface MovieService {
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "movie/popular")
    public abstract kotlinx.coroutines.Deferred<retrofit2.Response<com.raywenderlich.android.movieapp.framework.network.model.MoviesResponse>> fetchPopularMoviesAsync();
    
    @org.jetbrains.annotations.NotNull()
    @retrofit2.http.GET(value = "search/movie")
    public abstract kotlinx.coroutines.Deferred<retrofit2.Response<com.raywenderlich.android.movieapp.framework.network.model.MoviesResponse>> fetchMovieByQueryAsync(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Query(value = "query")
    java.lang.String query);
}