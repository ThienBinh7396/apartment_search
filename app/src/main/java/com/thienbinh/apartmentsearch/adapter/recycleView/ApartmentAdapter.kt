package com.thienbinh.apartmentsearch.adapter.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.ApartmentBlockLayoutBinding
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.model.customInterface.IApartmentAdapterEventListener
import com.thienbinh.apartmentsearch.viewModel.ApartmentViewModel

class ApartmentAdapter(var eventListener: IApartmentAdapterEventListener) :
  RecyclerView.Adapter<ApartmentAdapter.ApartmentViewHolder>() {
  private val apartments = mutableListOf<Apartment>()

  class ApartmentViewHolder(
    private var binding: ApartmentBlockLayoutBinding,
    private val eventListener: IApartmentAdapterEventListener
  ) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var mApartment: Apartment

    init {
      binding.targetGotoDetail.setOnClickListener {
        eventListener.onGotoDetailEventListener(mApartment, binding.imvThumbnail, binding.tvTitle)
      }
    }

    fun bindingData(data: Apartment, isLastItem: Boolean = false, position: Int = 0) {
      if (binding.apartmentViewModel == null) {
        binding.apartmentViewModel = ApartmentViewModel(data, eventListener)
      } else {
        (binding.apartmentViewModel as ApartmentViewModel).updateApartment(data)
      }
      binding.isLastItem = isLastItem

      mApartment = data

      binding.imvThumbnail.transitionName = "imageView-transition-${position}"

      binding.tvTitle.transitionName = "textView-transition-${position}"
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentViewHolder =
    ApartmentViewHolder(
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.apartment_block_layout,
        null,
        false
      ),
      eventListener
    )

  override fun onBindViewHolder(holder: ApartmentViewHolder, position: Int) {
    holder.bindingData(apartments[position], position == apartments.size - 1, position)
  }

  override fun getItemCount(): Int = apartments.size

  fun updateApartmentList(newList: MutableList<Apartment>) {
    val diffCallback = ApartmentDiffCallback(apartments, newList)

    val diffResult = DiffUtil.calculateDiff(diffCallback)

    apartments.clear()
    apartments.addAll(newList)

    diffResult.dispatchUpdatesTo(this)
  }

  class ApartmentDiffCallback(
    var oldList: MutableList<Apartment>,
    var newList: MutableList<Apartment>
  ) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].title == oldList[newItemPosition].title && oldList[oldItemPosition].isLiked == oldList[newItemPosition].isLiked
  }
}