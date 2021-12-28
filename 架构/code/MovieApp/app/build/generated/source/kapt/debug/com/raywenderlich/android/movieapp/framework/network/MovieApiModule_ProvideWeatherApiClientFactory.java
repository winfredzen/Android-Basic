package com.raywenderlich.android.movieapp.framework.network;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MovieApiModule_ProvideWeatherApiClientFactory implements Factory<OkHttpClient> {
  private final MovieApiModule module;

  private final Provider<Interceptor> authInterceptorProvider;

  public MovieApiModule_ProvideWeatherApiClientFactory(MovieApiModule module,
      Provider<Interceptor> authInterceptorProvider) {
    this.module = module;
    this.authInterceptorProvider = authInterceptorProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideWeatherApiClient(module, authInterceptorProvider.get());
  }

  public static MovieApiModule_ProvideWeatherApiClientFactory create(MovieApiModule module,
      Provider<Interceptor> authInterceptorProvider) {
    return new MovieApiModule_ProvideWeatherApiClientFactory(module, authInterceptorProvider);
  }

  public static OkHttpClient provideWeatherApiClient(MovieApiModule instance,
      Interceptor authInterceptor) {
    return Preconditions.checkNotNull(instance.provideWeatherApiClient(authInterceptor), "Cannot return null from a non-@Nullable @Provides method");
  }
}
