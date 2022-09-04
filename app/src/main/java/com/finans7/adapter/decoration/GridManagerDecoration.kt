package com.finans7.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridManagerDecoration(private val vSize: Int, private val hSize: Int) : RecyclerView.ItemDecoration() {
    private var aPos: Int = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        aPos = parent.getChildAdapterPosition(view)

        if (aPos > 1)
            outRect.set(hSize, vSize, hSize, 0)
        else
            outRect.set(hSize, 0, hSize, 0)
    }
}