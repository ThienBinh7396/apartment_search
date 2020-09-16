package com.thienbinh.apartmentsearch.store.action

import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.db.entities.ApartmentAmenity
import com.thienbinh.apartmentsearch.db.entities.ApartmentType
import org.rekotlin.Action

sealed class ApartmentAction : Action {
  class Apartment_ACTION_UPDATE_APARTMENTS(var apartments: MutableList<Apartment>): Action
  class Apartment_ACTION_UPDATE_APARTMENT_TYPES(var list: MutableList<ApartmentType>): Action
  class Apartment_ACTION_UPDATE_APARTMENT_AMENITIES(var list: MutableList<ApartmentAmenity>): Action
}