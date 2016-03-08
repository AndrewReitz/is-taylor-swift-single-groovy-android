package com.andrewreitz.taylor.data

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import groovy.transform.CompileStatic
import okhttp3.OkHttpClient

import javax.inject.Singleton

@Module(
    complete = false,
    library = true,
    overrides = true
)
@CompileStatic
final class DebugDataModule {
  @Provides @Singleton OkHttpClient provideOkHttpClient(Application app) {
    return DataModule.createOkHttpClient(app)
        .addInterceptor(new StethoInterceptor())
        .build()
  }
}
