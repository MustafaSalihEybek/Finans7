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
import com.finans7.model.categorynews.RootCategoryNews
import com.finans7.util.show
import com.finans7.viewmodel.SearchViewModel

class SearchFragment : Fragment() {
    private lateinit var v: View
    private lateinit var searchBinding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var searchedValue: String
    private lateinit var rootCategoryNews: RootCategoryNews
    private lateinit var newsLayoutManager: LinearLayoutManager
    private lateinit var newsByCategoryAdapter: NewsByCategoryAdapter

    private fun init(){
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        observeLiveData()

        searchBinding.searchFragmentEditSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()){
                searchedValue = it.toString()
                searchViewModel.getNewsBySearch(searchedValue, 0)
            } else {
                newsByCategoryAdapter = NewsByCategoryAdapter(arrayListOf(), v, true)
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

                searchBinding.searchFragmentRecyclerView.setHasFixedSize(true)
                newsLayoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
                searchBinding.searchFragmentRecyclerView.layoutManager = newsLayoutManager
                newsByCategoryAdapter = NewsByCategoryAdapter(rootCategoryNews.postList, v, true)
                searchBinding.searchFragmentRecyclerView.adapter = newsByCategoryAdapter
            }
        })
    }
}