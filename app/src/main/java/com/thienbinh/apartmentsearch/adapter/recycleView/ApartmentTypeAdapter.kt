package com.thienbinh.apartmentsearch.adapter.recycleView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.ApartmentTypeLayoutBinding
import com.thienbinh.apartmentsearch.db.entities.ApartmentType
import com.thienbinh.apartmentsearch.util.gson

class ApartmentTypeAdapter : RecyclerView.Adapter<ApartmentTypeAdapter.ApartmentTypeViewHolder>() {
  private val mApartmentTypes = mutableListOf<ApartmentType>()

  class ApartmentTypeViewHolder(val binding: ApartmentTypeLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: ApartmentType) {
      binding.apartmentType = data
      binding.executePendingBindings()
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ApartmentTypeViewHolder(
    DataBindingUtil.inflate(
      LayoutInflater.from(parent.context),
      R.layout.apartment_type_layout,
      parent,
      false
    )
  )

  override fun onBindViewHolder(holder: ApartmentTypeViewHolder, position: Int) {
    holder.bindData(mApartmentTypes[position])
  }

  override fun getItemCount(): Int = mApartmentTypes.size

  fun setActiveAt(position: Int){
    if (position >= mApartmentTypes.size) return

    mApartmentTypes[position].isActive = !mApartmentTypes[position].isActive

    Log.d("Binh", "Gson: ${gson.toJson(mApartmentTypes[position])}")

    notifyItemChanged(position)
  }

  fun updateList(list: MutableList<ApartmentType>){
    val diffCallback = ApartmentTypeCallBack(mApartmentTypes, list)

    val diffResult = DiffUtil.calculateDiff(diffCallback)

    mApartmentTypes.clear()
    mApartmentTypes.addAll(list)

    diffResult.dispatchUpdatesTo(this)
  }

  class ApartmentTypeCallBack(val oldList: MutableList<ApartmentType>, val newList: MutableList<ApartmentType>): DiffUtil.Callback(){
    override fun getOldListSize(): Int  = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].isActive == newList[newItemPosition].isActive
  }
}