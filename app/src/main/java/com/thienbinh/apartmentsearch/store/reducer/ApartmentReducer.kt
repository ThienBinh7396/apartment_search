package com.thienbinh.apartmentsearch.store.reducer

import android.util.Log
import com.thienbinh.apartmentsearch.db.entities.deepCloneApartmentTypeList
import com.thienbinh.apartmentsearch.store.action.ApartmentAction
import com.thienbinh.apartmentsearch.store.state.ApartmentState
import com.thienbinh.apartmentsearch.util.gson
import org.rekotlin.Action

fun apartmentReducer(action: Action, apartmentState: ApartmentState?): ApartmentState {
  var _apartmentState = apartmentState ?: ApartmentState()

  when (action) {
    is ApartmentAction.Apartment_ACTION_UPDATE_APARTMENTS -> {
      Log.d("Binh", "Apartment")

      _apartmentState = _apartmentState.copy(
        apartments = action.apartments
      )
    }

    is ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_AMENITIES -> {
      Log.d("Binh", "Apartment amenity")

      _apartmentState = _apartmentState.copy(
        apartmentAmenities = action.list
      )
    }

    is ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_TYPES -> {
      _apartmentState = _apartmentState.copy(
        apartmentTypes = action.list
      )
    }

    is ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_FILTER -> {
      _apartmentState.apartmentFilterModel.apply {
        _apartmentState = _apartmentState.copy(
          apartmentFilterModel = this.copy(
            adultsAmount = action.adults ?: adultsAmount,
            childrenAmount = action.children ?: childrenAmount,
            infantsAmount = action.infants ?: infantsAmount,
          )
        )
      }
    }

    is ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_FILTER_UPDATE_DATE -> {
      _apartmentState.apartmentFilterModel.apply {
        _apartmentState = _apartmentState.copy(
          apartmentFilterModel = this.copy(
            startDate = action.startDate,
            endDate = action.endDate
          )
        )
      }
    }
  }

  return _apartmentState
}