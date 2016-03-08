package com.andrewreitz.taylor.data.api

import com.andrewreitz.taylor.data.api.model.IsTaylorSwiftSingleData
import groovy.transform.CompileStatic
import retrofit2.adapter.rxjava.Result
import retrofit2.http.GET
import retrofit2.http.Headers
import rx.Observable

@CompileStatic interface IsTaylorSwiftSingleService {

  @Headers('Accept: application/json')
  @GET('istaylorswiftsingle/data.json')
  Observable<Result<List<IsTaylorSwiftSingleData>>> getTaylorSwiftData()
}
