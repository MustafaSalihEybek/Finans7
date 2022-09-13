package com.finans7.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finans7.R
import com.finans7.adapter.NewsByCategoryAdapter
import com.finans7.databinding.FragmentNewsByCategoryBinding
import com.finans7.model.category.CategoryModel
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.categorynews.RootCategoryNews
import com.finans7.util.AppUtil
import com.finans7.util.Singleton
import com.finans7.util.show
import com.finans7.viewmodel.NewsByCategoryViewModel

class NewsByCategoryFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var newByCategoryBinding: FragmentNewsByCategoryBinding
    private lateinit var newsByCategoryViewModel: NewsByCategoryViewModel

    private var categoryData: CategoryModel? = null
    private var tagName: String? = null
    private lateinit var rootCategoryNews: RootCategoryNews
    private lateinit var postList: List<PostListModel>
    private lateinit var newsByCategoryAdapter: NewsByCategoryAdapter
    private lateinit var newsLayoutManager: LinearLayoutManager

    private fun init(){
        arguments?.let {
            categoryData = NewsByCategoryFragmentArgs.fromBundle(it).categoryData
            tagName = NewsByCategoryFragmentArgs.fromBundle(it).tagName

            newByCategoryBinding.newsByCategoryFragmentRecyclerView.setHasFixedSize(true)
            newsLayoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            newByCategoryBinding.newsByCategoryFragmentRecyclerView.layoutManager = newsLayoutManager
            newsByCategoryAdapter = NewsByCategoryAdapter(arrayListOf(), v, false)
            newByCategoryBinding.newsByCategoryFragmentRecyclerView.adapter = newsByCategoryAdapter

            newsByCategoryViewModel = ViewModelProvider(this).get(NewsByCategoryViewModel::class.java)
            observeLiveData()

            categoryData?.let {
                newByCategoryBinding.newsByCategoryFragmentTxtCategoryName.text = it.NAME
                newsByCategoryViewModel.getNewsByCategory(it.SLUG, 0)
            }

            tagName?.let {
                newByCategoryBinding.newsByCategoryFragmentTxtCategoryName.text = it
                newsByCategoryViewModel.getNewsByTag(it, 0)
            }

            Singleton.currentPage = "Category"
            Singleton.currentPageV = v
            Singleton.mContext = v.context

            newByCategoryBinding.newsByCategoryFragmentImgBack.setOnClickListener(this)
            newByCategoryBinding.newsByCategoryFragmentImgShare.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newByCategoryBinding = FragmentNewsByCategoryBinding.inflate(inflater)
        return newByCategoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.news_by_category_fragment_imgBack -> backToPage()
                R.id.news_by_category_fragment_imgShare -> shareNews()
            }
        }
    }

    private fun observeLiveData(){
        newsByCategoryViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        newsByCategoryViewModel.rootCategoryNews.observe(viewLifecycleOwner, Observer {
            it?.let {
                rootCategoryNews = it

                if (it.postList.isNotEmpty()){
                    postList = it.postList

                    newByCategoryBinding.newsByCategoryFragmentProgressBar.visibility = View.GONE
                    newByCategoryBinding.newsByCategoryFragmentRecyclerView.visibility = View.VISIBLE

                    newsByCategoryAdapter.loadData(it.postList)
                    attachUpcomingNewsOnScrollListener()
                }
            }
        })
    }

    private fun attachUpcomingNewsOnScrollListener(){
        newByCategoryBinding.newsByCategoryFragmentRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = newsLayoutManager.itemCount
                val visibleItemCount = newsLayoutManager.childCount
                val firstVisibleItem = newsLayoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2){
                    newByCategoryBinding.newsByCategoryFragmentRecyclerView.removeOnScrollListener(this)

                    categoryData?.let {
                        newsByCategoryViewModel.getNewsByCategory(it.SLUG, newsByCategoryAdapter.itemCount)
                    }

                    tagName?.let {
                        newsByCategoryViewModel.getNewsByTag(it, newsByCategoryAdapter.itemCount)
                    }
                }
            }
        })
    }

    private fun shareNews(){
        categoryData?.let {
            AppUtil.shareNews("${Singleton.BASE_URL}/kategori/${it.SLUG}", v.context)
        }

        tagName?.let {
            AppUtil.shareNews("${Singleton.BASE_URL}/tag/${it}", v.context)
        }
    }

    private fun backToPage(){
        Navigation.findNavController(v).popBackStack()
    }
}