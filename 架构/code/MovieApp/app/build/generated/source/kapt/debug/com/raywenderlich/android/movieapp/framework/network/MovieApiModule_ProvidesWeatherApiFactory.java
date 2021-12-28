package com.raywenderlich.android.movieapp.framework.network;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MovieApiModule_ProvidesWeatherApiFactory implements Factory<MovieService> {
  private final MovieApiModule module;

  private final Provider<Retrofit> retrofitProvider;

  public MovieApiModule_ProvidesWeatherApiFactory(MovieApiModule module,
      Provider<Retrofit> retrofitProvider) {
    this.module = module;
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public MovieService get() {
    return providesWeatherApi(module, retrofitProvider.get());
  }

  public static MovieApiModule_ProvidesWeatherApiFactory create(MovieApiModule module,
      Provider<Retrofit> retrofitProvider) {
    return new MovieApiModule_ProvidesWeatherApiFactory(module, retrofitProvider);
  }

  public static MovieService providesWeatherApi(MovieApiModule instance, Retrofit retrofit) {
    return Preconditions.checkNotNull(instance.providesWeatherApi(retrofit), "Cannot return null from a non-@Nullable @Provides method");
  }
}
