package com.finans7.adapter

import android.database.DataSetObserver
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class InfiniteScrollPagerAdapter(private val adapter: PagerAdapter) : PagerAdapter() {
    override fun getCount(): Int {
        if (getRealCount() == 0) {
            return 0;
        }
        // warning: scrolling to very high values (1,000,000+) results in
        // strange drawing behaviour
        return Integer.MAX_VALUE;
    }

    fun getRealCount() = adapter.count

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val virtualPosition = position % getRealCount()
        return adapter.instantiateItem(container, virtualPosition)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val virtualPosition = position % getRealCount()
        adapter.destroyItem(container, virtualPosition, `object`)
    }

    override fun finishUpdate(container: ViewGroup) {
        adapter.finishUpdate(container)
    }

    override fun isViewFromObject(view: View, `object`: Any) = adapter.isViewFromObject(view, `object`)

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        adapter.restoreState(state, loader)
    }

    override fun saveState(): Parcelable? {
        return adapter.saveState()
    }

    override fun startUpdate(container: ViewGroup) {
        adapter.startUpdate(container)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val virtualPosition = position % getRealCount()
        return adapter.getPageTitle(virtualPosition)
    }

    override fun getPageWidth(position: Int) = adapter.getPageWidth(position)

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        adapter.setPrimaryItem(container, position, `object`)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        adapter.unregisterDataSetObserver(observer)
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        adapter.registerDataSetObserver(observer)
    }

    override fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any) = adapter.getItemPosition(`object`)
}