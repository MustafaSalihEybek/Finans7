package com.finans7.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.finans7.adapter.CommentsAdapter
import com.finans7.adapter.TagsAdapter
import com.finans7.adapter.decoration.LinearManagerDecoration
import com.finans7.databinding.FragmentPostBinding
import com.finans7.model.Tag
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.comment.RootComment
import com.finans7.util.AppUtil
import com.finans7.util.Singleton
import com.finans7.util.show
import com.finans7.viewmodel.PostViewModel


class PostFragment(val postData: PostListModel) : Fragment() {
    private lateinit var v: View
    private lateinit var postBinding: FragmentPostBinding
    private lateinit var postViewModel: PostViewModel

    private lateinit var rootComment: RootComment
    private lateinit var commentsAdapter: CommentsAdapter

    private lateinit var tagList: ArrayList<Tag>
    private lateinit var tagsAdapter: TagsAdapter

    private fun init(){
        postBinding.postdata = postData
        tagList = AppUtil.getTagList(postData.tags)

        postBinding.postFragmentWebView.settings.setGeolocationEnabled(true)
        //postBinding.postFragmentWebView.settings.textZoom = 200 font size
        postBinding.postFragmentWebView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + postData.postcontent, "text/html", "UTF-8", null)

        postBinding.postFragmentRecyclerViewTags.setHasFixedSize(true)
        postBinding.postFragmentRecyclerViewTags.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.HORIZONTAL, false)
        tagsAdapter = TagsAdapter(tagList)
        postBinding.postFragmentRecyclerViewTags.addItemDecoration(LinearManagerDecoration(0, Singleton.H_SIZE_TAG, tagList.size, false, true))
        postBinding.postFragmentRecyclerViewTags.adapter = tagsAdapter

        postBinding.postFragmentRecyclerViewComments.setHasFixedSize(true)
        postBinding.postFragmentRecyclerViewComments.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        commentsAdapter = CommentsAdapter(arrayListOf())
        postBinding.postFragmentRecyclerViewComments.adapter = commentsAdapter

        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        observeLiveData()
        postViewModel.getComments(postData.postid, 0, 1, AppUtil.getDeviceId(v.context))
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

    private fun observeLiveData(){
        postViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        postViewModel.rootComment.observe(viewLifecycleOwner, Observer {
            it?.let {
                rootComment = it

                if (rootComment.commentList.isNotEmpty()){
                    postBinding.postFragmentTxtCommentMessage.visibility = View.GONE
                    postBinding.postFragmentRecyclerViewComments.visibility = View.VISIBLE
                } else {
                    postBinding.postFragmentRecyclerViewComments.visibility = View.GONE
                    postBinding.postFragmentTxtCommentMessage.visibility = View.VISIBLE
                }

                commentsAdapter.loadData(rootComment.commentList)
            }
        })
    }
}