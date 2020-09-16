package com.thienbinh.apartmentsearch.db

import android.content.Context
import androidx.room.*
import com.thienbinh.apartmentsearch.db.dao.IApartmentAmenityDAO
import com.thienbinh.apartmentsearch.db.dao.IApartmentDAO
import com.thienbinh.apartmentsearch.db.dao.IApartmentReviewDAO
import com.thienbinh.apartmentsearch.db.dao.IApartmentTypeDAO
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.db.entities.ApartmentAmenity
import com.thienbinh.apartmentsearch.db.entities.ApartmentReview
import com.thienbinh.apartmentsearch.db.entities.ApartmentType

@Database(
  version = 1,
  entities = [ApartmentType::class, ApartmentReview::class, ApartmentAmenity::class, Apartment::class],
  exportSchema = false
)
abstract class AppDB : RoomDatabase() {
  companion object {
    private const val DB_NAME = "apartment-x"

    private var appDB: AppDB? = null
    private var mContext: Context? = null

    fun getInstance(context: Context): AppDB {
      mContext = context

      if (appDB == null) initDb()

      return appDB!!
    }

    private fun initDb() {
      appDB = Room.databaseBuilder(mContext!!, AppDB::class.java, DB_NAME)
        .allowMainThreadQueries()
        .build()

    }
  }

  abstract fun getApartmentDAO(): IApartmentDAO
  abstract fun getApartmentTypeDAO(): IApartmentTypeDAO
  abstract fun getApartmentAmenityDAO(): IApartmentAmenityDAO
  abstract fun getApartmentReviewDAO(): IApartmentReviewDAO
}