package com.thienbinh.apartmentsearch.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.thienbinh.apartmentsearch.databinding.FragmentHomeBinding
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.viewModel.ApartmentViewModel
import com.thienbinh.apartmentsearch.viewModel.FragmentHomeViewModel

class HomeFragment : Fragment() {
  private lateinit var mFragmentHomeBinding: FragmentHomeBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, null, false).apply{

      fragmentHomeViewModel = ViewModelProvider(this@HomeFragment).get(FragmentHomeViewModel::class.java)

      lifecycleOwner = viewLifecycleOwner

    }

    return mFragmentHomeBinding.root
  }

  override fun onStart() {
    super.onStart()

    Handler(Looper.getMainLooper()).postDelayed({
      Log.d("Binh", "State: ${store.state.apartmentState.apartments.size}")
    }, 2000)
  }
}