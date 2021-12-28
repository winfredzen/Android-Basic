package com.raywenderlich.android.movieapp.framework.network.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B9\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0010\b\u0001\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\n\b\u0001\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0001\u0010\b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\rJ\u0011\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0003J\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\rJD\u0010\u0015\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0010\b\u0003\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00052\n\b\u0003\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0003\u0010\b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001\u00a2\u0006\u0002\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001R\u0019\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\f\u0010\rR\u0015\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\u000f\u0010\rR\u0015\u0010\b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\u0010\u0010\r\u00a8\u0006\u001d"}, d2 = {"Lcom/raywenderlich/android/movieapp/framework/network/model/MoviesResponse;", "", "page", "", "movies", "", "Lcom/raywenderlich/android/movieapp/framework/network/model/Movie;", "totalPages", "totalResults", "(Ljava/lang/Integer;Ljava/util/List;Ljava/lang/Integer;Ljava/lang/Integer;)V", "getMovies", "()Ljava/util/List;", "getPage", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getTotalPages", "getTotalResults", "component1", "component2", "component3", "component4", "copy", "(Ljava/lang/Integer;Ljava/util/List;Ljava/lang/Integer;Ljava/lang/Integer;)Lcom/raywenderlich/android/movieapp/framework/network/model/MoviesResponse;", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
@com.squareup.moshi.JsonClass(generateAdapter = true)
public final class MoviesResponse {
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer page = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<com.raywenderlich.android.movieapp.framework.network.model.Movie> movies = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer totalPages = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer totalResults = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getPage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.raywenderlich.android.movieapp.framework.network.model.Movie> getMovies() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getTotalPages() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getTotalResults() {
        return null;
    }
    
    public MoviesResponse(@org.jetbrains.annotations.Nullable()
    java.lang.Integer page, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "results")
    java.util.List<com.raywenderlich.android.movieapp.framework.network.model.Movie> movies, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "total_pages")
    java.lang.Integer totalPages, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "total_results")
    java.lang.Integer totalResults) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.raywenderlich.android.movieapp.framework.network.model.Movie> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.raywenderlich.android.movieapp.framework.network.model.MoviesResponse copy(@org.jetbrains.annotations.Nullable()
    java.lang.Integer page, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "results")
    java.util.List<com.raywenderlich.android.movieapp.framework.network.model.Movie> movies, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "total_pages")
    java.lang.Integer totalPages, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "total_results")
    java.lang.Integer totalResults) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object p0) {
        return false;
    }
}