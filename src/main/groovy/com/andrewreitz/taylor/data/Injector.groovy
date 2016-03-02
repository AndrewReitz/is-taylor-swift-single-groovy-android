package com.andrewreitz.taylor.data

import android.content.Context
import dagger.ObjectGraph
import groovy.transform.CompileStatic

@CompileStatic
abstract class Injector {
  private static final String INJECTOR_SERVICE = 'com.andrewreitz.taylor.injector'

  @SuppressWarnings(['ResourceType', 'WrongConstant']) // Explicitly doing a custom service.
  public static ObjectGraph obtain(Context context) {
    return context.getSystemService(INJECTOR_SERVICE) as ObjectGraph
  }

  public static boolean matchesService(String name) {
    return INJECTOR_SERVICE == name
  }
}
