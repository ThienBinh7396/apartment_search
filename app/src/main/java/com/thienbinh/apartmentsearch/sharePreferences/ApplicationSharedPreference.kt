package com.thienbinh.apartmentsearch.sharePreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.google.gson.Gson
import com.thienbinh.apartmentsearch.MainApplication
import java.io.Serializable
import java.lang.Error

data class ApplicationSharedPreferenceModel(
  var isFirstDBInitialize: Boolean = false
) : Serializable

class ApplicationSharedPreference {
  companion object {
    private var mContext: Context? = null

    private const val APPLICATION_SHARE_PREFERENCE_KEY = "APPLICATION_SHARE_PREFERENCE_KEY"

    private val gson = Gson()

    private val handlerThread = HandlerThread("ApplicationSharePreference")

    init {
      mContext = MainApplication.currentContext
      handlerThread.start()

    }

    private fun getApplicationSharedPreference(): SharedPreferences? {
      try {
        return mContext?.getSharedPreferences("apartment-application", MODE_PRIVATE)

      } catch (err: Error) {
        Log.d("Binh", "Application shared preference: ${err.message}")
      }
      return null
    }

    fun getApplicationInformation(): ApplicationSharedPreferenceModel {
      return getApplicationSharedPreference()?.getString(APPLICATION_SHARE_PREFERENCE_KEY, null)
        ?.let {
          gson.fromJson(it, ApplicationSharedPreferenceModel::class.java)
        } ?: ApplicationSharedPreferenceModel()
    }

    fun updateApplicationInformation(information: ApplicationSharedPreferenceModel) {
      Handler(handlerThread.looper).post {
        getApplicationSharedPreference()?.apply {
          edit().putString(APPLICATION_SHARE_PREFERENCE_KEY, gson.toJson(information)).apply()
        }
      }
    }

  }
}