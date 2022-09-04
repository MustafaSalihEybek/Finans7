package com.finans7.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.finans7.R
import com.finans7.databinding.FragmentNewsByTitleBinding
import com.finans7.model.homepage.News

class NewsByTitleFragment(val newsData: News, val newsList: List<News>, val newsIn: Int) : Fragment() {
    private lateinit var v: View
    private lateinit var newsByTitleBinding: FragmentNewsByTitleBinding
    private lateinit var navDirections: NavDirections

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

    private fun goToNewsPage(newsList: List<News>, newsIn: Int){
        navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(newsList.toTypedArray(), null, newsIn, false)
        Navigation.findNavController(v).navigate(navDirections)
    }
}