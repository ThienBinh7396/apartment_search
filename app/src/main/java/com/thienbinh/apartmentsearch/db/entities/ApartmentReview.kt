package com.thienbinh.apartmentsearch.db.entities

import androidx.room.*
import com.thienbinh.apartmentsearch.db.tableHelper.TableName
import java.io.Serializable

@Entity(
  tableName = TableName.APARTMENT_COMMENT,
  foreignKeys = [
    ForeignKey(
      entity = Apartment::class,
      parentColumns = ["id"],
      childColumns = ["apartment_id"],
      onDelete = ForeignKey.CASCADE
    )]
)
data class ApartmentReview(
  @PrimaryKey(autoGenerate = true)
  val id: Int?,
  @ColumnInfo(name = "apartment_id")
  val apartmentId: Int,
  @ColumnInfo(name = "user_name")
  val userName: String,
  @ColumnInfo(name = "comment")
  val comment: String,
  @ColumnInfo(name = "rate")
  val rate: Float
) : Serializable