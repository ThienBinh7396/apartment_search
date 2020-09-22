package com.thienbinh.apartmentsearch.adapter.recycleView

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.SortTypeLayoutBinding
import com.thienbinh.apartmentsearch.model.SortTypeModel

class SortTypeAdapter(var context: Context) :
  RecyclerView.Adapter<SortTypeAdapter.SortTypeViewHolder>() {
  companion object {
    private val gson = Gson()

    private var currentActivePosition = -1

    private val listSortType = mutableListOf<SortTypeModel>()

    private fun getListSortType(): MutableList<SortTypeModel> {

      if (listSortType.size == 0) {
        listSortType.add(
          SortTypeModel(
            R.drawable.ic_feather_map_pin,
            "Nearby",
            Color.parseColor("#3165EC"),
            Color.parseColor("#435582")
          )
        )

        listSortType.add(
          SortTypeModel(
            R.drawable.ic_feather_star_2,
            "Hightest rated",
            Color.parseColor("#FED57D"),
            Color.parseColor("#9c8860")
          )
        )

        listSortType.add(
          SortTypeModel(
            R.drawable.ic_feather_dollar_sign,
            "Best offer",
            Color.parseColor("#A1D881"),
            Color.parseColor("#76966a")
          )
        )
      }

      return gson.fromJson(gson.toJson(listSortType), Array<SortTypeModel>::class.java).toMutableList()
    }

    fun setActiveAtPosition(position: Int) {
      if (listSortType.size == 0) getListSortType()

      if (position >= listSortType.size) return

      listSortType.forEach { it.isActive = false }

      listSortType[position].isActive = position != currentActivePosition

      currentActivePosition = if (position == currentActivePosition) -1 else position
    }
  }

  private val sortTypes = getListSortType()

  class SortTypeViewHolder(val binding: SortTypeLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: SortTypeModel) {
      binding.sortType = data

      binding.executePendingBindings()
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SortTypeViewHolder(
    DataBindingUtil.inflate(
      LayoutInflater.from(parent.context),
      R.layout.sort_type_layout,
      parent,
      false
    )
  )

  override fun onBindViewHolder(holder: SortTypeViewHolder, position: Int) {
    holder.bindData(sortTypes[position])

  }

  override fun getItemCount(): Int = sortTypes.size

  fun updateListType() {
    val diffCallback = SortTypeDiffCallback(sortTypes, listSortType)

    val diffResult = DiffUtil.calculateDiff(diffCallback)

    sortTypes.clear()
    sortTypes.addAll(getListSortType())

    diffResult.dispatchUpdatesTo(this)
  }

  class SortTypeDiffCallback(
    val oldList: MutableList<SortTypeModel>,
    val newList: MutableList<SortTypeModel>
  ) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].title == newList[newItemPosition].title

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
      oldList[oldItemPosition].isActive == newList[newItemPosition].isActive

  }
}