package com.raywenderlich.android.movieapp.ui.movies;

import androidx.lifecycle.ViewModelProvider;
import dagger.MembersInjector;
import dagger.internal.InjectedFieldSignature;
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
public final class MovieListFragment_MembersInjector implements MembersInjector<MovieListFragment> {
  private final Provider<ViewModelProvider.Factory> viewModelFactoryProvider;

  public MovieListFragment_MembersInjector(
      Provider<ViewModelProvider.Factory> viewModelFactoryProvider) {
    this.viewModelFactoryProvider = viewModelFactoryProvider;
  }

  public static MembersInjector<MovieListFragment> create(
      Provider<ViewModelProvider.Factory> viewModelFactoryProvider) {
    return new MovieListFragment_MembersInjector(viewModelFactoryProvider);}

  @Override
  public void injectMembers(MovieListFragment instance) {
    injectViewModelFactory(instance, viewModelFactoryProvider.get());
  }

  @InjectedFieldSignature("com.raywenderlich.android.movieapp.ui.movies.MovieListFragment.viewModelFactory")
  public static void injectViewModelFactory(MovieListFragment instance,
      ViewModelProvider.Factory viewModelFactory) {
    instance.viewModelFactory = viewModelFactory;
  }
}
