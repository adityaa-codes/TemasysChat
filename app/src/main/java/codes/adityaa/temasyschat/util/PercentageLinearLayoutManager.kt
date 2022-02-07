package com.world.suuz.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PercentageLinearLayoutManager(context: Context, private val percentage: Float) :
    LinearLayoutManager(context, HORIZONTAL, false) {
    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        lp?.let {
            it.width = (width * percentage).toInt()
        }
        return true
    }
}
