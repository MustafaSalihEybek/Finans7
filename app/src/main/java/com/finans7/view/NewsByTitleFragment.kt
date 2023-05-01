package com.finans7.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.finans7.databinding.FragmentNewsByTitleBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.util.AppUtil

class NewsByTitleFragment(val newsData: PostListModel, val newsList: List<PostListModel>, val newsIn: Int) : Fragment() {
    private lateinit var v: View
    private lateinit var newsByTitleBinding: FragmentNewsByTitleBinding
    private lateinit var navDirections: NavDirections

    fun NewsByTitleFragment() {
        // doesn't do anything special
    }

    private fun init(){
        newsByTitleBinding.newdata = newsData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newsByTitleBinding = FragmentNewsByTitleBinding.inflate(inflater)
        return newsByTitleBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()

        newsByTitleBinding.newsByTitleFragmentLinearNews.setOnClickListener {
            goToNewsPage(newsList, newsIn)
        }
    }

    private fun goToNewsPage(newsList: List<PostListModel>, newsIn: Int){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(AppUtil.getPostIdList(newsList), newsIn)
        Navigation.findNavController(v).navigate(navDirections)
    }
}