package com.finans7.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.finans7.R
import com.finans7.util.downloadImageUrl

class NewsImagesPagerAdapter(val imageUrlList: ArrayList<String>, val context: Context) : PagerAdapter() {
    private lateinit var listener: NewsSliderOnClickListener

    override fun getCount() = imageUrlList.size

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.slider_item, null)
        val imgView: ImageView = view.findViewById(R.id.slider_item_imgNews)
        Glide.with(context).load(imageUrlList.get(position)).override(600, 400).into(imgView)

        imgView.setOnClickListener {
            listener.onItemClick(position)
        }

        container.addView(view)
        return view
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