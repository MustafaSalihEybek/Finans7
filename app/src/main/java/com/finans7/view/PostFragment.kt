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
import com.finans7.model.postdetail.PostDetailModel
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

    private lateinit var postDetailModel: PostDetailModel
    private lateinit var rootComment: RootComment
    private lateinit var commentsAdapter: CommentsAdapter

    private lateinit var tagList: ArrayList<Tag>
    private lateinit var tagsAdapter: TagsAdapter

    private lateinit var sharedPreferences: SharedPreferences
    private var commentAmount: Int = 0

    private fun init(){
        postBinding.postdata = postData
        tagList = AppUtil.getTagList(postData.tags)

        sharedPreferences = SharedPreferences(v.context)

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
        postViewModel.getPostDetail(postData.postid, AppUtil.getDeviceId(v.context))

        postBinding.postFragmentLinearComments.setOnClickListener(this)
        postBinding.postFragmentEditSearch.setOnClickListener(this)
        postBinding.postFragmentRelativeCommentCount.setOnClickListener(this)
        postBinding.postFragmentImgMessage.setOnClickListener(this)
        postBinding.postFragmentImgShare.setOnClickListener(this)
        postBinding.postFragmentImgFacebook.setOnClickListener(this)
        postBinding.postFragmentImgTwitter.setOnClickListener(this)
        postBinding.postFragmentImgGmail.setOnClickListener(this)
        postBinding.postFragmentImgPinterest.setOnClickListener(this)
        postBinding.postFragmentImgWhatsApp.setOnClickListener(this)
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
                R.id.post_fragment_imgShare -> AppUtil.shareNews("${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}", v.context)
                R.id.post_fragment_imgFacebook -> {
                    AppUtil.shareWithSocialMedia(
                        "${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}",
                        v.context,
                        Singleton.FACEBOOK_PACKAGE,
                        "${Singleton.FACEBOOK_PAGE}${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}"
                    )
                }
                R.id.post_fragment_imgTwitter -> {
                    AppUtil.shareWithSocialMedia(
                        "${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}",
                        v.context,
                        Singleton.TWITTER_PACKAGE,
                        "${Singleton.TWITTER_PAGE}Haberi Paylaş : ${postData.posttitle}&url=${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}"
                    )
                }
                R.id.post_fragment_imgGmail -> {
                    AppUtil.shareWithSocialMedia(
                        "${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}",
                        v.context,
                        Singleton.GMAIL_PACKAGE,
                        "${Singleton.GMAIL_PAGE}${postData.posttitle}&BODY=${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}"
                    )
                }
                R.id.post_fragment_imgPinterest -> {
                    AppUtil.shareWithSocialMedia(
                        "${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}",
                        v.context,
                        Singleton.PINTEREST_PACKAGE,
                        "${Singleton.PINTEREST_PAGE}${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}"
                    )
                }
                R.id.post_fragment_imgWhatsApp -> {
                    AppUtil.shareWithSocialMedia(
                        "${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}",
                        v.context,
                        Singleton.WHATSAPP_PACKAGE,
                        "${Singleton.WHATSAPP_PAGE}${Singleton.BASE_URL}/haber/${AppUtil.getUrl(postData.posttitle)}-${postData.postid}"
                    )
                }
            }
        }
    }

    private fun observeLiveData(){
        postViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                println(it)
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

        postViewModel.postDetailModel.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadPostContent(it)
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPostContent(postDetailModel: PostDetailModel){
        postBinding.postFragmentWebView.settings.javaScriptEnabled = true
        postBinding.postFragmentWebView.settings.setGeolocationEnabled(true)
        postBinding.postFragmentWebView.settings.textZoom = sharedPreferences.getFontSize()
        postBinding.postFragmentWebView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + postDetailModel.postDetailFromModel.postcontent, "text/html", "UTF-8", null)

        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK) && Singleton.themeMode.equals("Dark")) {
            WebSettingsCompat.setForceDark(postBinding.postFragmentWebView.settings, WebSettingsCompat.FORCE_DARK_ON);
        }

        postDetailModel.postDetailFromModel.commentcount?.let {
            commentAmount = it
        }

        postBinding.postFragmentTxtCommentCount.text = commentAmount.toString()
    }

    private fun goToCommentsPage(postId: Int, postTitle: String){
        navDirections = NewsFragmentDirections.actionNewsFragmentToCommentsFragment(postId, postTitle)
        Navigation.findNavController(v).navigate(navDirections)
    }
}