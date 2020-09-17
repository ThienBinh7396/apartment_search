package com.thienbinh.apartmentsearch.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecycleViewSpaceItemDecoration(
  private val spaceBottom: Int,
  private val spaceRight: Int
) :
  RecyclerView.ItemDecoration() {
  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    outRect.right = spaceRight
    outRect.bottom = spaceBottom
  }
}