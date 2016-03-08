package com.andrewreitz.taylor

import groovy.transform.CompileStatic

@CompileStatic abstract class Modules {
  static Object[] list(IsApp app) {
    return [
      new IsModule(app)
    ].toArray()
  }
}
