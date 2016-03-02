package com.andrewreitz.taylor

import android.app.Application
import com.andrewreitz.taylor.data.DataModule
import com.andrewreitz.taylor.ui.UiModule
import dagger.Module
import dagger.Provides
import groovy.transform.CompileStatic

import javax.inject.Singleton

@Module(
    includes = [UiModule, DataModule],
    injects = IsApp
)
@CompileStatic
final class IsModule {

  private final IsApp app

  IsModule(IsApp app) {
    this.app = app
  }

  @Provides @Singleton Application provideApplication() {
    return app
  }
}
