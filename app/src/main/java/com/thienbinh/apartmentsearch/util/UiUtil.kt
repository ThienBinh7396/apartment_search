package com.thienbinh.apartmentsearch.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlin.math.ceil


object UiUtil {
  fun drawableToBitmapDescriptor(drawable: Drawable): BitmapDescriptor {
    if (drawable is BitmapDrawable) {
      if (drawable.bitmap != null) {
        return BitmapDescriptorFactory.fromBitmap(drawable.bitmap)
      }
    }
    val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
      Bitmap.createBitmap(
        1,
        1,
        Bitmap.Config.ARGB_8888
      ) // Single color bitmap will be created of 1x1 pixel
    } else {
      Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
      )
    }
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
  }

  fun dpToPx(dp: Float): Int {
    val density: Float = Resources.getSystem().displayMetrics.density
    return ceil(dp * density.toDouble()).toInt()
  }
}