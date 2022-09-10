package com.finans7.view

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.finans7.R
import com.finans7.adapter.NavCategoriesAdapter
import com.finans7.databinding.FragmentMainBinding
import com.finans7.model.category.RootCategory
import com.finans7.util.AppUtil
import com.finans7.util.Singleton
import com.finans7.util.show
import com.finans7.viewmodel.MainViewModel


class MainFragment : Fragment() {
    private lateinit var v: View
    private lateinit var mainBinding: FragmentMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var transaction: FragmentTransaction
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var fragmentList: Array<Fragment>

    private lateinit var categoryLit: RootCategory
    private lateinit var navCategoriesAdapter: NavCategoriesAdapter

    private lateinit var categoryFragment: CategoryFragment
    private var categoryRetrieved: Boolean = false

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

        fragmentList = arrayOf(HomeFragment(), SearchFragment())
        selectPage(0)

        mainBinding.mainFragmentRecyclerView.setHasFixedSize(true)
        mainBinding.mainFragmentRecyclerView.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        navCategoriesAdapter = NavCategoriesAdapter(arrayListOf(), v)
        mainBinding.mainFragmentRecyclerView.adapter = navCategoriesAdapter

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        observeLiveData()
        mainViewModel.getCategoryList()

        if (Singleton.themeMode.equals("Dark")){
            mainBinding.mainFragmentImgAppLogo.imageTintList = ColorStateList.valueOf(Color.WHITE)
            mainBinding.mainFragmentImgNavAppLogo.imageTintList = ColorStateList.valueOf(Color.WHITE)
        }
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
                    selectPage(0)
                    true
                }

                R.id.bottom_menu_search -> {
                    selectPage(1)
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

    private fun selectPage(sIn: Int){
        if (sIn >= 0 && sIn < fragmentList.size)
            setFragment(fragmentList.get(sIn))
    }

    private fun setFragment(fragment: Fragment){
        transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment_frameLayout, fragment)
        transaction.commit()
    }

    private fun observeLiveData(){
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
}