package com.thienbinh.apartmentsearch.db.entities

import androidx.room.*
import com.thienbinh.apartmentsearch.db.tableHelper.TableName
import com.thienbinh.apartmentsearch.util.gson
import java.io.Serializable

@Entity(
  tableName = TableName.APARTMENT_NAME, indices = [Index(unique = true, value = ["title"])],
  foreignKeys = [
    ForeignKey(
      entity = ApartmentType::class,
      parentColumns = ["id"],
      childColumns = ["type_id"],
      onDelete = ForeignKey.CASCADE
    )]
)
data class Apartment(
  @PrimaryKey(autoGenerate = true)
  val id: Int?,
  @ColumnInfo(name = "title")
  val title: String,
  @ColumnInfo(name = "thumbnail")
  val thumbnail: String,
  @ColumnInfo(name = "price")
  val price: Float,
  @ColumnInfo(name = "address")
  val address: String,
  @ColumnInfo(name = "longitude")
  val longitude: Float,
  @ColumnInfo(name = "latitude")
  val latitude: Float,
  @ColumnInfo(name = "maximum_adults")
  val maximum_adults: Int = 2,
  @ColumnInfo(name = "maximum_children")
  val maximum_children: Int = 0,
  @ColumnInfo(name = "maximum_infants")
  val maximum_infants: Int = 0,
  @ColumnInfo(name = "type_id")
  val typeId: Int,
  @ColumnInfo(name = "description")
  val description: String,
  @ColumnInfo(name = "isLiked")
  val isLiked: Boolean = false
) : Serializable

val checkApartmentAreTheSame =
  { elementOne: Apartment, elementTwo: Apartment ->
    elementOne.id == elementTwo.id && elementOne.title == elementTwo.title && elementOne.isLiked == elementTwo.isLiked
  }

fun MutableList<Apartment>.deepCloneApartmentList(): MutableList<Apartment> {
  return gson.fromJson(gson.toJson(this), Array<Apartment>::class.java).toMutableList()
}