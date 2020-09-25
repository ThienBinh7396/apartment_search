package com.thienbinh.apartmentsearch.db.entities

import androidx.room.*
import com.thienbinh.apartmentsearch.db.tableHelper.TableName
import java.io.Serializable

@Entity(
  tableName = TableName.APARTMENT_AMENITY_NAME, indices = [Index(unique = true, value = ["title"])]
)
data class ApartmentAmenity(
  @PrimaryKey(autoGenerate = true)
  val id: Int?,
  @ColumnInfo(name = "title")
  val title: String,
  @ColumnInfo(name = "icon")
  val icon: String
) : Serializable