package com.andrewreitz.taylor.ui

import dagger.Module
import groovy.transform.CompileStatic

@Module(
    injects = [
      MainActivity
    ],
    complete = false,
    library = true
)
@CompileStatic
class UiModule {
}
