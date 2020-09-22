package com.thienbinh.apartmentsearch.model

import androidx.annotation.DrawableRes
import java.io.Serializable

data class SortTypeModel(
  @DrawableRes var icon: Int,
  var title: String,
  var color: Int,
  var textColor: Int,
  var isActive: Boolean = false
) : Serializable