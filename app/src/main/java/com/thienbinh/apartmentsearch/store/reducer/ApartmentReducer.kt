package com.thienbinh.apartmentsearch.store.reducer

import android.util.Log
import com.thienbinh.apartmentsearch.store.action.ApartmentAction
import com.thienbinh.apartmentsearch.store.state.ApartmentState
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
      Log.d("Binh", "Apartment type")

      _apartmentState = _apartmentState.copy(
        apartmentTypes = action.list
      )
    }
  }

  return _apartmentState
}