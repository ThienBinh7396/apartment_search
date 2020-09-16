package com.thienbinh.apartmentsearch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ApartmentViewModel : ViewModel() {
  val isLoading = MutableLiveData<Boolean>().apply {
    value = true
  }

  fun toggleStateLoading(){
    isLoading.value = !isLoading.value!!
  }
}