package com.andrewreitz.taylor

import android.app.Application
import com.crashlytics.android.Crashlytics
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import io.fabric.sdk.android.Fabric

import javax.inject.Inject

@CompileStatic final class Initializer {

  private final Application app

  @Inject Initializer(Application app) {
    this.app = app
  }

  @PackageScope void init() {
    Fabric.with(app, new Crashlytics())
  }
}
