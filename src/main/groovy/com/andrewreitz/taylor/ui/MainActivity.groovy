package com.andrewreitz.taylor.ui

import android.app.Activity
import android.os.Bundle
import android.support.annotation.NonNull
import android.view.ViewGroup
import com.andrewreitz.taylor.R
import com.andrewreitz.taylor.data.Injector
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import dagger.ObjectGraph
import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends Activity {

  @InjectView(R.id.main_content) ViewGroup content

  private ObjectGraph activityGraph;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    contentView = R.layout.activity_main

    SwissKnife.inject(this)

    // Explicitly reference the application object since we don't want to match our own injector.
    ObjectGraph appGraph = Injector.obtain(application)
    appGraph.inject(this)
    activityGraph = appGraph.plus(new MainActivityModule(this))

    layoutInflater.inflate(R.layout.view_is_single, content)
  }

  @Override protected void onDestroy() {
    activityGraph = null
    super.onDestroy()
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return activityGraph
    }
    return super.getSystemService(name)
  }
}