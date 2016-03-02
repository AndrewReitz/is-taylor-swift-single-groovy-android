package com.andrewreitz.taylor.data.api

import com.andrewreitz.taylor.data.api.model.IsTaylorSwiftSingleData
import groovy.transform.CompileStatic
import retrofit2.http.GET
import rx.Observable

@CompileStatic
interface IsTaylorSwiftSingleService {
  @GET('istaylorswiftsingle/data.json')
  Observable<List<IsTaylorSwiftSingleData>> getTaylorSwiftData()
}
