package com.andrewreitz.taylor.data.api

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import groovy.transform.CompileStatic
import javax.inject.Singleton
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module(complete = false,
    library = true)
@CompileStatic
class ApiModule {
  public static final HttpUrl PRODUCTION_API_URL = HttpUrl.parse('https://pieces029.github.io')

  @Provides
  @Singleton
  IsTaylorSwiftSingleService provideIsTaylorSwiftSingleService(Retrofit retrofit) {
    return retrofit.create(IsTaylorSwiftSingleService)
  }

  @Provides @Singleton HttpUrl provideBaseUrl() {
    return PRODUCTION_API_URL;
  }

  @Provides @Singleton Retrofit provideRetrofit(HttpUrl baseUrl, OkHttpClient client,
      Moshi moshi) {
    return new Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build()
  }
}
