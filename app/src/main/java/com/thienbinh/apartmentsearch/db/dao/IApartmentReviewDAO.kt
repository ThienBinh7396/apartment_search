package com.thienbinh.apartmentsearch.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.thienbinh.apartmentsearch.db.entities.ApartmentReview
import com.thienbinh.apartmentsearch.db.tableHelper.TableName

@Dao
interface IApartmentReviewDAO: IBaseDAO<ApartmentReview> {

  @Query("select * from ${TableName.APARTMENT_COMMENT}")
  fun getApartmentComments(): List<ApartmentReview>
}