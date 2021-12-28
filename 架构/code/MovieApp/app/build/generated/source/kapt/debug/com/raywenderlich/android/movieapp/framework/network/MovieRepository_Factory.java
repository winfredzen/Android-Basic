package com.raywenderlich.android.movieapp.framework.network;

import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MovieRepository_Factory implements Factory<MovieRepository> {
  private final Provider<MovieService> movieServiceProvider;

  public MovieRepository_Factory(Provider<MovieService> movieServiceProvider) {
    this.movieServiceProvider = movieServiceProvider;
  }

  @Override
  public MovieRepository get() {
    return newInstance(movieServiceProvider.get());
  }

  public static MovieRepository_Factory create(Provider<MovieService> movieServiceProvider) {
    return new MovieRepository_Factory(movieServiceProvider);
  }

  public static MovieRepository newInstance(MovieService movieService) {
    return new MovieRepository(movieService);
  }
}
