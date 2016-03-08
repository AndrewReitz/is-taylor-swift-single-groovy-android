package com.andrewreitz.taylor

import com.andrewreitz.taylor.data.DebugDataModule
import dagger.Module
import groovy.transform.CompileStatic

@Module(
    addsTo = IsModule,
    includes = [
      DebugDataModule
    ],
    overrides = true
)
@CompileStatic
class DebugIsModule {
}
