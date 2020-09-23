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
import com.thienbinh.apartmentsearch.db.entities.deepCloneApartmentList
import com.thienbinh.apartmentsearch.model.ApartmentFilterModel
import com.thienbinh.apartmentsearch.model.checkApartmentFilterModelAreTheSame
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.state.ApartmentState
import com.thienbinh.apartmentsearch.util.Helper
import org.rekotlin.StoreSubscriber
import java.util.*

interface IFragmentHomeViewModelEventListener {
  fun onChooseDateButtonClickListener()
  fun onChooseGuestButtonClickListener()
  fun onShowFilterButtonClickListener()
}

class FragmentHomeViewModel(eventListener: IFragmentHomeViewModelEventListener? = null) :
  ViewModel(), Observable, StoreSubscriber<ApartmentState> {
  init {
    store.subscribe(this) {
      it.select {
        it.apartmentState
      }
    }
  }

  val title = MutableLiveData<String>().apply {
    value = "Hà Nội"
  }

  val apartments = MutableLiveData<MutableList<Apartment>>().apply {
    value = store.state.apartmentState.apartments.deepCloneApartmentList()
  }

  val apartmentFilterModel = MutableLiveData<ApartmentFilterModel>().apply {
    value = store.state.apartmentState.apartmentFilterModel
  }

  val eventListener = MutableLiveData<IFragmentHomeViewModelEventListener>().apply {
    value = eventListener
  }

  @Bindable
  fun getTotalGuests(): String {
    apartmentFilterModel.value?.apply {
      return "${adultsAmount + childrenAmount + infantsAmount} guests"
    }

    return "0 guest"
  }

  @Bindable
  fun getRangeDateText(): String {
    if (apartmentFilterModel.value?.startDate == null || apartmentFilterModel.value?.endDate == null) return "MM dd - dd"

    apartmentFilterModel.value?.apply {
      if (startDate!!.month == endDate!!.month) {
        return "${Helper.monOfYears[startDate!!.month]} ${startDate!!.date} - ${endDate!!.date}"

      }

      return "${Helper.monOfYears[startDate!!.month]} ${startDate!!.date} - ${Helper.monOfYears[endDate!!.month]} ${endDate!!.date}"

    }

    return ""
  }

  override fun newState(state: ApartmentState) {
//    Log.d("Binh", "Apartment: ${apartments?.value}")

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

      Log.d("Binh", "Update filter")
      apartmentFilterModel.value = state.apartmentFilterModel

      callbacks.notifyCallbacks(this, BR.rangeDateText, null)
      callbacks.notifyCallbacks(this, BR.totalGuests, null)
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