package com.finans7.view

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.finans7.R
import com.finans7.databinding.FragmentNewBinding
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.homepage.News
import com.finans7.model.postdetail.PostDetailModel
import com.finans7.util.AppUtil
import com.finans7.util.show
import com.finans7.viewmodel.NewViewModel

class NewFragment(val newsData: News) : Fragment() {
    private lateinit var v: View
    private lateinit var newBinding: FragmentNewBinding
    private lateinit var newViewModel: NewViewModel

    private lateinit var postDetailData: PostDetailModel

    private fun init(){
        newBinding.newsdata = newsData

        newViewModel = ViewModelProvider(this).get(NewViewModel::class.java)
        observeLiveData()

        newsData.postid?.let {
            newViewModel.getPostDetail(it, AppUtil.getDeviceId(v.context))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        newBinding = FragmentNewBinding.inflate(inflater)
        return newBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    private fun observeLiveData(){
        newViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        newViewModel.postDetailData.observe(viewLifecycleOwner, Observer {
            it?.let {
                postDetailData = it

                newBinding.newFragmentWebView.settings.setGeolocationEnabled(true)
                newBinding.newFragmentWebView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + postDetailData.postDetailFromModel.postcontent, "text/html", "UTF-8", null)
            }
        })
    }
}