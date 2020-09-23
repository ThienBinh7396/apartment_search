package com.thienbinh.apartmentsearch.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.model.customInterface.IApartmentAdapterEventListener

class ApartmentViewModel(apartment: Apartment, eventListener: IApartmentAdapterEventListener? = null) : ViewModel() {
  val apartment = MutableLiveData<Apartment>().apply {
    value = apartment
  }

  val eventListener = MutableLiveData<IApartmentAdapterEventListener>().apply {
    value = eventListener
  }

  fun getApartmentPriceText() = "$${apartment.value?.price}"

  fun updateApartment(apartment: Apartment) {
    this.apartment.value = apartment
  }

}