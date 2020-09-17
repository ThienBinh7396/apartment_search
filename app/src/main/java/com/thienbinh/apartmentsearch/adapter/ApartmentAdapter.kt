package com.thienbinh.apartmentsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.ApartmentBlockLayoutBinding
import com.thienbinh.apartmentsearch.db.entities.Apartment
import com.thienbinh.apartmentsearch.viewModel.ApartmentViewModel

class ApartmentAdapter : RecyclerView.Adapter<ApartmentAdapter.ApartmentViewHolder>() {
  private val apartments = mutableListOf<Apartment>()

  class ApartmentViewHolder(private var binding: ApartmentBlockLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindingData(data: Apartment) {
      if (binding.apartmentViewModel == null) {
        binding.apartmentViewModel = ApartmentViewModel(data)
      } else {
        (binding.apartmentViewModel as ApartmentViewModel).updateApartment(data)
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentViewHolder =
    ApartmentViewHolder(
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.apartment_block_layout,
        null,
        false
      )
    )

  override fun onBindViewHolder(holder: ApartmentViewHolder, position: Int) {
    holder.bindingData(apartments[position])
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