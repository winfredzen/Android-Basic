package com.raywenderlich.android.movieapp.framework.network;

import com.squareup.moshi.Moshi;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MovieApiModule_ProvideMoshiFactory implements Factory<Moshi> {
  private final MovieApiModule module;

  public MovieApiModule_ProvideMoshiFactory(MovieApiModule module) {
    this.module = module;
  }

  @Override
  public Moshi get() {
    return provideMoshi(module);
  }

  public static MovieApiModule_ProvideMoshiFactory create(MovieApiModule module) {
    return new MovieApiModule_ProvideMoshiFactory(module);
  }

  public static Moshi provideMoshi(MovieApiModule instance) {
    return Preconditions.checkNotNull(instance.provideMoshi(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
