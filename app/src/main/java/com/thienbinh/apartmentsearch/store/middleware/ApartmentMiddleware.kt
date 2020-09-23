package com.thienbinh.apartmentsearch.store.middleware

import android.util.Log
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.action.ApartmentAction
import com.thienbinh.apartmentsearch.store.state.ApartmentState
import com.thienbinh.apartmentsearch.store.state.RootState
import org.rekotlin.Action
import org.rekotlin.DispatchFunction
import org.rekotlin.Middleware

internal val apartmentMiddleware: Middleware<RootState> = { dispatch, _ ->
  { next ->
    { action ->
      run {
        when (action) {
          is ApartmentAction.APARTMENT_ACTION_ACTIVE_APARTMENT_TYPE_AT -> {
            activeApartmentTypeAt(dispatch, action.position)
          }
        }

        next(action)
      }
    }
  }
}

fun activeApartmentTypeAt(dispatchFunction: DispatchFunction, position: Int) {
  store.state.apartmentState.apply {

    if (position >= apartmentTypes.size) return

    apartmentTypes[position].isActive = !apartmentTypes[position].isActive
  }
}