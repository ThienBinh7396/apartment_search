package com.thienbinh.apartmentsearch.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.thienbinh.apartmentsearch.GlideApp
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.binding.DataBindingHelper.Companion.mapUrlWithBitmap
import com.thienbinh.apartmentsearch.databinding.FragmentApartmentDetailBinding
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.util.Helper.Companion.roundTo
import com.thienbinh.apartmentsearch.viewModel.ApartmentViewModel
import java.lang.Math.abs

class ApartmentDetailFragment : Fragment() {

  private lateinit var mFragmentApartmentDetailBinding: FragmentApartmentDetailBinding

  private var mApartment: Apartment? = null

  private var transitionName: String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move).apply {
      duration = 350
    }

    sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.slide_right)

    setHasOptionsMenu(true)

    getDateFromBundle()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    mFragmentApartmentDetailBinding = DataBindingUtil.inflate<FragmentApartmentDetailBinding>(
      inflater,
      R.layout.fragment_apartment_detail,
      container,
      false
    ).apply {
      imvThumbnail.transitionName = transitionName

      if (mapUrlWithBitmap[mApartment!!.thumbnail] != null){
        imvThumbnail.setImageDrawable(mapUrlWithBitmap[mApartment!!.thumbnail])

        Log.d("Binh", "Fragment: $transitionName has image")


      }else{
        GlideApp.with(this@ApartmentDetailFragment)
          .load(mApartment!!.thumbnail)
          .centerCrop()
          .into(imvThumbnail)
      }

      if (mApartment != null) {
        apartmentViewModel = ApartmentViewModel(mApartment!!)
      }

      ibnGoBack.setOnClickListener {
        imvThumbnail.findNavController().navigateUp()
      }
    }

    setupBottomSheetBehavior()

    return mFragmentApartmentDetailBinding.root
  }

  private fun setupBottomSheetBehavior() {
   mFragmentApartmentDetailBinding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

     val offsetRate = kotlin.math.abs(verticalOffset) * 1f / appBarLayout.totalScrollRange

     Log.d("Binh", "Offset: ${abs(verticalOffset)} ${appBarLayout.totalScrollRange} ${offsetRate.roundTo(1)}")


            if (mFragmentApartmentDetailBinding.headerTitle.alpha != offsetRate){
              mFragmentApartmentDetailBinding.headerTitle.alpha = offsetRate
            }
   })
  }

  private fun getDateFromBundle() {
    val apartment = arguments?.getSerializable("apartment")

    if (apartment != null && apartment is Apartment)
      mApartment = apartment

    arguments?.getString("transitionName")?.apply {
      transitionName = this
    }
  }

}