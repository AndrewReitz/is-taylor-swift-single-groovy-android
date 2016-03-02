package com.andrewreitz.taylor.ui

import dagger.Module
import groovy.transform.CompileStatic

@Module(
)
@CompileStatic
public final class MainActivityModule {
  private final MainActivity mainActivity

  MainActivityModule(MainActivity mainActivity) {
    this.mainActivity = mainActivity
  }
}
