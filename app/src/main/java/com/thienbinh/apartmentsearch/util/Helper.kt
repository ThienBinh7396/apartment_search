package com.thienbinh.apartmentsearch.util

import android.util.Log
import com.thienbinh.apartmentsearch.model.enum.ETypeDateFormat
import org.joda.time.DateTime
import java.util.Date
import java.text.DecimalFormat

class Helper {
  companion object {
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