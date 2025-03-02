package com.example.musicapp.presentation.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecorationUtil(
    private val verticalSpaceHeight: Int,
    private val horizontalSpaceWidth: Int,
    private val countOfFirstElements: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val countOfElements = parent.adapter?.itemCount ?: 0

        // Если элементов нет, просто выходим.
        if (countOfElements == 0) {
            return
        }

        // Если countOfFirstElements <= 0, применяем дефолтные отступы
        if (countOfFirstElements == 0) {
            outRect.left = horizontalSpaceWidth
            outRect.right = horizontalSpaceWidth
            outRect.bottom = verticalSpaceHeight
            return
        }

        // Исходные вычисления, когда countOfFirstElements > 0
        val countOfElementsWithoutSpace = countOfFirstElements - 1
        val countOfLastElementsWithoutSpace = countOfElements % countOfFirstElements
        val firstElementInEndWithoutSpace =
            if (countOfFirstElements != 1) countOfElements - countOfLastElementsWithoutSpace
            else countOfElements - countOfFirstElements

        outRect.left = if (position <= countOfElementsWithoutSpace ) 0 else horizontalSpaceWidth
        //outRect.right = if (position >= firstElementInEndWithoutSpace) 0 else horizontalSpaceWidth
        outRect.bottom = verticalSpaceHeight
    }
}
