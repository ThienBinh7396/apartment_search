package com.thienbinh.apartmentsearch

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import com.thienbinh.apartmentsearch.store.reducer.rootReducer
import com.thienbinh.apartmentsearch.util.FirstInitializeStoreData
import org.rekotlin.Store

val store = Store(
  reducer = ::rootReducer,
  state = null
)

class MainApplication : Application() {
  companion object {
    var currentActivity: Activity? = null

    var currentContext: Context? = null

  }

  override fun onCreate() {
    super.onCreate()

    currentContext = applicationContext

    FirstInitializeStoreData.updateAppDB(applicationContext)

    Log.d("Binh", "Application start!!!!!!!!!")
  }
}