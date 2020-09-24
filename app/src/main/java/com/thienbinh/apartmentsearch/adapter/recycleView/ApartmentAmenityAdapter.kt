package com.thienbinh.apartmentsearch.adapter.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.AmenityBlockLayoutBinding
import com.thienbinh.apartmentsearch.db.entities.ApartmentAmenity
import com.thienbinh.apartmentsearch.util.Helper.Companion.deepCloneList

class ApartmentAmenityAdapter :
  RecyclerView.Adapter<ApartmentAmenityAdapter.ApartmentAmenityViewHolder>() {

  private var mainAmenityList = mutableListOf<ApartmentAmenity>()

  class ApartmentAmenityViewHolder(val binding: AmenityBlockLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: ApartmentAmenity) {
      binding.amenity = data
      binding.executePendingBindings()
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ApartmentAmenityViewHolder(
    DataBindingUtil.inflate(
      LayoutInflater.from(parent.context),
      R.layout.amenity_block_layout,
      null,
      false
    )
  )

  override fun onBindViewHolder(holder: ApartmentAmenityViewHolder, position: Int) {
    holder.bindData(mainAmenityList[position])
  }

  override fun getItemCount(): Int = mainAmenityList.size

  fun updateList(list: MutableList<ApartmentAmenity>) {
    val diffCallback = ApartmentAmenityCallback(mainAmenityList, list)

    val diffResult = DiffUtil.calculateDiff(diffCallback)

    mainAmenityList = list.deepCloneList(Array<ApartmentAmenity>::class.java)

    diffResult.dispatchUpdatesTo(this)

  }

  class ApartmentAmenityCallback(
    val oldList: MutableList<ApartmentAmenity>,
    val newList: MutableList<ApartmentAmenity>
  ) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
      oldList[oldItemPosition].title == newList[newItemPosition].title

  }
}