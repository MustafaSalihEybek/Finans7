package com.finans7.view

import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.finans7.R
import com.finans7.adapter.*
import com.finans7.adapter.decoration.GridManagerDecoration
import com.finans7.adapter.decoration.LinearManagerDecoration
import com.finans7.databinding.FragmentHomeBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.homepage.HomePageNews
import com.finans7.util.AppUtil
import com.finans7.util.Singleton
import com.finans7.util.show
import com.finans7.viewmodel.HomeViewModel

class HomeFragment() : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var navDirections: NavDirections

    private lateinit var homePageNews: HomePageNews
    private lateinit var imageUrlList: ArrayList<String>
    private lateinit var newsImagesPagerAdapter: NewsImagesPagerAdapter

    private lateinit var fourNewsAdapter: FourNewsAdapter
    private lateinit var newsByTitleAdapter: NewsFragmentAdapter

    private lateinit var trendingNewsList: ArrayList<PostListModel>
    private lateinit var trendingNewsAdapter: TrendingNewsAdapter

    private lateinit var homeCategoryNewsAdapters: HomeCategoryNewsAdapters

    private lateinit var categoryList: ArrayList<String>
    private lateinit var categoryNewsList: ArrayList<ArrayList<PostListModel>>
    private lateinit var homeNewsByCategoryAdapters: HomeNewsByCategoryAdapters

    private var isLastItem: Boolean = false
    private var isFirstItem: Boolean = false
    private var loadLastData: Boolean = false
    private var amount: Int = 0

    private fun init(){
        homeBinding.homeFragmentRecyclerViewFourNews.setHasFixedSize(true)
        homeBinding.homeFragmentRecyclerViewFourNews.layoutManager = GridLayoutManager(v.context, 2)
        fourNewsAdapter = FourNewsAdapter(arrayListOf())
        homeBinding.homeFragmentRecyclerViewFourNews.adapter = fourNewsAdapter

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        observeLiveData()

        if (!Singleton.homeIsCreated){
            homeViewModel.getHomePageNews()
            AppUtil.showSplashDialog(v.context)
        }

        homeBinding.homeFragmentImgLastNewsRight.setOnClickListener(this)
        homeBinding.homeFragmentImgLastNewsLeft.setOnClickListener(this)
        homeBinding.homeFragmentImgHeadlineNewsRight.setOnClickListener(this)
        homeBinding.homeFragmentImgHeadlineNewsLeft.setOnClickListener(this)
        homeBinding.homeFragmentImgTrendingNewsRight.setOnClickListener(this)
        homeBinding.homeFragmentImgTrendingNewsLeft.setOnClickListener(this)
        homeBinding.homeFragmentImgMostReadNewsRight.setOnClickListener(this)
        homeBinding.homeFragmentImgMostReadNewsLeft.setOnClickListener(this)
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

        if (Singleton.homeIsCreated){
            homePageNews = Singleton.homePageNews
            loadAllData(homePageNews)
        }
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.home_fragment_imgLastNewsRight -> nextNews(homeBinding.homeFragmentViewPagerLastNews, homePageNews.yatayHaberler, true)
                R.id.home_fragment_imgLastNewsLeft -> nextNews(homeBinding.homeFragmentViewPagerLastNews, homePageNews.yatayHaberler, false)
                R.id.home_fragment_imgHeadlineNewsRight -> nextNews(homeBinding.homeFragmentViewPagerHeadlineNews, homePageNews.gununManseti, true)
                R.id.home_fragment_imgHeadlineNewsLeft -> nextNews(homeBinding.homeFragmentViewPagerHeadlineNews, homePageNews.gununManseti, false)
                R.id.home_fragment_imgTrendingNewsRight -> nextNews(homeBinding.homeFragmentViewPagerTrendingNews, homePageNews.sagManset, true)
                R.id.home_fragment_imgTrendingNewsLeft -> nextNews(homeBinding.homeFragmentViewPagerTrendingNews, homePageNews.sagManset, false)
                R.id.home_fragment_imgMostReadNewsRight -> nextNews(homeBinding.homeFragmentViewPagerMostReadNews, homePageNews.ozelHaberler, true)
                R.id.home_fragment_imgMostReadNewsLeft -> nextNews(homeBinding.homeFragmentViewPagerMostReadNews, homePageNews.ozelHaberler, false)
            }
        }
    }

    private fun observeLiveData(){
        homeViewModel.homePageNews.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (!Singleton.homeIsCreated){
                    homePageNews = it
                    loadAllData(homePageNews)

                    Singleton.homePageNews = homePageNews
                    Singleton.homeIsCreated = true
                    closeSplashDialog()
                }
            }
        })

        homeViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })
    }

    private fun loadAllData(homePageNews: HomePageNews){
        categoryList = getCategoryList(homePageNews.kategoriHaberleri)
        categoryNewsList = getNewsListByCategory(categoryList, homePageNews.kategoriHaberleri)

        loadSlide(homePageNews.anaManset)
        loadFourNews(homePageNews.besliManset)
        loadLastNews(homePageNews.yatayHaberler)
        loadHeadlineNews(homePageNews.gununManseti)
        loadTrendingNews(homePageNews.sagManset)
        loadMostReadNews(homePageNews.ozelHaberler)
        loadInterestingNews(homePageNews.haberBandı)
        loadNewsByCategory(Pair(categoryList, categoryNewsList))
        AppUtil.loadFooterFragment(R.id.home_fragment_footerFrameLayout, childFragmentManager, true)

        homeBinding.homeFragmentNestedScrollView.fling(0)
        Singleton.nestedScroll = homeBinding.homeFragmentNestedScrollView

        if (Singleton.homeIsCreated){
            Handler(Looper.getMainLooper()).postDelayed({
                homeBinding.homeFragmentNestedScrollView.scrollTo(Singleton.scrollXPosition, Singleton.scrollYPosition)
            }, 25)
        }
    }

    private fun getCategoryList(newsList: List<PostListModel>) : ArrayList<String> {
        val categoryList: ArrayList<String> = ArrayList()

        for (news in newsList){
            if (!categoryList.contains(news.termname))
                categoryList.add(news.termname)
        }

        return categoryList
    }

    private fun getNewsListByCategory(categoryList: ArrayList<String>, newsList: List<PostListModel>) : ArrayList<ArrayList<PostListModel>> {
        val categoryNewsList: ArrayList<ArrayList<PostListModel>> = ArrayList()
        lateinit var newsArr: ArrayList<PostListModel>

        for (category in categoryList){
            newsArr = ArrayList()

            for (news in newsList){
                if (news.termname.equals(category))
                    newsArr.add(news)
            }

            categoryNewsList.add(newsArr)
        }

        return categoryNewsList
    }

    private fun loadSlide(mainHeadLine: List<PostListModel>){
        imageUrlList = ArrayList()

        for (headline in mainHeadLine)
            imageUrlList.add(AppUtil.getPostImageUrl(headline.mainimage))

        newsImagesPagerAdapter = NewsImagesPagerAdapter(imageUrlList, v.context)

        homeBinding.homeFragmentViewPagerMainHeadline.adapter = newsImagesPagerAdapter
        homeBinding.homeFragmentViewPagerMainHeadline.isSaveEnabled = false

        if (Singleton.homeIsCreated)
            homeBinding.homeFragmentViewPagerMainHeadline.currentItem = Singleton.sliderCurrentPage

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

    private fun loadFourNews(fourNews: List<PostListModel>){
        if (homeBinding.homeFragmentRecyclerViewFourNews.itemDecorationCount > 0)
            homeBinding.homeFragmentRecyclerViewFourNews.removeItemDecorationAt(0)

        homeBinding.homeFragmentRecyclerViewFourNews.addItemDecoration(GridManagerDecoration(Singleton.V_SIZE, Singleton.H_SIZE))
        fourNewsAdapter.loadData(fourNews)

        fourNewsAdapter.setFourNewsOnItemClickListener(object : FourNewsAdapter.FourNewsItemClickListener{
            override fun onItemClick(newsData: PostListModel, newsIn: Int) {
                goToNewsPage(arrayListOf(newsData), 0)
            }
        })
    }

    private fun loadLastNews(lastNews: List<PostListModel>){
        newsByTitleAdapter = NewsFragmentAdapter(this)

        for (n in lastNews.indices)
            newsByTitleAdapter.addFragment(NewsByTitleFragment(lastNews.get(n), lastNews, n))

        homeBinding.homeFragmentViewPagerLastNews.adapter = newsByTitleAdapter
        homeBinding.homeFragmentViewPagerLastNews.isSaveEnabled = false

        if (Singleton.homeIsCreated){
            println("${Singleton.lastNewsCurrentPage} => ${homeBinding.homeFragmentViewPagerLastNews.currentItem}")

            Handler(Looper.getMainLooper()).postDelayed({
                homeBinding.homeFragmentViewPagerLastNews.currentItem = Singleton.lastNewsCurrentPage
            }, 25)
        }
    }

    private fun loadHeadlineNews(headlineNews: List<PostListModel>){
        newsByTitleAdapter = NewsFragmentAdapter(this)

        for (n in headlineNews.indices)
            newsByTitleAdapter.addFragment(NewsByTitleFragment(headlineNews.get(n), headlineNews, n))

        homeBinding.homeFragmentViewPagerHeadlineNews.adapter = newsByTitleAdapter
        homeBinding.homeFragmentViewPagerHeadlineNews.isSaveEnabled = false

        if (Singleton.homeIsCreated){
            Handler(Looper.getMainLooper()).postDelayed({
                homeBinding.homeFragmentViewPagerHeadlineNews.currentItem = Singleton.headlineNewsCurrentPage
            }, 25)
        }
    }

    private fun loadTrendingNews(trendingNews: List<PostListModel>){
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
        trendingNewsAdapter = TrendingNewsAdapter(trendingNewsList, v)
        homeBinding.homeFragmentRecyclerViewTrendingNews.adapter = trendingNewsAdapter

        if (Singleton.homeIsCreated){
            Handler(Looper.getMainLooper()).postDelayed({
                homeBinding.homeFragmentViewPagerTrendingNews.currentItem = Singleton.trendingNewsCurrentPage
            }, 25)
        }
    }

    private fun loadMostReadNews(mostReadNews: List<PostListModel>){
        newsByTitleAdapter = NewsFragmentAdapter(this)

        for (n in mostReadNews.indices)
            newsByTitleAdapter.addFragment(NewsByTitleFragment(mostReadNews.get(n), mostReadNews, n))

        homeBinding.homeFragmentViewPagerMostReadNews.adapter = newsByTitleAdapter
        homeBinding.homeFragmentViewPagerMostReadNews.isSaveEnabled = false

        if (Singleton.homeIsCreated){
            Handler(Looper.getMainLooper()).postDelayed({
                homeBinding.homeFragmentViewPagerMostReadNews.currentItem = Singleton.mostNewsCurrentPage
            }, 25)
        }
    }

    private fun loadInterestingNews(interestingNews: List<PostListModel>){
        homeBinding.homeFragmentRecyclerViewInterestingNews.setHasFixedSize(true)
        homeBinding.homeFragmentRecyclerViewInterestingNews.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        homeCategoryNewsAdapters = HomeCategoryNewsAdapters(interestingNews, v)
        homeBinding.homeFragmentRecyclerViewInterestingNews.adapter = homeCategoryNewsAdapters
    }

    private fun loadNewsByCategory(categoryNewsPair: Pair<ArrayList<String>, ArrayList<ArrayList<PostListModel>>>){
        homeBinding.homeFragmentRecyclerViewNewsByCategory.setHasFixedSize(true)
        homeBinding.homeFragmentRecyclerViewNewsByCategory.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        homeBinding.homeFragmentRecyclerViewNewsByCategory.addItemDecoration(LinearManagerDecoration(Singleton.V_SIZE, Singleton.H_SIZE, categoryNewsPair.first.size, true, false))
        homeNewsByCategoryAdapters = HomeNewsByCategoryAdapters(categoryNewsPair, v)
        homeBinding.homeFragmentRecyclerViewNewsByCategory.adapter = homeNewsByCategoryAdapters
    }

    private fun nextNews(viewPager: ViewPager2, newsList: List<PostListModel>, isNext: Boolean){
        if (isNext){
            if (viewPager.currentItem < (newsList.size - 1))
                viewPager.currentItem++
        } else {
            if (viewPager.currentItem > 0)
                viewPager.currentItem--
        }
    }

    private fun closeSplashDialog(){
        Handler(Looper.getMainLooper()).postDelayed({
            AppUtil.closeSplashDialog()
        }, 1000)
    }

    override fun onResume() {
        super.onResume()
        Singleton.currentIsHome = true
    }

    override fun onPause() {
        super.onPause()
        amount++

        if (amount == 1){
            Singleton.sliderCurrentPage = homeBinding.homeFragmentViewPagerMainHeadline.currentItem
            Singleton.lastNewsCurrentPage = homeBinding.homeFragmentViewPagerLastNews.currentItem
            Singleton.headlineNewsCurrentPage = homeBinding.homeFragmentViewPagerHeadlineNews.currentItem
            Singleton.trendingNewsCurrentPage = homeBinding.homeFragmentViewPagerTrendingNews.currentItem
            Singleton.mostNewsCurrentPage = homeBinding.homeFragmentViewPagerMostReadNews.currentItem
            Singleton.scrollXPosition = homeBinding.homeFragmentNestedScrollView.scrollX
            Singleton.scrollYPosition = homeBinding.homeFragmentNestedScrollView.scrollY
            Singleton.currentIsHome = false
        }
    }

    private fun goToNewsPage(newsList: List<PostListModel>, newsIn: Int){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(newsList.toTypedArray(), newsIn)
        Navigation.findNavController(v).navigate(navDirections)
    }
}