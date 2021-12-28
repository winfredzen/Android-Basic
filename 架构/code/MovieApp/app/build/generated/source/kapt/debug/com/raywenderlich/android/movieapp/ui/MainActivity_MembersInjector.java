package com.raywenderlich.android.movieapp.ui;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<ViewModelProvider.Factory> viewModelFactoryProvider;

  public MainActivity_MembersInjector(
      Provider<ViewModelProvider.Factory> viewModelFactoryProvider) {
    this.viewModelFactoryProvider = viewModelFactoryProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<ViewModelProvider.Factory> viewModelFactoryProvider) {
    return new MainActivity_MembersInjector(viewModelFactoryProvider);}

  @Override
  public void injectMembers(MainActivity instance) {
    injectViewModelFactory(instance, viewModelFactoryProvider.get());
  }

  @InjectedFieldSignature("com.raywenderlich.android.movieapp.ui.MainActivity.viewModelFactory")
  public static void injectViewModelFactory(MainActivity instance,
      ViewModelProvider.Factory viewModelFactory) {
    instance.viewModelFactory = viewModelFactory;
  }
}
