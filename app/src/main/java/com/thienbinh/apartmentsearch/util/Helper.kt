package com.thienbinh.apartmentsearch.util

import android.R
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.TextView
import com.thienbinh.apartmentsearch.model.enum.ETypeDateFormat
import org.joda.time.DateTime
import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToInt


class Helper {
  companion object {
    fun Float.roundTo(n: Int): Float {
      return "%.${n}f".format(Locale.US, this).toFloat()
    }

    fun TextView.setTextWithHtml(html: String) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(
          html,
          Html.FROM_HTML_MODE_COMPACT
        )
      } else {
        this.text = Html.fromHtml(html)
      }
    }

    @JvmStatic
    fun getColorWithAlpha(color: Int, ratio: Float): Int {
      val alpha = (Color.alpha(color) * ratio).roundToInt()
      val r: Int = Color.red(color)
      val g: Int = Color.green(color)
      val b: Int = Color.blue(color)
      return Color.argb(alpha, r, g, b)
    }

    private fun getDecimalFromNumber(num: Int): String {
      var decimalString = ""

      (0 until num).forEach { decimalString += "#" }

      return decimalString
    }

    @JvmStatic
    fun formatDecimal(num: Float, numDecimal: Int = 1): String? {
      if (num.rem(1).equals(0.0)) return "${num.toInt()}"

      val simpleDecimalFormat = DecimalFormat("#.${getDecimalFromNumber(numDecimal)}")
      return simpleDecimalFormat.format(num)
    }

    val dayOfWeeks = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val monOfYears = arrayListOf(
      "Jan",
      "Feb",
      "Mar",
      "Apr",
      "May",
      "Jub",
      "Jul",
      "Aug",
      "Sep",
      "Oct",
      "Nov",
      "Dec"
    )

    @JvmStatic
    fun formatDate(date: Date?, type: ETypeDateFormat = ETypeDateFormat.TYPE_DAY_DATE): String {
      if (date == null) return "_,__"

      val jodaDateTime = DateTime(date)
      val day = jodaDateTime.dayOfWeek().get()
      val date = jodaDateTime.dayOfMonth().get()
      val month = jodaDateTime.monthOfYear().get()
      val year = jodaDateTime.year().get()

      Log.d("Binh", "Date: $day $date $month")

      return when (type) {
        ETypeDateFormat.TYPE_DAY_DATE ->
          "${dayOfWeeks[day - 1]}, ${monOfYears[month - 1]} $date"

      }
    }

    fun <T> MutableList<T>.deepCloneList(clazz: Class<Array<T>>): MutableList<T> {
      return gson.fromJson(gson.toJson(this), clazz).toMutableList()
    }

    @JvmStatic
    fun <T> checkListAreTheSame(
      listOne: MutableList<T>,
      listTwo: MutableList<T>,
      compareElement: (T, T) -> Boolean
    ): Boolean {
      if (listOne.size != listTwo.size) return false

      var checkListTheSame = true

      listOne.forEachIndexed { index, elementOne ->
        run {
          val check = compareElement(elementOne, listTwo[index])

          if (!check) {
            checkListTheSame = false

            return@forEachIndexed
          }
        }
      }

      return checkListTheSame
    }

  }
}