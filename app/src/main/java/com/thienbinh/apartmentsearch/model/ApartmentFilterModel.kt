package com.thienbinh.apartmentsearch.model

import android.util.Log
import java.io.Serializable
import java.util.*

data class ApartmentFilterModel(
  var startDate: Date? = Calendar.getInstance().time,
  var endDate: Date? = Calendar.getInstance().time,
  var adultsAmount: Int = 2,
  var childrenAmount: Int = 0,
  var infantsAmount: Int = 0
) : Serializable

val checkApartmentFilterModelAreTheSame =
  { objectOne: ApartmentFilterModel, objectTwo: ApartmentFilterModel ->
    run {
      return@run objectOne.startDate != null && objectOne.endDate != null &&
          objectTwo.startDate != null && objectTwo.endDate != null &&
          objectOne.startDate!!.compareTo(objectTwo.startDate) == 0 &&
          objectOne.endDate!!.compareTo(objectTwo.endDate) == 0 &&
          objectOne.adultsAmount == objectTwo.adultsAmount &&
          objectOne.childrenAmount == objectTwo.childrenAmount &&
          objectOne.infantsAmount == objectTwo.infantsAmount
    }

  }