package com.finans7.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.finans7.R
import com.finans7.adapter.CommentsAdapter
import com.finans7.adapter.TagsAdapter
import com.finans7.adapter.decoration.LinearManagerDecoration
import com.finans7.databinding.FragmentPostBinding
import com.finans7.model.Tag
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.comment.RootComment
import com.finans7.util.AppUtil
import com.finans7.util.SharedPreferences
import com.finans7.util.Singleton
import com.finans7.util.show
import com.finans7.viewmodel.PostViewModel


class PostFragment(val postData: PostListModel) : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var postBinding: FragmentPostBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var navDirections: NavDirections

    private lateinit var rootComment: RootComment
    private lateinit var commentsAdapter: CommentsAdapter

    private lateinit var tagList: ArrayList<Tag>
    private lateinit var tagsAdapter: TagsAdapter

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(){
        postBinding.postdata = postData
        tagList = AppUtil.getTagList(postData.tags)

        sharedPreferences = SharedPreferences(v.context)

        postBinding.postFragmentWebView.settings.javaScriptEnabled = true
        postBinding.postFragmentWebView.settings.setGeolocationEnabled(true)
        postBinding.postFragmentWebView.settings.textZoom = sharedPreferences.getFontSize()
        postBinding.postFragmentWebView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + postData.postcontent, "text/html", "UTF-8", null)

        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK) && Singleton.themeMode.equals("Dark")) {
            WebSettingsCompat.setForceDark(postBinding.postFragmentWebView.settings, WebSettingsCompat.FORCE_DARK_ON);
        }

        postBinding.postFragmentRecyclerViewTags.setHasFixedSize(true)
        postBinding.postFragmentRecyclerViewTags.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.HORIZONTAL, false)
        tagsAdapter = TagsAdapter(tagList, v)
        postBinding.postFragmentRecyclerViewTags.addItemDecoration(LinearManagerDecoration(0, Singleton.H_SIZE_TAG, tagList.size, false, true))
        postBinding.postFragmentRecyclerViewTags.adapter = tagsAdapter

        postBinding.postFragmentRecyclerViewComments.setHasFixedSize(true)
        postBinding.postFragmentRecyclerViewComments.layoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
        commentsAdapter = CommentsAdapter(arrayListOf())
        postBinding.postFragmentRecyclerViewComments.adapter = commentsAdapter

        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        observeLiveData()
        postViewModel.getComments(postData.postid, 0, 1, AppUtil.getDeviceId(v.context))

        postBinding.postFragmentLinearComments.setOnClickListener(this)
        postBinding.postFragmentEditSearch.setOnClickListener(this)
        postBinding.postFragmentRelativeCommentCount.setOnClickListener(this)
        postBinding.postFragmentImgMessage.setOnClickListener(this)
        postBinding.postFragmentImgShare.setOnClickListener(this)
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

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.post_fragment_linearComments -> goToCommentsPage(postData.postid, postData.posttitle)
                R.id.post_fragment_editSearch -> goToCommentsPage(postData.postid, postData.posttitle)
                R.id.post_fragment_imgMessage -> goToCommentsPage(postData.postid, postData.posttitle)
                R.id.post_fragment_relativeCommentCount -> goToCommentsPage(postData.postid, postData.posttitle)
                R.id.post_fragment_imgShare -> shareNews()
            }
        }
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

                    postBinding.postFragmentTxtCommentCount.text = rootComment.commentList.size.toString()
                } else {
                    postBinding.postFragmentRecyclerViewComments.visibility = View.GONE
                    postBinding.postFragmentTxtCommentMessage.visibility = View.VISIBLE

                    postBinding.postFragmentTxtCommentCount.text = "0"
                }

                commentsAdapter.loadData(rootComment.commentList)
            }
        })
    }

    private fun shareNews(){
        println("Url: ${Singleton.NEWS_SHARE_BASE_URL}${AppUtil.getUrlByTitle(postData.posttitle)}-${postData.postid}")
    }

    private fun goToCommentsPage(postId: Int, postTitle: String){
        navDirections = NewsFragmentDirections.actionNewsFragmentToCommentsFragment(postId, postTitle)
        Navigation.findNavController(v).navigate(navDirections)
    }
}