package com.thienbinh.apartmentsearch.viewModel

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thienbinh.apartmentsearch.BR
import com.thienbinh.apartmentsearch.db.entities.*
import com.thienbinh.apartmentsearch.model.ApartmentFilterModel
import com.thienbinh.apartmentsearch.model.checkApartmentFilterModelAreTheSame
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.state.ApartmentState
import com.thienbinh.apartmentsearch.util.Helper
import org.joda.time.DateTime
import org.joda.time.Days
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
    value = store.state.apartmentState.apartments.deepCloneApartmentList()
  }

  val apartmentFilterModel = MutableLiveData<ApartmentFilterModel>().apply {
    value = store.state.apartmentState.apartmentFilterModel
  }

  val apartmentTypes = MutableLiveData<MutableList<ApartmentType>>().apply {
    value = store.state.apartmentState.apartmentTypes.deepCloneApartmentTypeList()
  }

  @Bindable
  fun getStartDate() = Helper.formatDate(apartmentFilterModel.value?.startDate)

  @Bindable
  fun getEndDate() = Helper.formatDate(apartmentFilterModel.value?.endDate)

  @Bindable
  fun getCountDate(): Int {
    apartmentFilterModel.value?.apply {
      if (startDate != null && endDate != null) {
        return Days.daysBetween(DateTime(startDate), DateTime(endDate)).days + 1
      }
    }

    return 0
  }

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
      apartments.value = state.apartments.deepCloneApartmentList()
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
      callbacks.notifyCallbacks(this, BR.countDate, null)
    }

    if (apartmentTypes?.value != null && !Helper.checkListAreTheSame(
        apartmentTypes.value!!,
        state.apartmentTypes,
        checkApartmentTypeAreTheSame
      )
    ) {
      apartmentTypes.value = state.apartmentTypes.deepCloneApartmentTypeList()


      Log.d("Binh", "Update apartment type")
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