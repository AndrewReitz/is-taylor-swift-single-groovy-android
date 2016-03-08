package com.andrewreitz.groovy.runtime

import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.adapter.rxjava.Result
import spock.lang.Specification

class ResultExtensionsSpec extends Specification {

  def "success should return true"() {
    given:
    def request_ = new Request.Builder().with {
      url('http://example.com')
      build()
    }

    def rawResponse = new okhttp3.Response.Builder().with {
      request(request_)
      protocol(Protocol.HTTP_1_0)
      code(200)
      build()
    }

    def response = Response.success(null, rawResponse)

    def result = Result.response(response)

    expect:
    result.success
  }

  def "success should return false"() {
    given:
    def responseBody = Mock(ResponseBody)
    def response = Response.error(500, responseBody)
    def result = Result.response(response)

    expect:
    !result.success
  }
}