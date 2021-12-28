package com.raywenderlich.android.movieapp.ui;

import androidx.lifecycle.ViewModel;
import dagger.internal.Factory;
import java.util.Map;
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
public final class ViewModelFactory_Factory implements Factory<ViewModelFactory> {
  private final Provider<Map<Class<? extends ViewModel>, Provider<ViewModel>>> viewModelsProvider;

  public ViewModelFactory_Factory(
      Provider<Map<Class<? extends ViewModel>, Provider<ViewModel>>> viewModelsProvider) {
    this.viewModelsProvider = viewModelsProvider;
  }

  @Override
  public ViewModelFactory get() {
    return newInstance(viewModelsProvider.get());
  }

  public static ViewModelFactory_Factory create(
      Provider<Map<Class<? extends ViewModel>, Provider<ViewModel>>> viewModelsProvider) {
    return new ViewModelFactory_Factory(viewModelsProvider);
  }

  public static ViewModelFactory newInstance(
      Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels) {
    return new ViewModelFactory(viewModels);
  }
}
