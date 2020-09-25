package com.thienbinh.apartmentsearch.ui.fragment

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
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.FragmentMapsApartmentBinding
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.ui.activity.MainActivity
import com.thienbinh.apartmentsearch.util.SCALE_DP_PX
import com.thienbinh.apartmentsearch.viewModel.ApartmentViewModel


class MapsApartmentFragment : Fragment() {

  private lateinit var mapView: MapView

  private lateinit var mapsApartmentBinding: FragmentMapsApartmentBinding

  private lateinit var mApartmentViewModel: ApartmentViewModel

  private lateinit var mGoogleMap: GoogleMap

  val apartments = store.state.apartmentState.apartments

  private var mApartment = apartments[0]

  val markers = mutableListOf<Marker>()

  private var lastPosition = -1

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

  private fun getDateFromBundle() {
    arguments?.getSerializable("apartment")?.apply {
      if (this is Apartment) mApartment = this
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    mapsApartmentBinding = DataBindingUtil.inflate<FragmentMapsApartmentBinding>(
      inflater,
      R.layout.fragment_maps_apartment,
      container,
      false
    ).apply {
      mapView = map

      mapView.onCreate(savedInstanceState)
      mapView.onResume()

      mapView.getMapAsync(callback)

      goBack.setOnClickListener {
        goBack.findNavController().navigateUp()
      }

      imageTransition.setOnClickListener {
        val extras = FragmentNavigatorExtras(
          imageTransition to "imageViewTransition"
        )

        imageTransition.findNavController().navigate(
          R.id.action_mapsApartmentFragment_to_apartmentDetailFragment, bundleOf(
            "apartment" to mApartment,
            "transitionName" to "imageViewTransition",
          ), null, extras
        )
      }

      mApartmentViewModel = ApartmentViewModel(mApartment)

      apartmentViewModel = mApartmentViewModel

      lifecycleOwner = this@MapsApartmentFragment
    }

    return mapsApartmentBinding.root
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  private val callback = OnMapReadyCallback { googleMap ->
    mGoogleMap = googleMap

    customStyleGoogleMap(googleMap)

    makeApartmentsMarker()

    googleMap.setOnMarkerClickListener(onMarkerCallback)
  }

  private val onMarkerCallback = GoogleMap.OnMarkerClickListener { marker ->
    var id = marker.tag ?: return@OnMarkerClickListener true

    id = id as Int

    var indexFound: Int = -1

    var apartmentFound: Apartment? = null

    apartments.forEachIndexed { index, apartment ->
      if (apartment.id == id) {
        apartmentFound = apartment
        indexFound = index
        return@forEachIndexed
      }
    }

    if (apartmentFound == null) return@OnMarkerClickListener true

    mApartmentViewModel.updateApartment(apartmentFound!!)

    mApartment = apartmentFound!!

    updateMakerIconAt(markers[lastPosition], apartments[lastPosition], false)
    updateMakerIconAt(markers[indexFound], apartmentFound!!, true)

    lastPosition = indexFound

    return@OnMarkerClickListener false
  }

  private fun makeApartmentsMarker() {
    apartments.forEachIndexed { index, apartment ->

      if (mApartment.id === apartment.id) lastPosition = index

      markers.add(
        makeMakerOf(
          apartment,
          index,
          mApartment.id === apartment.id
        )
      )
    }
  }

  private fun updateMakerIconAt(marker: Marker, apartment: Apartment, isActive: Boolean) {
    marker.setIcon(
      BitmapDescriptorFactory.fromBitmap(
        makeIconMarker(
          requireContext(),
          apartment.price,
          isActive
        )
      )
    )
  }

  private fun makeMakerOf(apartment: Apartment, position: Int, isActive: Boolean): Marker {
    val apartmentLatLng = LatLng(apartment.latitude, apartment.longitude)

    val marker = mGoogleMap.addMarker(
      MarkerOptions().position(apartmentLatLng).title(apartment.title).icon(
        BitmapDescriptorFactory.fromBitmap(
          makeIconMarker(
            requireContext(),
            apartment.price,
            isActive
          )
        )
      )
    )

    marker.tag = apartment.id

    if (mApartment.id === apartment.id) {

      val cameraPosition = CameraPosition.Builder()
        .target(apartmentLatLng).zoom(15.5f).build()

      mGoogleMap.moveCamera(
        CameraUpdateFactory
          .newCameraPosition(cameraPosition)
      )
    }

    return marker
  }

  private val titleMakerSize = 15 * SCALE_DP_PX
  private val titleMakerPaddingVertical = 9 * SCALE_DP_PX
  private val titleMakerPaddingHorizontal = 13 * SCALE_DP_PX

  private fun makeIconMarker(
    context: Context,
    price: Float,
    isActive: Boolean = false
  ): Bitmap {

    val text = "$$price"
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val boundText = Rect()

    paint.textSize = titleMakerSize
    paint.typeface = Typeface.DEFAULT_BOLD
    paint.getTextBounds(text, 0, text.length, boundText)

    val containerWidth = (titleMakerPaddingHorizontal * 2 + boundText.width()).toInt()
    val containerHeight = (titleMakerPaddingVertical * 2 + boundText.height()).toInt()

    val offset = 5

    val bitmap = Bitmap.createBitmap(
      containerWidth + offset * 2,
      containerHeight + offset * 2,
      Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)

    paint.color = Color.GRAY
    paint.maskFilter = BlurMaskFilter(
      7f /* shadowRadius */,
      BlurMaskFilter.Blur.OUTER
    )

    canvas.drawRoundRect(
      RectF(
        5f + offset,
        5f + offset,
        containerWidth.toFloat() + offset,
        containerHeight.toFloat() + offset
      ),
      containerHeight.toFloat(),
      containerHeight.toFloat(),
      paint

    )

    paint.color = if (isActive) Color.parseColor("#3165ec") else Color.WHITE
    paint.maskFilter = null
    canvas.drawRoundRect(
      RectF(
        0f + offset,
        0f + offset,
        containerWidth.toFloat() + offset,
        containerHeight.toFloat() + offset
      ),
      containerHeight.toFloat(),
      containerHeight.toFloat(),
      paint
    )

    paint.color = if (isActive) Color.WHITE else Color.parseColor("#3165ec")

    canvas.drawText(
      text,
      titleMakerPaddingHorizontal + offset,
      boundText.height() - 4 + titleMakerPaddingVertical + offset,
      paint
    )

    return bitmap
  }

  private fun customStyleGoogleMap(googleMap: GoogleMap?) {
    if (googleMap == null) return

    googleMap.setMapStyle(MainActivity.mapSliverStyle)

    googleMap.setMinZoomPreference(10f)

  }
}