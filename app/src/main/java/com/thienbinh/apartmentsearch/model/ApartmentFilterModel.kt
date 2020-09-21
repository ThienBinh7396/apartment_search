package com.thienbinh.apartmentsearch.model

import android.util.Log
import java.io.Serializable
import java.util.*

data class ApartmentFilterModel(
  var startDate: Date? = null,
  var endDate: Date? = null,
  var adultsAmount: Int = 2,
  var childrenAmount: Int = 0,
  var infantsAmount: Int = 0
) : Serializable

fun compareDateWithNull(a: Date?, b: Date?) = a?.equals(b) ?: (b === null)

val checkApartmentFilterModelAreTheSame =
  { objectOne: ApartmentFilterModel, objectTwo: ApartmentFilterModel ->
    run {
      return@run ((compareDateWithNull(objectOne.startDate, objectTwo.startDate)
          && compareDateWithNull(objectOne.endDate, objectTwo.endDate)) ||
          (objectOne.startDate != null && objectOne.endDate != null &&
              objectTwo.startDate != null && objectTwo.endDate != null &&
              objectOne.startDate!!.compareTo(objectTwo.startDate) == 0 &&
              objectOne.endDate!!.compareTo(objectTwo.endDate) == 0)) &&
          objectOne.adultsAmount == objectTwo.adultsAmount &&
          objectOne.childrenAmount == objectTwo.childrenAmount &&
          objectOne.infantsAmount == objectTwo.infantsAmount
    }

  }