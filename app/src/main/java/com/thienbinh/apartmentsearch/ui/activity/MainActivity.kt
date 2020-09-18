package com.thienbinh.apartmentsearch.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.util.FirstInitializeStoreData

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    FirstInitializeStoreData.initializeStoreData()
  }
}