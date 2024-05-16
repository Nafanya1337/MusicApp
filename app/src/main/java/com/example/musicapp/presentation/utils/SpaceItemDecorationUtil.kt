package com.example.musicapp.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecorationUtil(private val verticalSpaceHeight: Int, private val horizontalSpaceWidth: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = horizontalSpaceWidth
        outRect.right = horizontalSpaceWidth
        outRect.bottom = verticalSpaceHeight

    }
}