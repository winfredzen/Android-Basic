package com.raywenderlich.android.movieapp.framework.network.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b/\b\u0087\b\u0018\u00002\u00020\u0001B\u00a7\u0001\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0001\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0010\b\u0001\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007\u0012\b\u0010\t\u001a\u0004\u0018\u00010\b\u0012\n\b\u0001\u0010\n\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0001\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0001\u0010\u000f\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0001\u0010\u0010\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0011\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0001\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u0012\n\b\u0001\u0010\u0015\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\u0016J\u0010\u0010/\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0018J\u000b\u00100\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u00101\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u0010\u00102\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0018J\u0010\u00103\u001a\u0004\u0018\u00010\u0014H\u00c6\u0003\u00a2\u0006\u0002\u0010,J\u0010\u00104\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001fJ\u000b\u00105\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u0011\u00106\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007H\u00c6\u0003J\u0010\u00107\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u001fJ\u000b\u00108\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u0010\u0010;\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003\u00a2\u0006\u0002\u0010%J\u000b\u0010<\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u00bc\u0001\u0010=\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0003\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0010\b\u0003\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b2\n\b\u0003\u0010\n\u001a\u0004\u0018\u00010\u00052\n\b\u0003\u0010\u000b\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0003\u0010\u000f\u001a\u0004\u0018\u00010\u00052\n\b\u0003\u0010\u0010\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00032\n\b\u0003\u0010\u0013\u001a\u0004\u0018\u00010\u00142\n\b\u0003\u0010\u0015\u001a\u0004\u0018\u00010\bH\u00c6\u0001\u00a2\u0006\u0002\u0010>J\u0013\u0010?\u001a\u00020\u00032\b\u0010@\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010A\u001a\u00020\bH\u00d6\u0001J\t\u0010B\u001a\u00020\u0005H\u00d6\u0001R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0019\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0019\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0015\u0010\t\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010 \u001a\u0004\b\u001e\u0010\u001fR\u0013\u0010\n\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001bR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001bR\u0013\u0010\f\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001bR\u0015\u0010\r\u001a\u0004\u0018\u00010\u000e\u00a2\u0006\n\n\u0002\u0010&\u001a\u0004\b$\u0010%R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001bR\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001bR\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001bR\u0015\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0019\u001a\u0004\b*\u0010\u0018R\u0015\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u00a2\u0006\n\n\u0002\u0010-\u001a\u0004\b+\u0010,R\u0015\u0010\u0015\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010 \u001a\u0004\b.\u0010\u001f\u00a8\u0006C"}, d2 = {"Lcom/raywenderlich/android/movieapp/framework/network/model/Movie;", "", "adult", "", "backdropPath", "", "genreIds", "", "", "id", "originalLanguage", "originalTitle", "overview", "popularity", "", "posterPath", "releaseDate", "title", "video", "voteAverage", "", "voteCount", "(Ljava/lang/Boolean;Ljava/lang/String;Ljava/util/List;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Float;Ljava/lang/Integer;)V", "getAdult", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "getBackdropPath", "()Ljava/lang/String;", "getGenreIds", "()Ljava/util/List;", "getId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getOriginalLanguage", "getOriginalTitle", "getOverview", "getPopularity", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getPosterPath", "getReleaseDate", "getTitle", "getVideo", "getVoteAverage", "()Ljava/lang/Float;", "Ljava/lang/Float;", "getVoteCount", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/Boolean;Ljava/lang/String;Ljava/util/List;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Double;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Float;Ljava/lang/Integer;)Lcom/raywenderlich/android/movieapp/framework/network/model/Movie;", "equals", "other", "hashCode", "toString", "app_debug"})
@com.squareup.moshi.JsonClass(generateAdapter = true)
public final class Movie {
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Boolean adult = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String backdropPath = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<java.lang.Integer> genreIds = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer id = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String originalLanguage = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String originalTitle = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String overview = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Double popularity = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String posterPath = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String releaseDate = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String title = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Boolean video = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Float voteAverage = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer voteCount = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean getAdult() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getBackdropPath() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.Integer> getGenreIds() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOriginalLanguage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOriginalTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getOverview() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getPopularity() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPosterPath() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getReleaseDate() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTitle() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean getVideo() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Float getVoteAverage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getVoteCount() {
        return null;
    }
    
    public Movie(@org.jetbrains.annotations.Nullable()
    java.lang.Boolean adult, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "backdrop_path")
    java.lang.String backdropPath, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "genre_ids")
    java.util.List<java.lang.Integer> genreIds, @org.jetbrains.annotations.Nullable()
    java.lang.Integer id, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "original_language")
    java.lang.String originalLanguage, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "original_title")
    java.lang.String originalTitle, @org.jetbrains.annotations.Nullable()
    java.lang.String overview, @org.jetbrains.annotations.Nullable()
    java.lang.Double popularity, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "poster_path")
    java.lang.String posterPath, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "release_date")
    java.lang.String releaseDate, @org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean video, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "vote_average")
    java.lang.Float voteAverage, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "vote_count")
    java.lang.Integer voteCount) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.Integer> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double component8() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Float component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.raywenderlich.android.movieapp.framework.network.model.Movie copy(@org.jetbrains.annotations.Nullable()
    java.lang.Boolean adult, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "backdrop_path")
    java.lang.String backdropPath, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "genre_ids")
    java.util.List<java.lang.Integer> genreIds, @org.jetbrains.annotations.Nullable()
    java.lang.Integer id, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "original_language")
    java.lang.String originalLanguage, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "original_title")
    java.lang.String originalTitle, @org.jetbrains.annotations.Nullable()
    java.lang.String overview, @org.jetbrains.annotations.Nullable()
    java.lang.Double popularity, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "poster_path")
    java.lang.String posterPath, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "release_date")
    java.lang.String releaseDate, @org.jetbrains.annotations.Nullable()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean video, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "vote_average")
    java.lang.Float voteAverage, @org.jetbrains.annotations.Nullable()
    @com.squareup.moshi.Json(name = "vote_count")
    java.lang.Integer voteCount) {
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