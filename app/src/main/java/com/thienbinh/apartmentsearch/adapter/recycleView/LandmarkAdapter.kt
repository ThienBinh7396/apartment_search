package com.thienbinh.apartmentsearch.adapter.recycleView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.LandmarkIntroduceLayoutBinding
import com.thienbinh.apartmentsearch.model.LandMarkModel
import com.thienbinh.apartmentsearch.model.listLandMarks

class LandmarkAdapter : RecyclerView.Adapter<LandmarkAdapter.LandmarkViewHolder>() {

  class LandmarkViewHolder(val binding: LandmarkIntroduceLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(data: LandMarkModel) {
      binding.landMark = data
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LandmarkViewHolder(
    DataBindingUtil.inflate(
      LayoutInflater.from(parent.context),
      R.layout.landmark_introduce_layout,
      null,
      false
    )
  )

  override fun onBindViewHolder(holder: LandmarkViewHolder, position: Int) {
    holder.bindData(listLandMarks[position])
  }

  override fun getItemCount() = listLandMarks.size
}