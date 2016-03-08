package com.andrewreitz.taylor

import android.app.Application
import android.support.annotation.NonNull
import android.support.multidex.MultiDex
import com.andrewreitz.taylor.data.Injector
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import dagger.ObjectGraph
import groovy.transform.CompileStatic

import javax.inject.Inject

@CompileStatic class IsApp extends Application {

  @Inject Initializer initializer

  private ObjectGraph objectGraph

  @Override void onCreate() {
    super.onCreate()

    objectGraph = ObjectGraph.create(Modules.list(this))
    objectGraph.inject(this)

    // todo move to debug init since shouldn't be needed in release
    MultiDex.install(this)
    AndroidThreeTen.init(this)
    LeakCanary.install(this)

    initializer.init()
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return objectGraph
    }
    return super.getSystemService(name)
  }
}
