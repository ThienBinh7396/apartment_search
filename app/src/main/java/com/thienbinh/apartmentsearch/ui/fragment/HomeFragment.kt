package com.thienbinh.apartmentsearch.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.FragmentBottomSheetChooseDateLayoutBinding
import com.thienbinh.apartmentsearch.databinding.FragmentBottomSheetChooseGuestLayoutBinding
import com.thienbinh.apartmentsearch.databinding.FragmentHomeBinding
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.viewModel.ApartmentStateViewModel
import com.thienbinh.apartmentsearch.viewModel.ApartmentViewModel
import com.thienbinh.apartmentsearch.viewModel.FragmentHomeViewModel
import com.thienbinh.apartmentsearch.viewModel.IFragmentHomeViewModelEventListener

class HomeFragment : Fragment(), IFragmentHomeViewModelEventListener {
  private lateinit var mFragmentHomeBinding: FragmentHomeBinding

  private lateinit var mChooseDateDialog: BottomSheetDialog

  private lateinit var mChooseGuestDialog: BottomSheetDialog

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, null, false).apply {

      fragmentHomeViewModel =
        FragmentHomeViewModel(eventListener = this@HomeFragment)

      lifecycleOwner = viewLifecycleOwner
    }

    mChooseDateDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog).apply {
      setContentView(
        DataBindingUtil.inflate<FragmentBottomSheetChooseDateLayoutBinding>(
          LayoutInflater.from(requireContext()),
          R.layout.fragment_bottom_sheet_choose_date_layout,
          null,
          false
        ).apply {
          apartmentStateViewModel = ApartmentStateViewModel()

          lifecycleOwner = this@HomeFragment
        }.root
      )
    }

    mChooseGuestDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog).apply {
      setContentView(
        DataBindingUtil.inflate<FragmentBottomSheetChooseGuestLayoutBinding>(
          LayoutInflater.from(requireContext()),
          R.layout.fragment_bottom_sheet_choose_guest_layout,
          null,
          false
        ).apply {

          apartmentStateViewModel = ApartmentStateViewModel()

          lifecycleOwner = this@HomeFragment
        }.root
      )
    }

    return mFragmentHomeBinding.root
  }

  override fun onStart() {
    super.onStart()

    Handler(Looper.getMainLooper()).postDelayed({
      Log.d("Binh", "State: ${store.state.apartmentState.apartments.size}")
    }, 2000)
  }

  override fun onChooseDateButtonClickListener() {
    Log.d("Binh", "Choose date")

    mChooseDateDialog.show()
  }

  override fun onChooseGuestButtonClickListener() {

    mChooseGuestDialog.show()

  }
}