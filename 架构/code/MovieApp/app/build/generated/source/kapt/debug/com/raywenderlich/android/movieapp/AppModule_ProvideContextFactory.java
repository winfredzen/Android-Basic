package com.raywenderlich.android.movieapp;

import android.app.Application;
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
public final class AppModule_ProvideContextFactory implements Factory<Application> {
  private final AppModule module;

  public AppModule_ProvideContextFactory(AppModule module) {
    this.module = module;
  }

  @Override
  public Application get() {
    return provideContext(module);
  }

  public static AppModule_ProvideContextFactory create(AppModule module) {
    return new AppModule_ProvideContextFactory(module);
  }

  public static Application provideContext(AppModule instance) {
    return Preconditions.checkNotNull(instance.provideContext(), "Cannot return null from a non-@Nullable @Provides method");
  }
}
