package com.thienbinh.apartmentsearch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thienbinh.apartmentsearch.db.entities.Apartment

class ApartmentViewModel(apartment: Apartment) : ViewModel() {
  val apartment = MutableLiveData<Apartment>().apply {
    value = apartment
  }

  fun getApartmentPriceText() = "$${apartment.value?.price}"

  fun updateApartment(apartment: Apartment) {
    this.apartment.value = apartment
  }

  val isLoading = MutableLiveData<Boolean>().apply {
    value = true
  }

  fun toggleStateLoading() {
    isLoading.value = !isLoading.value!!
  }
}