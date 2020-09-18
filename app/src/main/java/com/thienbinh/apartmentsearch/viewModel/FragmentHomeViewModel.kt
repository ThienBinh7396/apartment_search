package com.thienbinh.apartmentsearch.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.db.entities.checkApartmentAreTheSame
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.state.ApartmentState
import com.thienbinh.apartmentsearch.util.Helper
import org.rekotlin.StoreSubscriber
import java.util.*

interface IFragmentHomeViewModelEventListener{
  fun onChooseDateButtonClickListener()
  fun onChooseGuestButtonClickListener()
}

class FragmentHomeViewModel(eventListener: IFragmentHomeViewModelEventListener? = null) : ViewModel(), StoreSubscriber<ApartmentState> {
  init {
    store.subscribe(this){
      it.select {
        it.apartmentState
      }
    }
  }
  val title = MutableLiveData<String>().apply {
    value = "Hà Nội"
  }

  val apartments = MutableLiveData<MutableList<Apartment>>().apply {
    value = store.state.apartmentState.apartments
  }

  val eventListener = MutableLiveData<IFragmentHomeViewModelEventListener>().apply {
    value = eventListener
  }

  override fun newState(state: ApartmentState) {
    Log.d("Binh", "Apartment: ${apartments?.value}")

    if (apartments?.value != null && !Helper.checkListAreTheSame(apartments.value!!, state.apartments, checkApartmentAreTheSame)){
      apartments.value = state.apartments
    }
  }
}