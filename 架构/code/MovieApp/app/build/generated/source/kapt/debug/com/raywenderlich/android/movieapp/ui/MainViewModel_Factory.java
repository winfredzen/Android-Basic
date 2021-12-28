package com.raywenderlich.android.movieapp.ui;

import com.raywenderlich.android.movieapp.framework.network.MovieRepository;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<MovieRepository> repositoryProvider;

  public MainViewModel_Factory(Provider<MovieRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static MainViewModel_Factory create(Provider<MovieRepository> repositoryProvider) {
    return new MainViewModel_Factory(repositoryProvider);
  }

  public static MainViewModel newInstance(MovieRepository repository) {
    return new MainViewModel(repository);
  }
}
