package com.andrewreitz.taylor

import android.app.Application
import android.os.StrictMode
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import timber.log.Timber

import javax.inject.Inject

@CompileStatic final class Initializer {

  private final Application app

  @Inject Initializer(Application app) {
    this.app = app
  }

  @PackageScope void init() {
    Stetho.initializeWithDefaults(app)

    Timber.plant(new Timber.DebugTree())
    Timber.plant(new StethoTree())

    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectAll()
        .penaltyDeath()
        .build())
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectAll()
        .penaltyDeath()
        .build());
  }
}
