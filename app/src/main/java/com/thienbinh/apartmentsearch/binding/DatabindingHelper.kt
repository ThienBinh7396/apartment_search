package com.thienbinh.apartmentsearch.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.ptrstovka.calendarview2.CalendarDay
import com.ptrstovka.calendarview2.CalendarView2
import com.ptrstovka.calendarview2.format.ArrayWeekDayFormatter
import com.ptrstovka.calendarview2.format.MonthArrayTitleFormatter
import com.stfalcon.pricerangebar.RangeBarWithChart
import com.stfalcon.pricerangebar.model.BarEntry
import com.thienbinh.apartmentsearch.GlideApp
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.adapter.recycleView.SortTypeAdapter
import com.thienbinh.apartmentsearch.adapter.recycleView.ApartmentAdapter
import com.thienbinh.apartmentsearch.adapter.recycleView.ApartmentTypeAdapter
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.db.entities.ApartmentType
import com.thienbinh.apartmentsearch.model.customInterface.IApartmentAdapterEventListener
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.action.ApartmentAction
import com.thienbinh.apartmentsearch.ui.activity.MainActivity
import com.thienbinh.apartmentsearch.ui.customView.WidgetInputNumber
import com.thienbinh.apartmentsearch.util.RecyclerViewTouchListener
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

    val mapUrlWithBitmap = mutableMapOf<String, Drawable>()

    @BindingAdapter("app:bindImageSrcWithCenterCrop")
    @JvmStatic
    fun bindImageSrcWithCenterCrop(imageView: ImageView, src: Any? = null) {
      if (src != null) {
        GlideApp.with(imageView.context)
          .load(src)
          .centerCrop()
          .into(imageView)
      }
    }

    @BindingAdapter("app:bindImageSrcWithCacheToMap")
    @JvmStatic
    fun bindImageSrcWithCacheToMap(imageView: ImageView, src: Any? = null) {
      if (src != null) {
        GlideApp.with(imageView.context)
          .load(src)
          .centerCrop()
          .listener(object : RequestListener<Drawable>{
            override fun onLoadFailed(
              e: GlideException?,
              model: Any?,
              target: Target<Drawable>?,
              isFirstResource: Boolean
            ): Boolean {

              Log.d("Binh", "Drawable faild: ${e?.message}")
              return false
            }

            override fun onResourceReady(
              resource: Drawable?,
              model: Any?,
              target: Target<Drawable>?,
              dataSource: DataSource?,
              isFirstResource: Boolean
            ): Boolean {
              Log.d("Binh", "Drawable: $resource")

              if (mapUrlWithBitmap[src.toString()] == null && resource != null){
                mapUrlWithBitmap[src.toString()] = resource
              }

              return false
            }

          })
          .into(imageView)
      }
    }

    @BindingAdapter("app:bindShowUnless")
    @JvmStatic
    fun bindShowUnless(view: View, isShow: Boolean?) {
      view.visibility = if (isShow != null && isShow) View.VISIBLE else View.GONE
    }


    @BindingAdapter("app:bindEntriesRangeBar")
    @JvmStatic
    fun bindEntriesRangeBar(seekBar: RangeBarWithChart, someThing: Any?) {
      val barEntrys = ArrayList<BarEntry>()

      val array = arrayOf(
        22.0f to 0f,
        25.0f to 0f,
        28.0f to 0f,
        30.0f to 0f,
        32.0f to 0.0f,
        35.0f to 1.0f,
        38.0f to 2.0f,
        40.0f to 2.0f,
        42.0f to 3.0f,
        45.0f to 5.0f,
        48.0f to 7.0f,
        50.0f to 10.0f,
        52.0f to 10.0f,
        55.0f to 11.0f,
        58.0f to 12.0f,
        60.0f to 15.0f,
        62.0f to 19.0f,
        65.0f to 19.0f,
        68.0f to 18.0f,
        70.0f to 19.0f,
        72.0f to 20.0f,
        75.0f to 23.0f,
        78.0f to 24.0f,
        80.0f to 23.0f,
        82.0f to 23.0f,
        85.0f to 26.0f,
        88.0f to 26.0f,
        90.0f to 29.0f,
        92.0f to 29.0f,
        95.0f to 30.0f,
        98.0f to 30.0f,
        100.0f to 32.0f,
        102.0f to 32.0f,
        105.0f to 38.0f,
        108.0f to 38.0f,
        110.0f to 40.0f,
        112.0f to 40.0f,
        115.0f to 37.0f,
        118.0f to 37.0f,
        120.0f to 37.0f,
        122.0f to 37.0f,
        125.0f to 33.0f,
        128.0f to 33.0f,
        130.0f to 28.0f,
        132.0f to 28.0f,
        135.0f to 23.0f,
        138.0f to 23.0f,
        140.0f to 22.0f,
        142.0f to 22.0f,
        145.0f to 17.0f,
        148.0f to 17.0f,
        150.0f to 16.0f,
        152.0f to 14.0f,
        155.0f to 13.0f,
        158.0f to 11.0f,
        160.0f to 10.0f,
        162.0f to 9.0f,
        165.0f to 9.0f,
        168.0f to 8.0f,
        170.0f to 7.0f,
        172.0f to 7.0f,
        175.0f to 8.0f,
        178.0f to 6.0f,
        180.0f to 6.0f,
        182.0f to 5.0f,
        185.0f to 5.0f,
        188.0f to 6.0f,
        190.0f to 6.0f,
        192.0f to 4.0f,
        195.0f to 2.0f,
        198.0f to 2.0f,
        200.0f to 3.0f,
        202.0f to 4.0f,
        205.0f to 3.0f,
        208.0f to 2.0f,
        210.0f to 0.0f,
        212.0f to 0.0f,
        215.0f to 0.0f,
        218.0f to 0.0f,
        220.0f to 0.0f,
        222.0f to 0.0f
      ).map { BarEntry(it.first, it.second) }

      barEntrys.addAll(array)

      seekBar.setEntries(barEntrys)

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

    @BindingAdapter("app:bindHideView")
    @JvmStatic
    fun bindHideView(view: View, isShow: Boolean?) {
      view.visibility = if (isShow != null && isShow) View.VISIBLE else View.INVISIBLE
    }

    @BindingAdapter("app:bindApartmentList")
    @JvmStatic
    fun bindApartmentList(rcv: RecyclerView, list: MutableList<Apartment>?) {
      var adapter = rcv.adapter ?: ApartmentAdapter(object : IApartmentAdapterEventListener {
        override fun onGotoDetailEventListener(apartment: Apartment, imageView: ImageView) {
          val transitionName = imageView.transitionName

          val extras = FragmentNavigatorExtras(
            imageView to transitionName
          )

          Log.d("Binh", "Transition: $transitionName")

          rcv.findNavController().navigate(R.id.action_homeFragment_to_apartmentDetailFragment , bundleOf(
            "apartment" to apartment,
            "transitionName" to transitionName
          ), null , extras)
        }
      })

      if (rcv.adapter == null) {
        rcv.adapter = adapter

        rcv.layoutManager = GridLayoutManager(rcv.context, 1, GridLayoutManager.VERTICAL, false)
      }


      Log.d("Binh", "List: ${list?.size}")
      if (list != null) {

        (adapter as ApartmentAdapter).updateApartmentList(list)
      }
    }

    @BindingAdapter("app:bindListSortType")
    @JvmStatic
    fun bindListSortType(rcv: RecyclerView, someThing: Any?) {
      if (rcv.adapter == null) {
        val adapter = SortTypeAdapter(rcv.context)

        rcv.adapter = adapter

        rcv.layoutManager = GridLayoutManager(rcv.context, 1, GridLayoutManager.HORIZONTAL, false)

        rcv.addOnItemTouchListener(
          RecyclerViewTouchListener(
            rcv.context,
            rcv,
            object : RecyclerViewTouchListener.ClickListener {
              override fun onClick(view: View?, position: Int) {
                SortTypeAdapter.setActiveAtPosition(position)

                adapter.updateListType()
              }

              override fun onLongClick(view: View?, position: Int) {
              }
            })
        )
      }
    }

    @BindingAdapter("app:bindApartmentStyle")
    @JvmStatic
    fun bindApartmentStyle(rcv: RecyclerView, list: MutableList<ApartmentType>?) {
      val adapter = rcv.adapter ?: ApartmentTypeAdapter()

      if (rcv.adapter == null) {
        rcv.adapter = adapter

        rcv.layoutManager = GridLayoutManager(rcv.context, 1, GridLayoutManager.VERTICAL, false)

        rcv.addOnItemTouchListener(
          RecyclerViewTouchListener(
            rcv.context,
            rcv,
            object : RecyclerViewTouchListener.ClickListener {
              override fun onClick(view: View?, position: Int) {
                store.dispatch(ApartmentAction.APARTMENT_ACTION_ACTIVE_APARTMENT_TYPE_AT(position))
              }

              override fun onLongClick(view: View?, position: Int) {
              }
            })
        )
      }

      (adapter as ApartmentTypeAdapter).updateList(list ?: mutableListOf())
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