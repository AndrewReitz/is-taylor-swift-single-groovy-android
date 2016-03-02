package com.andrewreitz.taylor.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import groovy.transform.CompileStatic
import org.threeten.bp.Instant

@CompileStatic
final class InstantAdapter {
  @ToJson public String toJson(Instant instant) {
    return instant.toString()
  }

  @FromJson public Instant fromJson(String value) {
    return Instant.parse(value)
  }
}
