package com.andrewreitz.groovy.runtime

import groovy.transform.CompileStatic
import retrofit2.adapter.rxjava.Result

/**
 * Extensions methods for {@link Result}
 */
@CompileStatic abstract class ResultExtensions {
  /**
   * Checks if the result is successful. Successful is true if there is no error and the response
   * was successful.
   *
   * @param self the result.
   * @return true if the result was successful false otherwise.
   */
  static boolean isSuccess(final Result self) {
    return !self.isError() && self.response().isSuccess()
  }
}
