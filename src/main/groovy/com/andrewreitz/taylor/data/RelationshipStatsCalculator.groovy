package com.andrewreitz.taylor.data

import com.andrewreitz.taylor.data.api.model.IsTaylorSwiftSingleData
import groovy.transform.CompileStatic
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit

import javax.inject.Inject

@CompileStatic
class RelationshipStatsCalculator {

  @Inject RelationshipStatsCalculator() {

  }

  Number calculateAverageRelationshipLengthInDays(List<IsTaylorSwiftSingleData> data) {
    def value = data.inject(0) { def length, IsTaylorSwiftSingleData it ->
      def begin = it.dated[0]
      def end = it.dated[1] ?: Instant.now()
      return length + Duration.between(begin, end).toDays()
    } as BigDecimal

    return value / data.size()
  }

  Number calculateAverageLengthBetweenRelationshipInDays(List<IsTaylorSwiftSingleData> data) {
    def averageTimeBetweenRelationships = 0 as BigDecimal
    (1..data.size()-1).each { int i ->
      def previousPerson = data[i-1]
      def person = data[i]
      def startOfBetweenTime = previousPerson.dated[1]
      def endOfBetweenTime = person.dated[0]

      def span = Duration.between(startOfBetweenTime, endOfBetweenTime).toDays()

      averageTimeBetweenRelationships += span
    }

    averageTimeBetweenRelationships /= (data.size()-1)

    return averageTimeBetweenRelationships
  }

  Number calculateAverageCompanionAgeInYears(List<IsTaylorSwiftSingleData> data) {
    def averageAge = data.inject(0) { def age, IsTaylorSwiftSingleData it ->
      return age + Duration.between(it.dateOfBirth, it.dated[0]).toMillis()
    } as BigDecimal

    averageAge /= data.size()
    averageAge /= ChronoUnit.YEARS.duration.toMillis()

    return averageAge
  }

  Number numberOfSongsWrittenAboutCompanions(List<IsTaylorSwiftSingleData> data) {
    return data.inject(0) { int songCount, IsTaylorSwiftSingleData it ->
      return songCount + it.songs.size()
    } as Number
  }

  Number numberOfSongsWrittenPerCompanion(List<IsTaylorSwiftSingleData> data) {
    return numberOfSongsWrittenAboutCompanions(data) / data.size()
  }
}
