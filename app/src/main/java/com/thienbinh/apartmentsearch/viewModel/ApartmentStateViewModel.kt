package com.thienbinh.apartmentsearch.viewModel

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thienbinh.apartmentsearch.BR
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.db.entities.checkApartmentAreTheSame
import com.thienbinh.apartmentsearch.model.ApartmentFilterModel
import com.thienbinh.apartmentsearch.model.checkApartmentFilterModelAreTheSame
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.state.ApartmentState
import com.thienbinh.apartmentsearch.util.Helper
import org.rekotlin.StoreSubscriber

class ApartmentStateViewModel : ViewModel(), Observable, StoreSubscriber<ApartmentState> {

  init {
    store.subscribe(this) {
      it.select {
        it.apartmentState
      }
    }
  }

  val apartments = MutableLiveData<MutableList<Apartment>>().apply {
    value = store.state.apartmentState.apartments
  }

  val apartmentFilterModel = MutableLiveData<ApartmentFilterModel>().apply {
    value = store.state.apartmentState.apartmentFilterModel
  }


  @Bindable
  fun getStartDate() = Helper.formatDate(apartmentFilterModel.value?.startDate)

  @Bindable
  fun getEndDate() = Helper.formatDate(apartmentFilterModel.value?.endDate)

  @Bindable
  fun getAmountGuestFullText(): String {
    apartmentFilterModel.value?.apply {
      var text = "${adultsAmount} Adults "

      if (childrenAmount > 0) {
        text += "- ${childrenAmount} Children "
      }

      if (infantsAmount > 0) {
        text += "- ${infantsAmount} Infants "
      }

      return text
    }

    return ""
  }

  override fun newState(state: ApartmentState) {

    if (apartments?.value != null && !Helper.checkListAreTheSame(
        apartments.value!!,
        state.apartments,
        checkApartmentAreTheSame
      )
    ) {
      apartments.value = state.apartments
    }

    if (apartmentFilterModel?.value != null && !checkApartmentFilterModelAreTheSame(
        apartmentFilterModel.value!!, state.apartmentFilterModel
      )
    ) {

      Log.d("Binh", "Update filter 123213")
      apartmentFilterModel.value = state.apartmentFilterModel

      callbacks.notifyCallbacks(this, BR.amountGuestFullText, null)
      callbacks.notifyCallbacks(this, BR.startDate, null)
      callbacks.notifyCallbacks(this, BR.endDate, null)
    }
  }

  private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

  override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    callbacks.add(callback)
  }

  override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    callbacks.remove(callback)
  }
}