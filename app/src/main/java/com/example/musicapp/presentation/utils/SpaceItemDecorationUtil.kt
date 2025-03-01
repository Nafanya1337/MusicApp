package com.example.musicapp.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecorationUtil(private val verticalSpaceHeight: Int, private val horizontalSpaceWidth: Int, private val countOfFirstElements: Int = 0) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val countOfElementsWithoutSpace = countOfFirstElements - 1
        outRect.left = if (position <= countOfElementsWithoutSpace) 0 else horizontalSpaceWidth
        outRect.right = horizontalSpaceWidth
        outRect.bottom = verticalSpaceHeight
    }
}