package com.thienbinh.apartmentsearch.util

import android.content.Context
import android.graphics.drawable.Drawable
import com.ptrstovka.calendarview2.CalendarDay
import com.ptrstovka.calendarview2.DayViewDecorator
import com.ptrstovka.calendarview2.DayViewFacade
import com.thienbinh.apartmentsearch.R


class RangeDayDecorator(context: Context) : DayViewDecorator() {
  private val list: HashSet<CalendarDay> = HashSet()
  private val drawable: Drawable = context.resources.getDrawable(R.drawable.custom_datepicker_drawable_background)

  override fun shouldDecorate(day: CalendarDay): Boolean {
    return list.contains(day)
  }

  override fun decorate(view: DayViewFacade) {
    view.setSelectionDrawable(drawable)
  }

  fun addFirstAndLast(first: CalendarDay, last: CalendarDay) {
    list.clear()
    list.add(first)
    list.add(last)
  }

}
