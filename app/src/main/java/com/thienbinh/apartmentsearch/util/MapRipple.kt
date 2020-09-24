package com.thienbinh.apartmentsearch.util

import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.GroundOverlay
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.thienbinh.apartmentsearch.R


class MapRipple(private val mGoogleMap: GoogleMap, private var mLatLng: LatLng, context: Context) {
  private var mPrevLatLng: LatLng
  private var mBackgroundImageDescriptor: BitmapDescriptor? = null
  private var mTransparency = 0.5f

  @Volatile
  private var mDistance = 100.0
  private var mNumberOfRipples = 1
  private var mFillColor: Int = Color.TRANSPARENT
  private var mStrokeColor: Int = Color.BLACK
  private var mStrokeWidth = 5
  private var mDurationBetweenTwoRipples: Long = 1000
  private var mRippleDuration: Long = 2000
  private val mAnimators: Array<ValueAnimator?>
  private val mHandlers: Array<Handler?>
  private lateinit var mGroundOverlays: Array<GroundOverlay?>
  private val mBackground: GradientDrawable?
  var isAnimationRunning = false
    private set

  fun withTransparency(transparency: Float): MapRipple {
    mTransparency = transparency
    return this
  }

  fun withDistance(distance: Double): MapRipple {
    mDistance = distance
    return this
  }

  fun withLatLng(latLng: LatLng): MapRipple {
    mPrevLatLng = mLatLng
    mLatLng = latLng
    return this
  }

  fun withNumberOfRipples(numberOfRipples: Int): MapRipple {
    mNumberOfRipples = if (numberOfRipples > 4 || numberOfRipples < 1) {
       4
    } else numberOfRipples

    return this
  }

  fun withFillColor(fillColor: Int): MapRipple {
    mFillColor = fillColor
    return this
  }

  fun withStrokeColor(strokeColor: Int): MapRipple {
    mStrokeColor = strokeColor
    return this
  }


  fun withStrokeWidth(strokeWidth: Int): MapRipple {
    mStrokeWidth = strokeWidth
    return this
  }

  fun withDurationBetweenTwoRipples(durationBetweenTwoRipples: Long): MapRipple {
    mDurationBetweenTwoRipples = durationBetweenTwoRipples
    return this
  }

  fun withRippleDuration(rippleDuration: Long): MapRipple {
    mRippleDuration = rippleDuration
    return this
  }

  private val mCircleOneRunnable = Runnable {
    mGroundOverlays[0] = mGoogleMap.addGroundOverlay(
      GroundOverlayOptions()
        .position(mLatLng, mDistance.toFloat())
        .transparency(mTransparency)
        .image(mBackgroundImageDescriptor!!)
    )
    startAnimation(0)
  }
  private val mCircleTwoRunnable = Runnable {
    mGroundOverlays[1] = mGoogleMap.addGroundOverlay(
      GroundOverlayOptions()
        .position(mLatLng, mDistance.toFloat())
        .transparency(mTransparency)
        .image(mBackgroundImageDescriptor!!)
    )
    startAnimation(1)
  }
  private val mCircleThreeRunnable = Runnable {
    mGroundOverlays[2] = mGoogleMap.addGroundOverlay(
      GroundOverlayOptions()
        .position(mLatLng, mDistance.toFloat())
        .transparency(mTransparency)
        .image(mBackgroundImageDescriptor!!)
    )
    startAnimation(2)
  }
  private val mCircleFourRunnable = Runnable {
    mGroundOverlays[3] = mGoogleMap.addGroundOverlay(
      GroundOverlayOptions()
        .position(mLatLng, mDistance.toFloat())
        .transparency(mTransparency)
        .image(mBackgroundImageDescriptor!!)
    )
    startAnimation(3)
  }

  private fun startAnimation(numberOfRipple: Int) {
    val animator = ValueAnimator.ofInt(0, mDistance.toInt())
    animator.repeatCount = ValueAnimator.INFINITE
    animator.repeatMode = ValueAnimator.RESTART
    animator.duration = mRippleDuration
    animator.setEvaluator(IntEvaluator())
    animator.interpolator = LinearInterpolator()
    animator.addUpdateListener { valueAnimator ->
      val animated = valueAnimator.animatedValue as Int
      mGroundOverlays[numberOfRipple]!!.setDimensions(animated.toFloat())
      if (mDistance - animated <= 10) {
        if (mLatLng !== mPrevLatLng) {
          mGroundOverlays[numberOfRipple]!!.position = mLatLng
        }
      }
    }
    animator.start()
    mAnimators[numberOfRipple] = animator
  }

  private fun setDrawableAndBitmap() {
    mBackground!!.setColor(mFillColor)
    mBackground.setStroke(UiUtil.dpToPx(mStrokeWidth.toFloat()), mStrokeColor)
    mBackgroundImageDescriptor = UiUtil.drawableToBitmapDescriptor(mBackground)
  }

  fun stopRippleMapAnimation(): MapRipple {
    if (isAnimationRunning) {
      try {
        for (i in 0 until mNumberOfRipples) {
          if (i == 0) {
            mHandlers[i]?.removeCallbacks(mCircleOneRunnable)
            mAnimators[i]!!.cancel()
            mGroundOverlays[i]!!.remove()
          }
          if (i == 1) {
            mHandlers[i]?.removeCallbacks(mCircleTwoRunnable)
            mAnimators[i]!!.cancel()
            mGroundOverlays[i]!!.remove()
          }
          if (i == 2) {
            mHandlers[i]?.removeCallbacks(mCircleThreeRunnable)
            mAnimators[i]!!.cancel()
            mGroundOverlays[i]!!.remove()
          }
          if (i == 3) {
            mHandlers[i]?.removeCallbacks(mCircleFourRunnable)
            mAnimators[i]!!.cancel()
            mGroundOverlays[i]!!.remove()
          }
        }
      } catch (e: Exception) {
        //no need to handle it
      }
    }
    isAnimationRunning = false

    return this
  }

  fun startRippleMapAnimation() {
    if (!isAnimationRunning) {
      setDrawableAndBitmap()
      for (i in 0 until mNumberOfRipples) {
        if (i == 0) {
          mHandlers[i] = Handler(Looper.getMainLooper())
          mHandlers[i]?.postDelayed(mCircleOneRunnable, mDurationBetweenTwoRipples * i)
        }
        if (i == 1) {
          mHandlers[i] = Handler(Looper.getMainLooper())
          mHandlers[i]?.postDelayed(mCircleTwoRunnable, mDurationBetweenTwoRipples * i)
        }
        if (i == 2) {
          mHandlers[i] = Handler(Looper.getMainLooper())
          mHandlers[i]?.postDelayed(mCircleThreeRunnable, mDurationBetweenTwoRipples * i)
        }
        if (i == 3) {
          mHandlers[i] = Handler(Looper.getMainLooper())
          mHandlers[i]?.postDelayed(mCircleFourRunnable, mDurationBetweenTwoRipples * i)
        }
      }
    }
    isAnimationRunning = true
  }

  init {
    mPrevLatLng = mLatLng
    mBackground = ContextCompat.getDrawable(context, R.drawable.map_background) as GradientDrawable?
    mAnimators = arrayOfNulls(4)
    mHandlers = arrayOfNulls<Handler>(4)
    mGroundOverlays = arrayOfNulls(4)
  }
}