package com.thienbinh.apartmentsearch.store.reducer

import com.thienbinh.apartmentsearch.store.state.RootState
import org.rekotlin.Action

fun rootReducer(action: Action, rootState: RootState?): RootState =
  RootState(
    apartmentState = apartmentReducer(action, rootState?.apartmentState)
  )