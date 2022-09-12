package com.finans7.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.finans7.R
import com.finans7.adapter.NewsFragmentAdapter
import com.finans7.databinding.FragmentNewsBinding
import com.finans7.util.Singleton

class NewsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var newsBinding: FragmentNewsBinding
    private lateinit var newsFragmentAdapter: NewsFragmentAdapter

    private lateinit var postIdList: IntArray
    private var newsIn: Int = 0

    private fun init(){
        arguments?.let {
            postIdList = NewsFragmentArgs.fromBundle(it).postIdList
            newsIn = NewsFragmentArgs.fromBundle(it).newsIn

            newsFragmentAdapter = NewsFragmentAdapter(this)

            Singleton.postDetailIsCreated = false

            for (id in postIdList)
                newsFragmentAdapter.addFragment(PostFragment(id))

            newsBinding.newsFragmentViewPager.adapter = newsFragmentAdapter
            Singleton.postDetailSlider = newsBinding.newsFragmentViewPager

            newsBinding.newsFragmentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {}

                override fun onPageSelected(position: Int) {
                    Singleton.postDetailSliderIn = position
                }

                override fun onPageScrollStateChanged(state: Int) {
                    Singleton.postDetailIsCreated = false
                }
            })

            Handler(Looper.getMainLooper()).postDelayed({
                if (Singleton.postDetailSliderIn != 0)
                    newsBinding.newsFragmentViewPager.setCurrentItem(Singleton.postDetailSliderIn, false)
                else
                    newsBinding.newsFragmentViewPager.setCurrentItem(newsIn, false)
            }, 100)

            newsBinding.newsFragmentImgBack.setOnClickListener(this)

            if (Singleton.themeMode.equals("Dark"))
                newsBinding.newsFragmentImgAppLogo.imageTintList = ColorStateList.valueOf(Color.WHITE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newsBinding = FragmentNewsBinding.inflate(inflater)
        return newsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.news_fragment_imgBack -> backToPage()
            }
        }
    }

    private fun backToPage(){
        Navigation.findNavController(v).popBackStack()
    }
}