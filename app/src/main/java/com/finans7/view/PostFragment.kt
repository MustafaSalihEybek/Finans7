package com.finans7.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.finans7.R
import com.finans7.databinding.FragmentPostBinding
import com.finans7.model.categorynews.PostListModel

class PostFragment(val postData: PostListModel) : Fragment() {
    private lateinit var v: View
    private lateinit var postBinding: FragmentPostBinding

    private fun init(){
        postBinding.postdata = postData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        postBinding = FragmentPostBinding.inflate(inflater)
        return postBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }
}