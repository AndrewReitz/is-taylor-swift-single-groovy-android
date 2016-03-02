package com.andrewreitz.taylor

import android.app.Application
import android.support.annotation.NonNull
import com.andrewreitz.taylor.data.Injector
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import dagger.ObjectGraph
import groovy.transform.CompileStatic
import timber.log.Timber

@CompileStatic class IsApp extends Application {
  private ObjectGraph objectGraph

  @Override void onCreate() {
    super.onCreate()

    AndroidThreeTen.init(this)
    LeakCanary.install(this)

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree())
    } else {
      // Release tree like crashlytics go here.
    }

    objectGraph = ObjectGraph.create(Modules.list(this))
    objectGraph.inject(this);
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return objectGraph
    }
    return super.getSystemService(name)
  }
}
