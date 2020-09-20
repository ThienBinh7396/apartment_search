package com.thienbinh.apartmentsearch.util

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.ptrstovka.calendarview2.CalendarDay
import com.ptrstovka.calendarview2.DayViewDecorator
import com.ptrstovka.calendarview2.DayViewFacade
import java.time.DayOfWeek
import java.util.*


class HighlightWeekendsDecorator : DayViewDecorator() {
  private val highlightDrawable: Drawable
  @RequiresApi(Build.VERSION_CODES.O)
  override fun shouldDecorate(day: CalendarDay): Boolean {
    val weekDay = day.date.day
    return weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY
  }

  override fun decorate(view: DayViewFacade) {
    view.setBackgroundDrawable(highlightDrawable)
  }

  companion object {
    private val color: Int = android.graphics.Color.parseColor("#228BC34A")
  }

  init {
    highlightDrawable = ColorDrawable(color)
  }
}