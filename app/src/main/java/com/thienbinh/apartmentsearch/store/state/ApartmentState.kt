package com.thienbinh.apartmentsearch.store.state

import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.db.entities.ApartmentAmenity
import com.thienbinh.apartmentsearch.db.entities.ApartmentType
import com.thienbinh.apartmentsearch.model.ApartmentFilterModel
import com.thienbinh.apartmentsearch.store.action.ApartmentAction
import org.rekotlin.StateType

data class ApartmentState(
  val apartments: MutableList<Apartment> = mutableListOf(),
  val apartmentAmenities: MutableList<ApartmentAmenity> = mutableListOf(),
  val apartmentTypes: MutableList<ApartmentType> = mutableListOf(),
  var apartmentFilterModel: ApartmentFilterModel = ApartmentFilterModel()
): StateType