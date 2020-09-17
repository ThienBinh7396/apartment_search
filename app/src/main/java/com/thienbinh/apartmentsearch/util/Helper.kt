package com.thienbinh.apartmentsearch.util

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