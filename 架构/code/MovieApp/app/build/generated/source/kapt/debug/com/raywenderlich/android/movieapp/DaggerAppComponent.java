package com.raywenderlich.android.movieapp;

import androidx.lifecycle.ViewModel;
import com.raywenderlich.android.movieapp.framework.network.MovieApiModule;
import com.raywenderlich.android.movieapp.framework.network.MovieApiModule_ProvideInterceptorFactory;
import com.raywenderlich.android.movieapp.framework.network.MovieApiModule_ProvideMoshiFactory;
import com.raywenderlich.android.movieapp.framework.network.MovieApiModule_ProvideRetrofitFactory;
import com.raywenderlich.android.movieapp.framework.network.MovieApiModule_ProvideWeatherApiClientFactory;
import com.raywenderlich.android.movieapp.framework.network.MovieApiModule_ProvidesWeatherApiFactory;
import com.raywenderlich.android.movieapp.framework.network.MovieRepository;
import com.raywenderlich.android.movieapp.framework.network.MovieRepository_Factory;
import com.raywenderlich.android.movieapp.framework.network.MovieService;
import com.raywenderlich.android.movieapp.ui.MainActivity;
import com.raywenderlich.android.movieapp.ui.MainActivity_MembersInjector;
import com.raywenderlich.android.movieapp.ui.MainViewModel;
import com.raywenderlich.android.movieapp.ui.MainViewModel_Factory;
import com.raywenderlich.android.movieapp.ui.ViewModelFactory;
import com.raywenderlich.android.movieapp.ui.ViewModelFactory_Factory;
import com.raywenderlich.android.movieapp.ui.movies.MovieListFragment;
import com.raywenderlich.android.movieapp.ui.movies.MovieListFragment_MembersInjector;
import com.squareup.moshi.Moshi;
import dagger.internal.DoubleCheck;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import java.util.Map;
import javax.annotation.Generated;
import javax.inject.Provider;
import okhttp3.Interceptor;
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
public final class DaggerAppComponent implements AppComponent {
  private Provider<Moshi> provideMoshiProvider;

  private Provider<Interceptor> provideInterceptorProvider;

  private Provider<OkHttpClient> provideWeatherApiClientProvider;

  private Provider<Retrofit> provideRetrofitProvider;

  private Provider<MovieService> providesWeatherApiProvider;

  private Provider<MovieRepository> movieRepositoryProvider;

  private Provider<MainViewModel> mainViewModelProvider;

  private Provider<Map<Class<? extends ViewModel>, Provider<ViewModel>>> mapOfClassOfAndProviderOfViewModelProvider;

  private Provider<ViewModelFactory> viewModelFactoryProvider;

  private DaggerAppComponent(MovieApiModule movieApiModuleParam) {

    initialize(movieApiModuleParam);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static AppComponent create() {
    return new Builder().build();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final MovieApiModule movieApiModuleParam) {
    this.provideMoshiProvider = DoubleCheck.provider(MovieApiModule_ProvideMoshiFactory.create(movieApiModuleParam));
    this.provideInterceptorProvider = DoubleCheck.provider(MovieApiModule_ProvideInterceptorFactory.create(movieApiModuleParam));
    this.provideWeatherApiClientProvider = DoubleCheck.provider(MovieApiModule_ProvideWeatherApiClientFactory.create(movieApiModuleParam, provideInterceptorProvider));
    this.provideRetrofitProvider = DoubleCheck.provider(MovieApiModule_ProvideRetrofitFactory.create(movieApiModuleParam, provideMoshiProvider, provideWeatherApiClientProvider));
    this.providesWeatherApiProvider = DoubleCheck.provider(MovieApiModule_ProvidesWeatherApiFactory.create(movieApiModuleParam, provideRetrofitProvider));
    this.movieRepositoryProvider = MovieRepository_Factory.create(providesWeatherApiProvider);
    this.mainViewModelProvider = MainViewModel_Factory.create(movieRepositoryProvider);
    this.mapOfClassOfAndProviderOfViewModelProvider = MapProviderFactory.<Class<? extends ViewModel>, ViewModel>builder(1).put(MainViewModel.class, (Provider) mainViewModelProvider).build();
    this.viewModelFactoryProvider = DoubleCheck.provider(ViewModelFactory_Factory.create(mapOfClassOfAndProviderOfViewModelProvider));
  }

  @Override
  public void inject(MainActivity mainActivity) {
    injectMainActivity(mainActivity);}

  @Override
  public void inject(MovieListFragment movieListFragment) {
    injectMovieListFragment(movieListFragment);}

  private MainActivity injectMainActivity(MainActivity instance) {
    MainActivity_MembersInjector.injectViewModelFactory(instance, viewModelFactoryProvider.get());
    return instance;
  }

  private MovieListFragment injectMovieListFragment(MovieListFragment instance) {
    MovieListFragment_MembersInjector.injectViewModelFactory(instance, viewModelFactoryProvider.get());
    return instance;
  }

  public static final class Builder {
    private MovieApiModule movieApiModule;

    private Builder() {
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder appModule(AppModule appModule) {
      Preconditions.checkNotNull(appModule);
      return this;
    }

    public Builder movieApiModule(MovieApiModule movieApiModule) {
      this.movieApiModule = Preconditions.checkNotNull(movieApiModule);
      return this;
    }

    public AppComponent build() {
      if (movieApiModule == null) {
        this.movieApiModule = new MovieApiModule();
      }
      return new DaggerAppComponent(movieApiModule);
    }
  }
}
