package com.thienbinh.apartmentsearch.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.thienbinh.apartmentsearch.db.tableHelper.TableName
import java.io.Serializable

@Entity(
  tableName = TableName.APARTMENT_TYPE_NAME,
  indices = [Index(value = ["title"], unique = true)]
)
data class ApartmentType(
  @PrimaryKey(autoGenerate = true)
  val id: Int?,
  @ColumnInfo(name = "title")
  val title: String,
  @ColumnInfo(name = "description")
  val description: String
) : Serializable