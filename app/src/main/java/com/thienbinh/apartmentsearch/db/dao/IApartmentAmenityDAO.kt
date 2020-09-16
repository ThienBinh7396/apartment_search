package com.thienbinh.apartmentsearch.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.thienbinh.apartmentsearch.db.entities.ApartmentAmenity
import com.thienbinh.apartmentsearch.db.tableHelper.TableName
import io.reactivex.Flowable

@Dao
interface IApartmentAmenityDAO : IBaseDAO<ApartmentAmenity> {
  @Query("select * from ${TableName.APARTMENT_AMENITY_NAME}")
  fun getApartmentAmenities(): Flowable<List<ApartmentAmenity>>

  @Query("select * from ${TableName.APARTMENT_AMENITY_NAME}")
  fun getApartmentAmenitiesSynchronous(): List<ApartmentAmenity>
}