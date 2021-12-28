package com.raywenderlich.android.movieapp.framework.network;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import okhttp3.Interceptor;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MovieApiModule_ProvideInterceptorFactory implements Factory<Interceptor> {
  private final MovieApiModule module;

  public MovieApiModule_ProvideInterceptorFactory(MovieApiModule module) {
    this.module = module;
  }

  @Override
  public Interceptor get() {
    return provideInterceptor(module);
  }

  public static MovieApiModule_ProvideInterceptorFactory create(MovieApiModule module) {
    return new MovieApiModule_ProvideInterceptorFactory(module);
  }

  public static Interceptor provideInterceptor(MovieApiModule instance) {
    return Preconditions.checkNotNull(instance.provideInterceptor(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
