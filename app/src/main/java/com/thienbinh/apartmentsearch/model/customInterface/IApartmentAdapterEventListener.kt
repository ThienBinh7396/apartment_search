package com.thienbinh.apartmentsearch.model.customInterface

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.thienbinh.apartmentsearch.db.entities.Apartment

interface IApartmentAdapterEventListener {
  fun onGotoDetailEventListener(apartment: Apartment, imageView: ImageView, textViewTitle: TextView)
}