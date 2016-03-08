package com.andrewreitz.taylor.ui

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.widget.TextView

class LinkTextView extends TextView {
  public LinkTextView(Context context, AttributeSet attrs) {
    super(context, attrs)
    movementMethod = LinkMovementMethod.instance
  }
}
