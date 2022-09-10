package com.finans7.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.finans7.util.downloadImageUrl

class NewsImagesPagerAdapter(val imageUrlList: ArrayList<String>, val context: Context) : PagerAdapter() {
    private lateinit var imgPage: ImageView
    private lateinit var listener: NewsSliderOnClickListener

    override fun getCount() = imageUrlList.size

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        imgPage = ImageView(context)
        imgPage.scaleType = ImageView.ScaleType.FIT_XY
        imgPage.downloadImageUrl(imageUrlList.get(position))

        imgPage.setOnClickListener {
            listener.onItemClick(position)
        }

        container.addView(imgPage)
        return imgPage
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    interface NewsSliderOnClickListener{
        fun onItemClick(newsIn: Int)
    }

    fun setOnNewsSliderOnItemClickListener(listener: NewsSliderOnClickListener){
        this.listener = listener
    }
}