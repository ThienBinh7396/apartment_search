package com.thienbinh.apartmentsearch.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.db.entities.ApartmentAmenity
import com.thienbinh.apartmentsearch.db.tableHelper.TableName
import io.reactivex.Flowable

@Dao
interface IApartmentDAO: IBaseDAO<Apartment> {
  @Query("select * from ${TableName.APARTMENT_NAME}")
  fun getApartmentAmenities(): Flowable<List<Apartment>>

  @Query("select * from ${TableName.APARTMENT_NAME}")
  fun getApartmentAmenitiesSynchronous(): List<Apartment>
}