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
import com.finans7.R
import com.finans7.adapter.NewsFragmentAdapter
import com.finans7.databinding.FragmentNewsBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.homepage.News
import com.finans7.util.Singleton

class NewsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var newsBinding: FragmentNewsBinding
    private lateinit var newsFragmentAdapter: NewsFragmentAdapter

    private var newsList: Array<News>? = null
    private var postList: Array<PostListModel>? = null
    private var newsIn: Int = 0
    private var fromCategory: Boolean = false

    private fun init(){
        arguments?.let {
            newsList = NewsFragmentArgs.fromBundle(it).newsList
            postList = NewsFragmentArgs.fromBundle(it).postList
            newsIn = NewsFragmentArgs.fromBundle(it).newsIn
            fromCategory = NewsFragmentArgs.fromBundle(it).fromCategory

            newsFragmentAdapter = NewsFragmentAdapter(this)

            if (!fromCategory){
                newsList?.let {
                    for (new in it)
                        newsFragmentAdapter.addFragment(NewFragment(new))
                }
            } else {
                postList?.let {
                    for (post in it)
                        newsFragmentAdapter.addFragment(PostFragment(post))
                }
            }

            newsBinding.newsFragmentViewPager.adapter = newsFragmentAdapter

            Handler(Looper.getMainLooper()).postDelayed({
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