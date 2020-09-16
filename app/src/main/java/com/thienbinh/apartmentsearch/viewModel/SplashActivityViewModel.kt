package com.thienbinh.apartmentsearch.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class SplashActivityViewModel : ViewModel() {
  val titleTask = MutableLiveData<String>().apply {
    value = "121"
  }

  fun updateTitle(title: String) {

    Log.d("Binh", "Update: $title")
    titleTask.value = title
  }
}