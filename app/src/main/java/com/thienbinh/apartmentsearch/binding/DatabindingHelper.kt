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
//import com.google.android.material.datepicker.MaterialDatePicker
//import com.prolificinteractive.materialcalendarview.CalendarDay
//import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.ptrstovka.calendarview2.CalendarView2
import com.ptrstovka.calendarview2.CalendarView2.SELECTION_MODE_RANGE
import com.thienbinh.apartmentsearch.GlideApp
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.adapter.ApartmentAdapter
import com.thienbinh.apartmentsearch.db.entities.Apartment
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
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
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
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        tv.text = Html.fromHtml(
          "<font color='#626b80'><b>$num night</b></font> stay",
          Html.FROM_HTML_MODE_COMPACT
        )
      } else {
        tv.text = Html.fromHtml("<font color='#626b80'><b>$num night</b></font> stay")
      }
    }

//    @BindingAdapter("app:bindCustomArrow")
//    @JvmStatic
//    fun bindCustomArrow(datePicker: MaterialCalendarView, someThing: Any? = null) {
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        datePicker.leftArrow.colorFilter = BlendModeColorFilter(
//          datePicker.resources.getColor(R.color.colorTextActive, null),
//          BlendMode.SRC_ATOP
//        )
//
//        datePicker.rightArrow.colorFilter = BlendModeColorFilter(
//          datePicker.resources.getColor(R.color.colorTextActive, null),
//          BlendMode.SRC_ATOP
//        )
//      } else {
//        datePicker.leftArrow.setColorFilter(
//          datePicker.resources.getColor(R.color.colorTextActive),
//          PorterDuff.Mode.SRC_ATOP
//        )
//        datePicker.rightArrow.setColorFilter(
//          datePicker.resources.getColor(R.color.colorTextActive),
//          PorterDuff.Mode.SRC_ATOP
//        )
//      }
//    }
//
//
//    @BindingAdapter("app:bindMinDateNow")
//    @JvmStatic
//    fun bindMinDateNow(datePicker: MaterialCalendarView, someThing: Any? = null) {
//      datePicker.state().edit().setMinimumDate(CalendarDay.today()).commit()
//    }
//
//    @BindingAdapter("app:bindMaxDateNow")
//    @JvmStatic
//    fun bindMaxDateNow(datePicker: MaterialCalendarView, someThing: Any? = null) {
//      datePicker.state().edit().setMaximumDate(
//        CalendarDay.from(
//          nextDay.yearOfEra().get(),
//          nextDay.monthOfYear().get(),
//          nextDay.dayOfMonth().get()
//        )
//      ).commit()
//    }


    @BindingAdapter("app:bindCustomArrow2")
    @JvmStatic
    fun bindCustomArrow2(datePicker: CalendarView2, someThing: Any? = null) {
      datePicker.selectionMode = SELECTION_MODE_RANGE

      if (Build.VERSION.SDK_INT >= 23) {
        datePicker.arrowColor = datePicker.resources.getColor(R.color.colorTextActive, null)
      } else {
        datePicker.arrowColor = datePicker.resources.getColor(R.color.colorTextActive)
      }
    }


    @BindingAdapter("app:bindMinDateNow2")
    @JvmStatic
    fun bindMinDateNow2(datePicker: CalendarView2, someThing: Any? = null) {
      datePicker.state().edit().setMinimumDate(Calendar.getInstance().time).commit()
    }

    @BindingAdapter("app:bindMaxDateNow2")
    @JvmStatic
    fun bindMaxDateNow2(datePicker: CalendarView2, someThing: Any? = null) {
      datePicker.state().edit().setMaximumDate(
        com.ptrstovka.calendarview2.CalendarDay.from(
          nextDay.yearOfEra().get(),
          nextDay.monthOfYear().get(),
          nextDay.dayOfMonth().get()
        )
      ).commit()
    }


  }
}