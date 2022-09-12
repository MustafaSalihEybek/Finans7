package com.finans7.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class InfiniteScrollViewPager : ViewPager {
    constructor(context: Context) : super(context)

    constructor(context: Context, attr: AttributeSet) : super(context, attr)

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        currentItem = 0
    }

    override fun setCurrentItem(item: Int) {
        setCurrentItem(item, false)
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        var nItem: Int = item

        if (adapter!!.count == 0) {
            super.setCurrentItem(nItem, smoothScroll)
            return
        }
        nItem = getOffsetAmount() + nItem % adapter!!.count
        super.setCurrentItem(nItem, smoothScroll)
    }

    override fun getCurrentItem(): Int {
        if (adapter!!.count == 0) {
            return super.getCurrentItem()
        }
        val position = super.getCurrentItem()
        return if (adapter is InfiniteScrollPagerAdapter) {
            val infAdapter: InfiniteScrollPagerAdapter? = adapter as InfiniteScrollPagerAdapter?
            return (position % infAdapter!!.getRealCount());
        } else {
            super.getCurrentItem()
        }
    }

    private fun getOffsetAmount(): Int {
        if (adapter!!.count == 0) {
            return 0
        }
        return if (adapter is InfiniteScrollPagerAdapter) {
            val infAdapter: InfiniteScrollPagerAdapter? = adapter as InfiniteScrollPagerAdapter?
            return infAdapter!!.getRealCount() * 100;
        } else {
            0
        }
    }
}