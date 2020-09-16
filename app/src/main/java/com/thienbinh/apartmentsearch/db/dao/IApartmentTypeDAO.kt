package com.thienbinh.apartmentsearch.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.thienbinh.apartmentsearch.db.entities.ApartmentType
import com.thienbinh.apartmentsearch.db.tableHelper.TableName
import io.reactivex.Flowable

@Dao
interface IApartmentTypeDAO: IBaseDAO<ApartmentType> {
  @Query("Select * from ${TableName.APARTMENT_TYPE_NAME}")
  fun getApartmentTypeListSynchronous(): List<ApartmentType>

  @Query("Select * from ${TableName.APARTMENT_TYPE_NAME}")
  fun getApartmentTypeList(): Flowable<List<ApartmentType>>
}