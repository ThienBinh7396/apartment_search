package com.thienbinh.apartmentsearch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WidgetInputNumberViewModel(number: Int): ViewModel() {
  val numberValue = MutableLiveData<Int>().apply {
    value = number
  }

  fun updateValueNumber(isIncrement: Boolean){
    numberValue.value = if (isIncrement) numberValue.value?.plus(1) else numberValue.value?.minus(1)
  }
}