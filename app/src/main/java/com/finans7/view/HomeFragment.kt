package com.finans7.view

import android.os.Bundle
import android.os.CountDownTimer
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.finans7.R
import com.finans7.adapter.*
import com.finans7.adapter.decoration.GridManagerDecoration
import com.finans7.adapter.decoration.LinearManagerDecoration
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

    private lateinit var trendingNewsList: ArrayList<News>
    private lateinit var trendingNewsAdapter: TrendingNewsAdapter

    private lateinit var homeCategoryNewsAdapters: HomeCategoryNewsAdapters

    private lateinit var categoryList: ArrayList<String>
    private lateinit var categoryNewsList: ArrayList<ArrayList<News>>
    private lateinit var homeNewsByCategoryAdapters: HomeNewsByCategoryAdapters

    private var isLastItem: Boolean = false
    private var isFirstItem: Boolean = false

    private fun init(){
        homeBinding.homeFragmentRecyclerViewFourNews.setHasFixedSize(true)
        homeBinding.homeFragmentRecyclerViewFourNews.layoutManager = GridLayoutManager(v.context, 2)
        fourNewsAdapter = FourNewsAdapter(arrayListOf())
        homeBinding.homeFragmentRecyclerViewFourNews.adapter = fourNewsAdapter

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        observeLiveData()
        homeViewModel.getHomePageNews()

        AppUtil.showSplashDialog(v.context)
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

                categoryList = getCategoryList(it.KategoriHaberleri)
                categoryNewsList = getNewsListByCategory(categoryList, it.KategoriHaberleri)

                loadSlide(homePageNews.AnaManset)
                loadFourNews(homePageNews.BesliManset)
                loadLastNews(homePageNews.YatayHaberler)
                loadHeadlineNews(homePageNews.GununManseti)
                loadTrendingNews(homePageNews.SagManset)
                loadMostReadNews(homePageNews.OzelHaberler)
                loadInterestingNews(homePageNews.HaberBandı)
                loadNewsByCategory(Pair(categoryList, categoryNewsList))

                AppUtil.closeSplashDialog()
            }
        })

        homeViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })
    }

    private fun getCategoryList(newsList: List<News>) : ArrayList<String> {
        val categoryList: ArrayList<String> = ArrayList()

        for (news in newsList){
            news.TERMNAME?.let {
                if (!categoryList.contains(it))
                    categoryList.add(it)
            }
        }

        return categoryList
    }

    private fun getNewsListByCategory(categoryList: ArrayList<String>, newsList: List<News>) : ArrayList<ArrayList<News>> {
        val categoryNewsList: ArrayList<ArrayList<News>> = ArrayList()
        lateinit var newsArr: ArrayList<News>

        for (category in categoryList){
            newsArr = ArrayList()

            for (news in newsList){
                if (news.TERMNAME.equals(category))
                    newsArr.add(news)
            }

            categoryNewsList.add(newsArr)
        }

        return categoryNewsList
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

        homeBinding.homeFragmentViewPagerMainHeadline.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
                /*if (state == ViewPager.SCROLL_STATE_IDLE){
                    if (homeBinding.homeFragmentViewPagerMainHeadline.currentItem == 0 && !isFirstItem)
                        isFirstItem = true
                    else if (homeBinding.homeFragmentViewPagerMainHeadline.currentItem == (imageUrlList.size - 1) && !isLastItem)
                        isLastItem = true

                    if (isLastItem) {
                        homeBinding.homeFragmentViewPagerMainHeadline.currentItem = 0
                        saveScrollState("Last")
                    }

                    if (isFirstItem){
                        homeBinding.homeFragmentViewPagerMainHeadline.currentItem = (imageUrlList.size - 1)
                        saveScrollState("First")
                    }
                }*/
            }
        })

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

    private fun loadTrendingNews(trendingNews: List<News>){
        newsByTitleAdapter = NewsFragmentAdapter(this)
        trendingNewsList = ArrayList()

        for (n in trendingNews.indices){
            newsByTitleAdapter.addFragment(NewsByTitleFragment(trendingNews.get(n), trendingNews, n))

            if (n >= 4)
                trendingNewsList.add(trendingNews.get(n))
        }

        homeBinding.homeFragmentViewPagerTrendingNews.adapter = newsByTitleAdapter
        homeBinding.homeFragmentViewPagerTrendingNews.isSaveEnabled = false

        homeBinding.homeFragmentRecyclerViewTrendingNews.setHasFixedSize(true)
        homeBinding.homeFragmentRecyclerViewTrendingNews.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        trendingNewsAdapter = TrendingNewsAdapter(trendingNewsList)
        homeBinding.homeFragmentRecyclerViewTrendingNews.adapter = trendingNewsAdapter
    }

    private fun loadMostReadNews(mostReadNews: List<News>){
        newsByTitleAdapter = NewsFragmentAdapter(this)

        for (n in mostReadNews.indices)
            newsByTitleAdapter.addFragment(NewsByTitleFragment(mostReadNews.get(n), mostReadNews, n))

        homeBinding.homeFragmentViewPagerMostReadNews.adapter = newsByTitleAdapter
        homeBinding.homeFragmentViewPagerMostReadNews.isSaveEnabled = false
    }

    private fun loadInterestingNews(interestingNews: List<News>){
        homeBinding.homeFragmentRecyclerViewInterestingNews.setHasFixedSize(true)
        homeBinding.homeFragmentRecyclerViewInterestingNews.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        homeCategoryNewsAdapters = HomeCategoryNewsAdapters(interestingNews)
        homeBinding.homeFragmentRecyclerViewInterestingNews.adapter = homeCategoryNewsAdapters
    }

    private fun loadNewsByCategory(categoryNewsPair: Pair<ArrayList<String>, ArrayList<ArrayList<News>>>){
        homeBinding.homeFragmentRecyclerViewNewsByCategory.setHasFixedSize(true)
        homeBinding.homeFragmentRecyclerViewNewsByCategory.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        homeBinding.homeFragmentRecyclerViewNewsByCategory.addItemDecoration(LinearManagerDecoration(Singleton.V_SIZE, Singleton.H_SIZE, categoryNewsPair.first.size, true, false))
        homeNewsByCategoryAdapters = HomeNewsByCategoryAdapters(categoryNewsPair, v)
        homeBinding.homeFragmentRecyclerViewNewsByCategory.adapter = homeNewsByCategoryAdapters
    }

    private fun saveScrollState(scrollName: String){
        object : CountDownTimer(200, 100){
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                if (scrollName.equals("Last"))
                    isLastItem = false
                else
                    isFirstItem = false
            }
        }.start()
    }

    private fun goToNewsPage(newsList: List<News>, newsIn: Int){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(newsList.toTypedArray(), null, newsIn, false)
        Navigation.findNavController(v).navigate(navDirections)
    }
}