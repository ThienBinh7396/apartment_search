package com.thienbinh.apartmentsearch.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.thienbinh.apartmentsearch.GlideApp
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.binding.DataBindingHelper.Companion.mapUrlWithBitmap
import com.thienbinh.apartmentsearch.databinding.FragmentApartmentDetailBinding
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.viewModel.ApartmentViewModel

class ApartmentDetailFragment : Fragment() {

  private lateinit var mFragmentApartmentDetailBinding: FragmentApartmentDetailBinding

  private var mApartment: Apartment? = null

  private var transitionName: String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

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

      GlideApp.with(this@ApartmentDetailFragment)
        .load(mApartment!!.thumbnail)
        .centerCrop()
        .into(imvThumbnail)

      if (mApartment != null) {
        apartmentViewModel = ApartmentViewModel(mApartment!!)
      }

      ibnGoBack.setOnClickListener {
        ibnGoBack.findNavController().navigateUp()
      }
    }

    return mFragmentApartmentDetailBinding.root
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