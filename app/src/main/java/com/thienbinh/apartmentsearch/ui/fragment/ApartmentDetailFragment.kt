package com.thienbinh.apartmentsearch.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.TransitionInflater
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.appbar.AppBarLayout
import com.thienbinh.apartmentsearch.GlideApp
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.binding.DataBindingHelper.Companion.mapUrlWithBitmap
import com.thienbinh.apartmentsearch.databinding.FragmentApartmentDetailBinding
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.ui.activity.MainActivity
import com.thienbinh.apartmentsearch.util.Helper
import com.thienbinh.apartmentsearch.util.Helper.Companion.roundTo
import com.thienbinh.apartmentsearch.util.MapRipple
import com.thienbinh.apartmentsearch.util.SCALE_DP_PX
import com.thienbinh.apartmentsearch.viewModel.ApartmentViewModel
import org.joda.time.DateTime
import org.joda.time.Days
import java.lang.Math.abs


class ApartmentDetailFragment : Fragment(), OnMapReadyCallback {

  private lateinit var mFragmentApartmentDetailBinding: FragmentApartmentDetailBinding

  private var mApartment: Apartment? = null

  private var transitionName: String = ""

  private var titleTransitionName: String = ""

  private var contentTranslationY = 26 * SCALE_DP_PX

  private lateinit var mMapView: MapView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    sharedElementEnterTransition =
      TransitionInflater.from(context).inflateTransition(android.R.transition.move).apply {
        duration = 350
      }

    sharedElementReturnTransition =
      TransitionInflater.from(context).inflateTransition(android.R.transition.no_transition)

    getDateFromBundle()

  }

  @SuppressLint("SetTextI18n")
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

      if (mapUrlWithBitmap[mApartment!!.thumbnail] != null) {
        imvThumbnail.setImageDrawable(mapUrlWithBitmap[mApartment!!.thumbnail])

        Log.d("Binh", "Fragment: $transitionName has image")


      } else {
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

      mMapView = map
      mMapView.onCreate(savedInstanceState)
      mMapView.onResume()

      mMapView.getMapAsync(this@ApartmentDetailFragment)

      tvPrice.text = "$${getCountDate() * mApartment!!.price.toInt()}"
    }

    setupBottomSheetBehavior()

    return mFragmentApartmentDetailBinding.root
  }

  private fun getCountDate(): Int {
    store.state.apartmentState.apartmentFilterModel.apply {
      if (startDate != null && endDate != null) {
        return Days.daysBetween(DateTime(startDate), DateTime(endDate)).days + 1
      }
    }

    return 0
  }

  private fun setupBottomSheetBehavior() {
    updateContentTranslationYByOffsetRate(0f)

    mFragmentApartmentDetailBinding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

      val offsetRate = kotlin.math.abs(verticalOffset) * 1f / appBarLayout.totalScrollRange

      Log.d(
        "Binh",
        "Offset: ${abs(verticalOffset)} ${appBarLayout.totalScrollRange} ${offsetRate.roundTo(1)}"
      )

      if (mFragmentApartmentDetailBinding.headerTitle.alpha != offsetRate) {
        mFragmentApartmentDetailBinding.headerTitle.alpha = offsetRate
        mFragmentApartmentDetailBinding.maskHeaderForeground.alpha = 1 - offsetRate
        updateContentTranslationYByOffsetRate(offsetRate)
      }
    })
  }

  private fun updateContentTranslationYByOffsetRate(offsetRate: Float) {
    mFragmentApartmentDetailBinding.content.translationY = -(1 - offsetRate) * contentTranslationY
  }

  private fun getDateFromBundle() {
    val apartment = arguments?.getSerializable("apartment")

    if (apartment != null && apartment is Apartment)
      mApartment = apartment

    arguments?.getString("transitionName")?.apply {
      transitionName = this
    }
    arguments?.getString("titleTransitionName")?.apply {
      titleTransitionName = this
    }
  }

  override fun onResume() {
    super.onResume()
    mMapView.onResume()
  }

  override fun onPause() {
    super.onPause()
    mMapView.onPause()
  }

  override fun onDestroy() {
    super.onDestroy()
    mMapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mMapView.onLowMemory()
  }

  private var firstInitialized = false

  override fun onMapReady(googleMap: GoogleMap?) {
    customStyleGoogleMap(googleMap)

    mApartment?.apply {
      val apartmentLatLng = LatLng(latitude, longitude)

      val cameraPosition = CameraPosition.Builder()
        .target(apartmentLatLng).zoom(17f).build()

      googleMap?.moveCamera(
        CameraUpdateFactory
          .newCameraPosition(cameraPosition)
      )


      googleMap?.addMarker(
        MarkerOptions()
          .icon(
            BitmapDescriptorFactory.fromBitmap(
              makeBitmapText(requireContext(), title, address, 300, 100)
            )
          )
          .position(apartmentLatLng)
          .anchor(-0.05f, 0.2f)
      )

      MapRipple(googleMap!!, apartmentLatLng, requireContext())
        .stopRippleMapAnimation()
        .withNumberOfRipples(4)
        .withFillColor(Color.parseColor("#6e93f1"))
        .withStrokeWidth(0)
        .withDistance(100.0)
        .withRippleDuration(6000)
        .withDurationBetweenTwoRipples(1500)
        .withTransparency(0.6f)
        .startRippleMapAnimation()

      val marker = googleMap.addMarker(
        MarkerOptions()
          .icon(
            BitmapDescriptorFactory.fromBitmap(
              Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(
                  resources,
                  R.drawable.map_icon
                ), 36, 36, false
              )
            )
          )
          .position(apartmentLatLng)
          .anchor(0.5f, 0.5f)
      )

      Log.d("Binh", "Map: $googleMap $latitude $longitude")
    }

  }

  fun makeBitmapText(
    context: Context,
    title: String,
    address: String,
    width: Int,
    height: Int
  ): Bitmap? {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    paint.color = Color.parseColor("#111111")// Text color
    paint.textSize = 11 * SCALE_DP_PX // Text size

    canvas.drawText(title, 20f, 30f, paint)
    return bitmap
  }

  private fun customStyleGoogleMap(googleMap: GoogleMap?) {
    if (googleMap == null) return

    googleMap.setMapStyle(MainActivity.mapSliverStyle)

    googleMap.uiSettings.isScrollGesturesEnabled = false
    googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false
    googleMap.setMinZoomPreference(17f)
  }
}