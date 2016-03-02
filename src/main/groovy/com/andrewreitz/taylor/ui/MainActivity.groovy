package com.andrewreitz.taylor.ui

import android.app.Activity
import android.os.Bundle
import android.support.annotation.NonNull
import com.andrewreitz.taylor.R
import com.andrewreitz.taylor.data.Injector
import com.andrewreitz.taylor.data.api.IsTaylorSwiftSingleService
import com.andrewreitz.taylor.data.api.model.IsTaylorSwiftSingleData
import dagger.ObjectGraph
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

import javax.inject.Inject

@CompileStatic
class MainActivity extends Activity {

  @Inject
  @PackageScope // must be package scope so annotation processor can access it
  IsTaylorSwiftSingleService taylorSwiftSingleService

  private ObjectGraph activityGraph;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    contentView = R.layout.activity_main

    // Explicitly reference the application object since we don't want to match our own injector.
    ObjectGraph appGraph = Injector.obtain(application)
    appGraph.inject(this)
    activityGraph = appGraph.plus(new MainActivityModule(this))

    taylorSwiftSingleService.taylorSwiftData.flatMap { Observable.from(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<IsTaylorSwiftSingleData>() {
          @Override void onCompleted() {}

          @Override void onError(Throwable e) {
            Timber.e(e, 'Error loading t-swift data')
          }

          @Override void onNext(IsTaylorSwiftSingleData isTaylorSwiftSingleData) {
            Timber.d(isTaylorSwiftSingleData.toString())
          }
        })

//    SwissKnife.inject(this)
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (Injector.matchesService(name)) {
      return activityGraph
    }
    return super.getSystemService(name)
  }
}