package com.raywenderlich.android.movieapp.framework.network;

import com.squareup.moshi.Moshi;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MovieApiModule_ProvideRetrofitFactory implements Factory<Retrofit> {
  private final MovieApiModule module;

  private final Provider<Moshi> moshiProvider;

  private final Provider<OkHttpClient> randomUserApiClientProvider;

  public MovieApiModule_ProvideRetrofitFactory(MovieApiModule module, Provider<Moshi> moshiProvider,
      Provider<OkHttpClient> randomUserApiClientProvider) {
    this.module = module;
    this.moshiProvider = moshiProvider;
    this.randomUserApiClientProvider = randomUserApiClientProvider;
  }

  @Override
  public Retrofit get() {
    return provideRetrofit(module, moshiProvider.get(), randomUserApiClientProvider.get());
  }

  public static MovieApiModule_ProvideRetrofitFactory create(MovieApiModule module,
      Provider<Moshi> moshiProvider, Provider<OkHttpClient> randomUserApiClientProvider) {
    return new MovieApiModule_ProvideRetrofitFactory(module, moshiProvider, randomUserApiClientProvider);
  }

  public static Retrofit provideRetrofit(MovieApiModule instance, Moshi moshi,
      OkHttpClient randomUserApiClient) {
    return Preconditions.checkNotNull(instance.provideRetrofit(moshi, randomUserApiClient), "Cannot return null from a non-@Nullable @Provides method");
  }
}
