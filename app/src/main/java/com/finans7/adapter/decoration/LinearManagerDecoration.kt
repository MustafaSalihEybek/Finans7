package com.finans7.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LinearManagerDecoration(
    val vSize: Int,
    val hSize: Int,
    val aSize: Int,
    val vState: Boolean,
    val hState: Boolean
    ) : RecyclerView.ItemDecoration() {
    private var aPos: Int = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        aPos = parent.getChildAdapterPosition(view)

        if (!vState && hState){
            if (aPos != 0 && aPos < (aSize - 1))
                outRect.set(hSize, 0, hSize, 0)
            else if (aPos == 0)
                outRect.set((hSize * 2), 0, hSize, 0)
            else
                outRect.set(hSize, 0, 0, 0)
        } else if (vState && !hState){
            if (aPos < (aSize - 1))
                outRect.bottom = (vSize * 2)
        }
    }
}