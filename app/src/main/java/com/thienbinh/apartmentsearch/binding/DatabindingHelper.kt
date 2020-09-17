package com.thienbinh.apartmentsearch.binding

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.Html
import android.transition.Transition
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.thienbinh.apartmentsearch.GlideApp
import com.thienbinh.apartmentsearch.adapter.ApartmentAdapter
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.util.RecycleViewSpaceItemDecoration
import com.thienbinh.apartmentsearch.util.SCALE_DP_PX
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class DataBindingHelper {
  companion object {
    @BindingAdapter("app:bindImageSrc")
    @JvmStatic
    fun bindImageSrc(imageView: ImageView, src: Any? = null) {
      if (src != null) {
        GlideApp.with(imageView.context)
          .load(src)
          .fitCenter()
          .into(imageView)
      }
    }

    @BindingAdapter("app:bindTopLeftRadiusImageSrc")
    @JvmStatic
    fun bindTopLeftRadiusImageSrc(imageView: ImageView, src: Any? = null) {
      if (src != null) {
        Log.d("Binh", "Parent: ${imageView.parent.requestLayout()}")

        Glide.with(imageView.context)
          .asBitmap()
          .load(src)
          .centerCrop()
          .transform(
            RoundedCornersTransformation(
              28 * SCALE_DP_PX.toInt(),
              0,
              RoundedCornersTransformation.CornerType.TOP_LEFT
            )
          )
          .into(imageView)

        imageView.parent.requestLayout()
      }
    }

    @BindingAdapter("app:bindShowUnless")
    @JvmStatic
    fun bindShowUnless(view: View, isShow: Boolean?) {
      view.visibility = if (isShow != null && isShow) View.VISIBLE else View.GONE
    }

    @BindingAdapter("app:bindApartmentList")
    @JvmStatic
    fun bindApartmentList(rcv: RecyclerView, list: MutableList<Apartment>?) {
      var adapter = rcv.adapter ?: ApartmentAdapter()

      if (rcv.adapter == null) {
        rcv.adapter = adapter

        rcv.layoutManager = GridLayoutManager(rcv.context, 1, GridLayoutManager.VERTICAL, false)
      }


      Log.d("Binh", "List: ${list?.size}")
      if (list != null) {

        (adapter as ApartmentAdapter).updateApartmentList(list)
      }
    }

    @BindingAdapter("app:bindResultFilter")
    @JvmStatic
    fun bindResultFilter(tv: TextView, num: Int? = null){
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        tv.text = Html.fromHtml("<font color='#626b80'><b>$num</b></font> results found", Html.FROM_HTML_MODE_COMPACT)
      }else{
        tv.text = Html.fromHtml("<font color='#626b80'><b>$num</b></font> results found")
      }
    }
  }
}