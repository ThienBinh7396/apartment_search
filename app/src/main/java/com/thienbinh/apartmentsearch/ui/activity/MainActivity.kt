package com.thienbinh.apartmentsearch.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.google.android.gms.maps.model.MapStyleOptions
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.ActivityMainBinding
import com.thienbinh.apartmentsearch.util.FirstInitializeStoreData

class MainActivity : AppCompatActivity() {
  companion object {
    private var navControllerMainActivity: NavController? = null

    var mapSliverStyle: MapStyleOptions? = null

    fun navigate(id: Int?, bundle: Bundle? = null, extras: FragmentNavigator.Extras? = null) {
      if (id == null || navControllerMainActivity == null) return

      navControllerMainActivity?.navigate(id, bundle, null, extras)
    }

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

    FirstInitializeStoreData.initializeStoreData()

    setupNavigation()

    initializeCompanionData()
  }

  private fun initializeCompanionData() {
    mapSliverStyle =
      MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.google_map_sliver_theme)
  }

  private fun setupNavigation() {
    navControllerMainActivity = findNavController(R.id.nav_host)
  }
}