package com.thienbinh.apartmentsearch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.thienbinh.apartmentsearch.databinding.FragmentHomeBinding
import com.thienbinh.apartmentsearch.viewModel.ApartmentViewModel

class HomeFragment : Fragment() {
  private lateinit var mFragmentHomeBinding: FragmentHomeBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, null, false).apply{
      apartmentViewModel = ViewModelProvider(this@HomeFragment).get(ApartmentViewModel::class.java)

      lifecycleOwner = viewLifecycleOwner

    }

    return mFragmentHomeBinding.root
  }
}