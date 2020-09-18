package com.thienbinh.apartmentsearch.util

import android.content.Context
import com.thienbinh.apartmentsearch.db.AppDB
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.action.ApartmentAction

class FirstInitializeStoreData {
  companion object {
    private var mAppDB: AppDB? = null

    fun updateAppDB(context: Context) {
      if (mAppDB == null) mAppDB = AppDB.getInstance(context)
    }

    fun getAppDatabase(): AppDB = mAppDB!!

    @JvmStatic
    fun initializeStoreData() {
      if (store.state.apartmentState.apartments.size != 0) return

      store.dispatch(ApartmentAction.Apartment_ACTION_UPDATE_APARTMENTS(getApartments().toMutableList()))
      store.dispatch(
        ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_AMENITIES(
          getApartmentAmenitiesData().toMutableList()
        )
      )
      store.dispatch(ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_TYPES(getApartmentTypesData().toMutableList()))
    }

    fun getApartments() = mAppDB!!.getApartmentDAO().getApartmentAmenitiesSynchronous()

    fun getApartmentTypesData() =
      mAppDB!!.getApartmentTypeDAO().getApartmentTypeListSynchronous()

    fun getApartmentAmenitiesData() =
      mAppDB!!.getApartmentAmenityDAO().getApartmentAmenitiesSynchronous()
  }
}