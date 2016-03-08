package com.andrewreitz.taylor.data.api.model

import com.squareup.moshi.Json
import groovy.transform.CompileStatic
import groovy.transform.Immutable
import groovy.transform.ToString
import org.threeten.bp.Instant

@Immutable(knownImmutableClasses = [Instant])
@ToString(includeNames = true)
@CompileStatic
class IsTaylorSwiftSingleData {
  final String name

  @Json(name = 'DOB')
  final Instant dateOfBirth

  @Json(name = 'Dated')
  final List<Instant> dated

  @Json(name = 'Sources')
  final List<String> sources

  @Json(name = 'Songs')
  final Map<String, String> songs
}
