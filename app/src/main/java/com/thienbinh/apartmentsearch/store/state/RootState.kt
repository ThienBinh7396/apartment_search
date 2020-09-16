package com.thienbinh.apartmentsearch.store.state

import org.rekotlin.StateType

data class RootState(
  var apartmentState: ApartmentState? = null
): StateType