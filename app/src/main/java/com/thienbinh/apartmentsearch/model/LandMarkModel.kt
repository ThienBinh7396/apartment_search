package com.thienbinh.apartmentsearch.model

import java.io.Serializable

data class LandMarkModel(
  val title: String,
  val distance: Float
) : Serializable

val listLandMarks = mutableListOf<LandMarkModel>(
  LandMarkModel(
    "Columbia Road Flower Market",
    0.5f
  ),
  LandMarkModel(
    "Brick Ln",
    0.6f
  ),
  LandMarkModel(
    "Old Spitalfields Market",
    0.7f
  ),
  LandMarkModel(
    "Coca-Cola London Eye",
    1.1f
  ),
  LandMarkModel(
    "Buckingham Palace",
    1.3f
  ),
)