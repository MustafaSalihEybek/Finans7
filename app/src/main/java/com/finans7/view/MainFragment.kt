package com.finans7.view

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.finans7.R
import com.finans7.adapter.NavCategoriesAdapter
import com.finans7.databinding.FragmentMainBinding
import com.finans7.model.category.RootCategory
import com.finans7.util.AppUtil
import com.finans7.util.SharedPreferences
import com.finans7.util.Singleton
import com.finans7.util.show
import com.finans7.viewmodel.MainViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var mainBinding: FragmentMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navDirections: NavDirections

    private lateinit var transaction: FragmentTransaction
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var fragmentList: Array<Fragment>

    private lateinit var categoryLit: RootCategory
    private lateinit var navCategoriesAdapter: NavCategoriesAdapter

    private lateinit var categoryFragment: CategoryFragment
    private var categoryRetrieved: Boolean = false

    private lateinit var socialIntent: Intent
    private var selectedPageIn: Int = 0

    private lateinit var sharedPreferences: SharedPreferences
    private var userTopic: Boolean = false

    private var currentScrollPosY: Int = 0
    private var minusScrollY: Int = 100

    private fun init(){
        mToggle = ActionBarDrawerToggle(
            (v.context as Activity),
            mainBinding.mainFragmentDrawerLayout,
            mainBinding.mainFragmentCustomAppBar,
            R.string.nav_open, R.string.nav_close
        )
        mToggle.drawerArrowDrawable.color = AppUtil.getColorByIntFromAttr(v.context, R.attr.appBarMenuColor)
        mainBinding.mainFragmentDrawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()

        sharedPreferences = SharedPreferences(v.context)
        userTopic = sharedPreferences.getUserTopic()

        fragmentList = arrayOf(HomeFragment(), SearchFragment())

        mainBinding.mainFragmentRecyclerView.setHasFixedSize(true)
        mainBinding.mainFragmentRecyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        navCategoriesAdapter = NavCategoriesAdapter(arrayListOf(), v)
        mainBinding.mainFragmentRecyclerView.adapter = navCategoriesAdapter

        Singleton.setSoftInput(1)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        observeLiveData()
        mainViewModel.getCategoryList()

        if (!userTopic){
            mainViewModel.generateUserTopic(
                "Android",
                AppUtil.getDeviceId(v.context),
                AppUtil.getDeviceName(),
                AppUtil.getDeviceVersion()
            )
        }

        if (Singleton.themeMode.equals("Dark")){
            mainBinding.mainFragmentImgAppLogo.imageTintList = ColorStateList.valueOf(Color.WHITE)
            mainBinding.mainFragmentImgNavAppLogo.imageTintList = ColorStateList.valueOf(Color.WHITE)
        }

        mainBinding.mainFragmentImgTwitter.setOnClickListener(this)
        mainBinding.mainFragmentImgInstagram.setOnClickListener(this)
        mainBinding.navHeaderLinearSettings.setOnClickListener(this)
        mainBinding.customAppBarImgShare.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return mainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()

        mainBinding.mainFragmentBottomNavBar.setOnItemSelectedListener {
            when (it.itemId){
                R.id.bottom_menu_home -> {
                    if (!Singleton.currentIsHome)
                        selectPage(0)
                    else {
                        currentScrollPosY = Singleton.nestedScroll.scrollY
                        scrollToFirstPosition()
                    }

                    true
                }

                R.id.bottom_menu_search -> {
                    selectPage(1)
                    Singleton.currentIsHome = false
                    true
                }

                R.id.bottom_menu_all_news -> {
                    if (categoryRetrieved) {
                        categoryFragment = CategoryFragment(categoryLit.AllCategoryList, v)
                        categoryFragment.show(childFragmentManager, "Categories")
                    }
                    true
                }

                else -> false
            }
        }
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.main_fragment_imgTwitter -> AppUtil.goToTwitterPage(v.context)
                R.id.main_fragment_imgInstagram -> AppUtil.goToInstagramPage(v.context)
                R.id.nav_header_linearSettings -> goToSettingsPage()
                R.id.custom_app_bar_imgShare -> AppUtil.shareNews(Singleton.BASE_URL, v.context)
            }
        }
    }

    private fun goToSettingsPage(){
        navDirections = MainFragmentDirections.actionMainFragmentToSettingsFragment()
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun selectPage(sIn: Int){
        if (sIn >= 0 && sIn < fragmentList.size){
            setFragment(fragmentList.get(sIn))
            Singleton.selectedPageIn = sIn
        }
    }

    private fun setFragment(fragment: Fragment){
        transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment_frameLayout, fragment)
        transaction.commit()
    }

    private fun observeLiveData(){
        mainViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.equals("Topic token başarıyla oluşturuldu"))
                    sharedPreferences.saveUserTopic()
            }
        })

        mainViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        mainViewModel.categoryList.observe(viewLifecycleOwner, Observer {
            it?.let {
                categoryLit = it
                navCategoriesAdapter.loadData(it.MainCategoryList)

                categoryRetrieved = true
            }
        })
    }

    /*private fun generateFirebaseToken(){
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            println("Token: $it")
        }
    }*/

    private fun scrollToFirstPosition(){
        Handler(Looper.getMainLooper()).postDelayed({
            if ((currentScrollPosY - minusScrollY) > 0)
                currentScrollPosY-= minusScrollY
            else
                currentScrollPosY = 0

            Singleton.nestedScroll.scrollTo(0, currentScrollPosY)

            if (currentScrollPosY > 0)
                scrollToFirstPosition()
        }, 10)
    }

    override fun onResume() {
        super.onResume()
        selectedPageIn = Singleton.selectedPageIn
        selectPage(selectedPageIn)
    }
}