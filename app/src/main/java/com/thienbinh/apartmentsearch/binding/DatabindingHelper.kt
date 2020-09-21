package com.thienbinh.apartmentsearch.binding

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.transition.Transition
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ptrstovka.calendarview2.CalendarDay
import com.ptrstovka.calendarview2.CalendarView2
import com.ptrstovka.calendarview2.CalendarView2.SELECTION_MODE_RANGE
import com.ptrstovka.calendarview2.format.ArrayWeekDayFormatter
import com.ptrstovka.calendarview2.format.MonthArrayTitleFormatter
import com.thienbinh.apartmentsearch.GlideApp
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.adapter.ApartmentAdapter
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.action.ApartmentAction
import com.thienbinh.apartmentsearch.ui.customView.WidgetInputNumber
import com.thienbinh.apartmentsearch.util.Helper
import com.thienbinh.apartmentsearch.util.RangeDayDecorator
import com.thienbinh.apartmentsearch.util.SCALE_DP_PX
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.joda.time.DateTime
import java.util.*

class DataBindingHelper {
  companion object {
    val today = Calendar.getInstance()
    var nextDay = DateTime().plusDays(30)

    @BindingAdapter("app:bindImageSrc")
    @JvmStatic
    fun bindImageSrc(imageView: ImageView, src: Any? = null) {
      if (src != null) {
        GlideApp.with(imageView.context)
          .load(src)
          .fitCenter()
          .into(imageView)
      }
    }

    @BindingAdapter("app:bindTopLeftRadiusImageSrc")
    @JvmStatic
    fun bindTopLeftRadiusImageSrc(imageView: ImageView, src: Any? = null) {
      if (src != null) {
        Log.d("Binh", "Parent: ${imageView.parent.requestLayout()}")

        Glide.with(imageView.context)
          .asBitmap()
          .load(src)
          .centerCrop()
          .transform(
            RoundedCornersTransformation(
              28 * SCALE_DP_PX.toInt(),
              0,
              RoundedCornersTransformation.CornerType.TOP_LEFT
            )
          )
          .into(imageView)

        imageView.parent.requestLayout()
      }
    }

    @BindingAdapter("app:bindShowUnless")
    @JvmStatic
    fun bindShowUnless(view: View, isShow: Boolean?) {
      view.visibility = if (isShow != null && isShow) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:bindHideView")
    @JvmStatic
    fun bindHideView(view: View, isShow: Boolean?) {
      view.visibility = if (isShow != null && isShow) View.VISIBLE else View.INVISIBLE
    }

    @BindingAdapter("app:bindApartmentList")
    @JvmStatic
    fun bindApartmentList(rcv: RecyclerView, list: MutableList<Apartment>?) {
      var adapter = rcv.adapter ?: ApartmentAdapter()

      if (rcv.adapter == null) {
        rcv.adapter = adapter

        rcv.layoutManager = GridLayoutManager(rcv.context, 1, GridLayoutManager.VERTICAL, false)
      }


      Log.d("Binh", "List: ${list?.size}")
      if (list != null) {

        (adapter as ApartmentAdapter).updateApartmentList(list)
      }
    }

    @BindingAdapter("app:bindResultFilter")
    @JvmStatic
    fun bindResultFilter(tv: TextView, num: Int? = null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        tv.text = Html.fromHtml(
          "<font color='#626b80'><b>$num</b></font> results found",
          Html.FROM_HTML_MODE_COMPACT
        )
      } else {
        tv.text = Html.fromHtml("<font color='#626b80'><b>$num</b></font> results found")
      }
    }

    @BindingAdapter("app:bindDayResult")
    @JvmStatic
    fun bindDayResult(tv: TextView, num: Int? = null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        tv.text = Html.fromHtml(
          "<font color='#626b80'><b>$num night</b></font> stay",
          Html.FROM_HTML_MODE_COMPACT
        )
      } else {
        tv.text = Html.fromHtml("<font color='#626b80'><b>$num night</b></font> stay")
      }
    }

    @BindingAdapter(
      value = ["app:bindCustomizeCalendarView", "app:bindSelectionMode"],
      requireAll = false
    )
    @JvmStatic
    fun bindCustomizeCalendarView(
      datePicker: CalendarView2,
      someThing: Any?,
      selectionMode: Int? = 3
    ) {
      if (datePicker.tag == false) {

        datePicker.selectionMode = selectionMode!!

        datePicker.setTitleFormatter(
          MonthArrayTitleFormatter(
            datePicker.context.resources.getTextArray(
              R.array.custom_months
            )
          )
        )

        datePicker.setWeekDayFormatter(
          ArrayWeekDayFormatter(
            datePicker.context.resources.getTextArray(
              R.array.custom_weekdays
            )
          )
        )

        datePicker.tag = true

        datePicker.state().edit()
          .isCacheCalendarPositionEnabled(true)
          .commit()

        datePicker.setOnDateChangedListener { widget, date, selected ->
          run {
            store.dispatch(
              ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_FILTER_UPDATE_DATE(
                startDate = if (selected) date.date else null,
                endDate = null
              )
            )

            Log.d("Binh", "Date: ${date.day} $selected")
          }
        }

        datePicker.setOnRangeSelectedListener { widget, dates ->
          Log.d("Binh", "Date: ${dates.size}")

          if (dates.size >= 2) {
            store.dispatch(
              ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_FILTER_UPDATE_DATE(
                startDate = dates[0].date,
                endDate = dates[dates.size - 1].date
              )
            )
            Log.d("Binh", "Date start: ${dates[0].day} ${dates[dates.size - 1].day}}")
          } else {
            ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_FILTER_UPDATE_DATE(
              startDate = null,
              endDate = null
            )
          }
        }

        store.state.apartmentState.apartmentFilterModel.apply {

          if (startDate != null && endDate != null && startDate!!.compareTo(endDate) != 0) {
            datePicker.selectRange(CalendarDay.from(startDate), CalendarDay.from(endDate))

            datePicker.invalidateDecorators()
          }
        }
      }

      Log.d("Binh", "Datepicker: ${datePicker.tag}")
    }

    @BindingAdapter("app:bindMinDateNow")
    @JvmStatic
    fun bindMinDateNow(datePicker: CalendarView2, someThing: Any? = null) {
      datePicker.state().edit().setMinimumDate(Calendar.getInstance().time).commit()
    }

    @BindingAdapter("app:bindMaxDateNow")
    @JvmStatic
    fun bindMaxDateNow(datePicker: CalendarView2, someThing: Any? = null) {
      Log.d(
        "Binh",
        "Binh, ${nextDay.yearOfEra().get()} ${nextDay.monthOfYear().get()} ${
          nextDay.dayOfMonth().get()
        }"
      )

      datePicker.state().edit().setMaximumDate(
        CalendarDay.from(
          nextDay.yearOfEra().get(),
          nextDay.monthOfYear().get() - 1,
          nextDay.dayOfMonth().get()
        )
      ).commit()
    }

    @BindingAdapter("app:bindToggleEnabled")
    fun bindToggleEnabled(view: View, isEnabled: Boolean) {
      view.isEnabled = isEnabled
    }

    @BindingAdapter("app:bindNumberValueToWidgetInput")
    @JvmStatic
    fun bindNumberValueToWidgetInput(widget: WidgetInputNumber, num: Int?) {
      widget.updateNumberValue(num ?: 0)
    }
  }
}