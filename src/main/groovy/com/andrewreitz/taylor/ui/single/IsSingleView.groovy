package com.andrewreitz.taylor.ui.single

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.andrewreitz.taylor.EyesImage
import com.andrewreitz.taylor.R
import com.andrewreitz.taylor.data.Injector
import com.andrewreitz.taylor.data.RelationshipStatsCalculator
import com.andrewreitz.taylor.data.api.IsTaylorSwiftSingleService
import com.andrewreitz.taylor.data.api.model.IsTaylorSwiftSingleData
import com.andrewreitz.velcro.betterviewanimator.BetterViewAnimator
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.squareup.picasso.Picasso
import groovy.transform.CompileStatic
import org.threeten.bp.Instant
import retrofit2.adapter.rxjava.Result
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

import javax.inject.Inject

import static org.threeten.bp.temporal.ChronoUnit.DAYS

@CompileStatic
class IsSingleView extends LinearLayout {

  @InjectView(R.id.is_single_animator) BetterViewAnimator animator
  @InjectView(R.id.is_single_status) TextView isSingleStatus
  @InjectView(R.id.is_single_eyes_image) ImageView eyesImage
  @InjectView(R.id.is_single_number_of_days) TextView numberOfDays
  @InjectView(R.id.is_single_interested_stats) TextView interestingData

  @Inject IsTaylorSwiftSingleService taylorSwiftSingleService
  @Inject Picasso picasso
  @Inject @EyesImage String eyesImageUrl
  @Inject RelationshipStatsCalculator relationshipStatsCalculator

  private final CompositeSubscription subscriptions = new CompositeSubscription()

  IsSingleView(Context context, AttributeSet attrs) {
    super(context, attrs)
    if (!inEditMode) {
      Injector.obtain(context).inject(this)
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate()
    SwissKnife.inject(this)
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow()

    if (!inEditMode) {
      picasso.load(eyesImageUrl)
          .fit()
          .into(eyesImage)

      def result = taylorSwiftSingleService.taylorSwiftData
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .share()

      subscriptions.add(
        result.filter { it.isSuccess() } /* note can not use success, must use method call. */
            .map { it.response().body() }
            .subscribe { List<IsTaylorSwiftSingleData> data ->
              animator.displayedChildId = R.id.is_single_content

              def currentRelationShip = data.find { it.dated[1] == null }

              if (currentRelationShip) {
                isSingleStatus.text = resources.getString(R.string.is_single_no)

                def startOfRelationShip = currentRelationShip.dated[0]
                def daysDated = startOfRelationShip.until(Instant.now(), DAYS)
                def numberOfRelationShips = data.size()

                numberOfDays.text = resources.getString(R.string.days_since_single, daysDated, numberOfRelationShips)
              } else {
                isSingleStatus.text = resources.getString(R.string.is_single_yes)

                def lastRelationship = data.inject { max, it -> it.dated[1] > max.dated[1] ? it : max }
                def daysSinceLastRelationship = lastRelationship.dated[1].until(Instant.now(), DAYS)

                numberOfDays.text = resources.getString(R.string.days_single, daysSinceLastRelationship, lastRelationship.name)
              }

              interestingData.text = resources.getString(
                  R.string.in_case_you_were_interested_data,
                  relationshipStatsCalculator.calculateAverageRelationshipLengthInDays(data),
                  relationshipStatsCalculator.calculateAverageLengthBetweenRelationshipInDays(data),
                  relationshipStatsCalculator.calculateAverageCompanionAgeInYears(data),
                  relationshipStatsCalculator.numberOfSongsWrittenAboutCompanions(data),
                  relationshipStatsCalculator.numberOfSongsWrittenPerCompanion(data)
              )
            }
      )

      // Handle Errors
      subscriptions.add(
          result.filter { !it.isSuccess() }
              .subscribe { Result errorResult ->
                  if (errorResult.error) {
                    Timber.e(errorResult.error(), 'Failed to get result');
                  } else {
                    def response = errorResult.response()
                    Timber.e("Failed to get result. Server returned ${response.code()}")
                  }
                  animator.displayedChildId = R.id.is_single_error
              }
      )
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow()
    subscriptions.unsubscribe()
  }
}
