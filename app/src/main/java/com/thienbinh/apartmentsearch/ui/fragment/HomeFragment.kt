package com.thienbinh.apartmentsearch.ui.fragment

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thienbinh.apartmentsearch.GlideApp
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.FragmentBottomSheetChooseDateLayoutBinding
import com.thienbinh.apartmentsearch.databinding.FragmentBottomSheetChooseGuestLayoutBinding
import com.thienbinh.apartmentsearch.databinding.FragmentBottomSheetFilterLayoutBinding
import com.thienbinh.apartmentsearch.databinding.FragmentHomeBinding
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.util.windowHeight
import com.thienbinh.apartmentsearch.util.windowWidth
import com.thienbinh.apartmentsearch.viewModel.ApartmentStateViewModel
import com.thienbinh.apartmentsearch.viewModel.FragmentHomeViewModel
import com.thienbinh.apartmentsearch.viewModel.IFragmentHomeViewModelEventListener
import kotlinx.android.synthetic.main.fragment_bottom_sheet_filter_layout.*


class HomeFragment : Fragment(), IFragmentHomeViewModelEventListener {
  private lateinit var mFragmentHomeBinding: FragmentHomeBinding

  private lateinit var mChooseDateDialog: BottomSheetDialog

  private lateinit var mChooseGuestDialog: BottomSheetDialog

  private lateinit var mFilterDialog: Dialog

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, null, false).apply {

      fragmentHomeViewModel =
        FragmentHomeViewModel(eventListener = this@HomeFragment)

      lifecycleOwner = viewLifecycleOwner

      gotoMapView.setOnClickListener {
        gotoMapView.findNavController().navigate(R.id.action_homeFragment_to_mapsApartmentFragment)
      }
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

    mFilterDialog = Dialog(requireContext(), R.style.CustomDialog).apply {
      setContentView(
        DataBindingUtil.inflate<FragmentBottomSheetFilterLayoutBinding>(
          LayoutInflater.from(requireContext()),
          R.layout.fragment_bottom_sheet_filter_layout,
          null,
          true
        ).apply {
          apartmentStateViewModel = ApartmentStateViewModel()

          lifecycleOwner = this@HomeFragment
        }.root
      )

      window?.setLayout(windowWidth, windowHeight)

      btnClose.setOnClickListener {
        this.dismiss()
      }
    }

    return mFragmentHomeBinding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
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

  override fun onShowFilterButtonClickListener() {
    mFilterDialog.show()
  }
}