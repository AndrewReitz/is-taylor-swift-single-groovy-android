package com.andrewreitz.taylor.data

import android.app.Application
import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import com.andrewreitz.taylor.EyesImage
import com.andrewreitz.taylor.data.api.ApiModule
import com.andrewreitz.taylor.data.api.IsTaylorSwiftSingleService
import com.f2prateek.rx.preferences.RxSharedPreferences
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.moshi.Moshi
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import groovy.transform.CompileStatic
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import timber.log.Timber

import javax.inject.Singleton

import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES

@Module(
    includes = ApiModule,
    complete = false,
    library = true
)
@CompileStatic
class DataModule {
  static final int DISK_CACHE_SIZE = MEGABYTES.toBytes(50) as int

  @Provides @Singleton @EyesImage String provideEyesImageUrl() {
    return 'http://peterhajas.com/istaylorswiftsingle/tayloreyes.png'
  }

  @Provides @Singleton SharedPreferences provideSharedPreferences(Application app) {
    return PreferenceManager.getDefaultSharedPreferences(app)
  }

  @Provides @Singleton RxSharedPreferences provideRxSharedPreferences(SharedPreferences prefs) {
    return RxSharedPreferences.create(prefs)
  }

  @Provides @Singleton Moshi provideMoshi() {
    return new Moshi.Builder()
        .add(new InstantAdapter())
        .build()
  }

  @Provides @Singleton IsTaylorSwiftSingleService provideIsTaylorSwiftSingleService(
      Retrofit retrofit) {
    return retrofit.create(IsTaylorSwiftSingleService)
  }

  @Provides @Singleton OkHttpClient provideOkHttpClient(Application app) {
    return createOkHttpClient(app).build()
  }

  @Provides @Singleton Picasso providePicasso(Application app, OkHttpClient client) {
    return new Picasso.Builder(app)
        .downloader(new OkHttp3Downloader(client))
        .listener { Picasso picasso, Uri uri, Exception e ->
            Timber.e(e, "Failed to load image: $uri")
        }.build()
  }

  static OkHttpClient.Builder createOkHttpClient(Application app) {
    // Install an HTTP cache in the application cache directory.
    File cacheDir = new File(app.getCacheDir(), 'http')
    Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE)

    return new OkHttpClient.Builder()
        .cache(cache)
  }
}
