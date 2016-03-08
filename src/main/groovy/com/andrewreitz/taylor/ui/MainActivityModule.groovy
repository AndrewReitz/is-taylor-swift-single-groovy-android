package com.andrewreitz.taylor.ui

import com.andrewreitz.taylor.IsModule
import com.andrewreitz.taylor.ui.single.IsSingleView
import dagger.Module
import groovy.transform.CompileStatic

@Module(
    addsTo = IsModule,
    injects = [IsSingleView]
)
@CompileStatic
public final class MainActivityModule {
  private final MainActivity mainActivity

  MainActivityModule(MainActivity mainActivity) {
    this.mainActivity = mainActivity
  }
}
