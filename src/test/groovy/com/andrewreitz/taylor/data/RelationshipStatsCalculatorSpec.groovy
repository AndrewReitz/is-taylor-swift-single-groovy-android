package com.andrewreitz.taylor.data

import com.andrewreitz.taylor.data.api.model.IsTaylorSwiftSingleData
import groovy.transform.CompileDynamic
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.zone.TzdbZoneRulesProvider
import org.threeten.bp.zone.ZoneRulesProvider
import spock.lang.Specification

import static org.threeten.bp.temporal.ChronoUnit.DAYS

// Be explicit, but also the compiler (using 'groovy-compile-options.groovy') is set to
// compile statically, so tell it to not do that here.
@CompileDynamic
class RelationshipStatsCalculatorSpec extends Specification {

  def relationshipCalculator = new RelationshipStatsCalculator()

  def setupSpec() {
    ZoneRulesProvider.registerProvider(new TzdbZoneRulesProvider())
  }

  def "Should calculate days between first and last relationship"() {
    given:
    def inputData = data.collect {
      new IsTaylorSwiftSingleData(dated: it)
    }

    expect:
    relationshipCalculator.calculateAverageLengthBetweenRelationshipInDays(inputData) == expected

    where:
    data                                                                                                     | expected
    [[null, getInstantXDaysAgo(5)], [Instant.now(), null]]                                                   | 5
    [[null, getInstantXDaysAgo(10)], [getInstantXDaysAgo(5), getInstantXDaysAgo(5)], [Instant.now(), null]]  | 5
    [[null, getInstantXDaysAgo(11)], [getInstantXDaysAgo(10), getInstantXDaysAgo(5)], [Instant.now(), null]] | 3
  }

  def "Should calculate average relationship length in days"() {
    given:
    def inputData = data.collect {
      new IsTaylorSwiftSingleData(dated: it)
    }

    expect:
    relationshipCalculator.calculateAverageRelationshipLengthInDays(inputData) == expected

    where:
    data                                                                                       | expected
    [[getInstantXDaysAgo(5), Instant.now()]]                                                   | 5
    [[getInstantXDaysAgo(10), getInstantXDaysAgo(5)], [getInstantXDaysAgo(5), Instant.now()]]  | 5
    [[getInstantXDaysAgo(11), getInstantXDaysAgo(10)], [getInstantXDaysAgo(5), Instant.now()]] | 3
  }

  def "Should calculate average age of companions"() {
    given:
    def inputData = data.collect {
      new IsTaylorSwiftSingleData(it)
    }

    expect:
    // down cast to int to remove small decimals
    relationshipCalculator.calculateAverageCompanionAgeInYears(inputData) as int == expected

    where:
    data                                                                                                                                                                                             | expected
    [[dated: [Instant.now()], dateOfBirth: getInstantXYearsAgo(10)]]                                                                                                                                 | 10
    [[dated: [Instant.now()], dateOfBirth: getInstantXYearsAgo(25)], [dated: [Instant.now()], dateOfBirth: getInstantXYearsAgo(25)]]                                                                 | 25
    [[dated: [Instant.now()], dateOfBirth: getInstantXYearsAgo(10)], [dated: [Instant.now()], dateOfBirth: getInstantXYearsAgo(12)], [dated: [Instant.now()], dateOfBirth: getInstantXYearsAgo(22)]] | 14
  }

  Instant getInstantXDaysAgo(int x) {
    return Instant.now().minus(x, DAYS)
  }

  Instant getInstantXYearsAgo(int x) {
    return ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).minusYears(x).toInstant()
  }
}
