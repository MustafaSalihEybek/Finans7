package com.finans7.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finans7.R
import com.finans7.adapter.NewsByCategoryAdapter
import com.finans7.databinding.FragmentSearchBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.categorynews.RootCategoryNews
import com.finans7.util.Singleton
import com.finans7.util.show
import com.finans7.viewmodel.SearchViewModel

class SearchFragment : Fragment() {
    private lateinit var v: View
    private lateinit var searchBinding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var searchedValue: String
    private lateinit var rootCategoryNews: RootCategoryNews
    private lateinit var postList: ArrayList<PostListModel>
    private lateinit var newsLayoutManager: LinearLayoutManager
    private lateinit var newsByCategoryAdapter: NewsByCategoryAdapter

    private var lastCount: Int = 0
    private var lastData: Boolean = false

    private fun init(){
        searchBinding.searchFragmentRecyclerView.setHasFixedSize(true)
        newsLayoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        searchBinding.searchFragmentRecyclerView.layoutManager = newsLayoutManager

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        observeLiveData()

        if (Singleton.searchIsCreated)
            lastData = true

        searchBinding.searchFragmentEditSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()){
                if (!lastData){
                    searchedValue = it.toString()
                    searchViewModel.getNewsBySearch(searchedValue, 0)
                    Singleton.searchedValue = searchedValue
                }
            } else {
                searchedValue = ""
                postList = arrayListOf()
                Singleton.searchedValue = searchedValue

                newsByCategoryAdapter = NewsByCategoryAdapter(postList, v, true)
                searchBinding.searchFragmentRecyclerView.adapter = newsByCategoryAdapter
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(inflater)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()

        if (Singleton.searchIsCreated){
            searchBinding.searchFragmentEditSearch.setText(Singleton.searchedValue)
            postList = Singleton.postList
            lastData = false

            newsByCategoryAdapter = NewsByCategoryAdapter(postList, v, true)
            searchBinding.searchFragmentRecyclerView.adapter = newsByCategoryAdapter
        }
    }

    private fun observeLiveData(){
        searchViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        searchViewModel.rootCategoryNews.observe(viewLifecycleOwner, Observer {
            it?.let {
                rootCategoryNews = it
                postList = it.postList

                Singleton.postList = postList
                Singleton.searchIsCreated = true

                newsByCategoryAdapter = NewsByCategoryAdapter(postList, v, true)
                searchBinding.searchFragmentRecyclerView.adapter = newsByCategoryAdapter
            }
        })
    }
}