package com.finans7.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.finans7.R
import com.finans7.adapter.FourNewsAdapter
import com.finans7.adapter.NewsFragmentAdapter
import com.finans7.adapter.NewsImagesPagerAdapter
import com.finans7.adapter.decoration.GridManagerDecoration
import com.finans7.databinding.FragmentHomeBinding
import com.finans7.model.homepage.HomePageNews
import com.finans7.model.homepage.News
import com.finans7.util.AppUtil
import com.finans7.util.Singleton
import com.finans7.util.show
import com.finans7.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var v: View
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var navDirections: NavDirections

    private lateinit var homePageNews: HomePageNews
    private lateinit var imageUrlList: ArrayList<String>
    private lateinit var newsImagesPagerAdapter: NewsImagesPagerAdapter

    private lateinit var fourNewsAdapter: FourNewsAdapter
    private lateinit var newsByTitleAdapter: NewsFragmentAdapter

    private fun init(){
        homeBinding.homeFragmentRecyclerViewFourNews.setHasFixedSize(true)
        homeBinding.homeFragmentRecyclerViewFourNews.layoutManager = GridLayoutManager(v.context, 2)
        fourNewsAdapter = FourNewsAdapter(arrayListOf())
        homeBinding.homeFragmentRecyclerViewFourNews.adapter = fourNewsAdapter

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        observeLiveData()
        homeViewModel.getHomePageNews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        homeViewModel.homePageNews.observe(viewLifecycleOwner, Observer {
            it?.let {
                homePageNews = it

                loadSlide(homePageNews.AnaManset)
                loadFourNews(homePageNews.BesliManset)
                loadLastNews(homePageNews.YatayHaberler)
                loadHeadlineNews(homePageNews.GununManseti)
                loadMostReadNews(homePageNews.OzelHaberler)
            }
        })

        homeViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })
    }

    private fun loadSlide(mainHeadLine: List<News>){
        imageUrlList = ArrayList()

        for (headline in mainHeadLine){
            headline.MAINIMAGE?.let {
                imageUrlList.add(AppUtil.getPostImageUrl(it))
            }
        }

        newsImagesPagerAdapter = NewsImagesPagerAdapter(imageUrlList, v.context)

        homeBinding.homeFragmentViewPagerMainHeadline.adapter = newsImagesPagerAdapter
        homeBinding.homeFragmentViewPagerMainHeadline.isSaveEnabled = false

        homeBinding.homeFragmentCircleIndicator.setViewPager(homeBinding.homeFragmentViewPagerMainHeadline)
        newsImagesPagerAdapter.registerDataSetObserver(homeBinding.homeFragmentCircleIndicator.dataSetObserver)

        newsImagesPagerAdapter.setOnNewsSliderOnItemClickListener(object : NewsImagesPagerAdapter.NewsSliderOnClickListener{
            override fun onItemClick(newsIn: Int) {
                goToNewsPage(mainHeadLine, newsIn)
            }
        })
    }

    private fun loadFourNews(fourNews: List<News>){
        if (homeBinding.homeFragmentRecyclerViewFourNews.itemDecorationCount > 0)
            homeBinding.homeFragmentRecyclerViewFourNews.removeItemDecorationAt(0)

        homeBinding.homeFragmentRecyclerViewFourNews.addItemDecoration(GridManagerDecoration(Singleton.V_SIZE, Singleton.H_SIZE))
        fourNewsAdapter.loadData(fourNews)

        fourNewsAdapter.setFourNewsOnItemClickListener(object : FourNewsAdapter.FourNewsItemClickListener{
            override fun onItemClick(newsData: News, newsIn: Int) {
                goToNewsPage(arrayListOf(newsData), 0)
            }
        })
    }

    private fun loadLastNews(lastNews: List<News>){
        newsByTitleAdapter = NewsFragmentAdapter(this)

        for (n in lastNews.indices)
            newsByTitleAdapter.addFragment(NewsByTitleFragment(lastNews.get(n), lastNews, n))

        homeBinding.homeFragmentViewPagerLastNews.adapter = newsByTitleAdapter
        homeBinding.homeFragmentViewPagerLastNews.isSaveEnabled = false
    }

    private fun loadHeadlineNews(headlineNews: List<News>){
        newsByTitleAdapter = NewsFragmentAdapter(this)

        for (n in headlineNews.indices)
            newsByTitleAdapter.addFragment(NewsByTitleFragment(headlineNews.get(n), headlineNews, n))

        homeBinding.homeFragmentViewPagerHeadlineNews.adapter = newsByTitleAdapter
        homeBinding.homeFragmentViewPagerHeadlineNews.isSaveEnabled = false
    }

    private fun loadMostReadNews(mostReadNews: List<News>){
        newsByTitleAdapter = NewsFragmentAdapter(this)

        for (n in mostReadNews.indices)
            newsByTitleAdapter.addFragment(NewsByTitleFragment(mostReadNews.get(n), mostReadNews, n))

        homeBinding.homeFragmentViewPagerMostReadNews.adapter = newsByTitleAdapter
        homeBinding.homeFragmentViewPagerMostReadNews.isSaveEnabled = false
    }

    private fun goToNewsPage(newsList: List<News>, newsIn: Int){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(newsList.toTypedArray(), null, newsIn, false)
        Navigation.findNavController(v).navigate(navDirections)
    }
}