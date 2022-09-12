package com.finans7.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finans7.R
import com.finans7.adapter.CommentsAdapter
import com.finans7.databinding.FragmentCommentsBinding
import com.finans7.model.Avatar
import com.finans7.model.comment.CommentModel
import com.finans7.model.comment.CommentPostModel
import com.finans7.model.comment.RootComment
import com.finans7.model.favorite.FavoritePostModel
import com.finans7.util.AppUtil
import com.finans7.util.Singleton
import com.finans7.util.downloadImageUrl
import com.finans7.util.show
import com.finans7.viewmodel.CommentsViewModel

class CommentsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var commentsBinding: FragmentCommentsBinding
    private lateinit var commentsViewModel: CommentsViewModel

    private lateinit var avatarList: List<Avatar>
    private lateinit var avatarsFragment: AvatarsFragment

    private var postId: Int = 0
    private lateinit var postTitle: String

    private lateinit var rootComment: RootComment
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var commentsLayoutManager: LinearLayoutManager

    private lateinit var commentValue: String
    private lateinit var selectedAvatar: Avatar

    private lateinit var commentShortingFragment: CommentShortingFragment
    private var selectedShortingIn: Int = 1

    private lateinit var commentList: ArrayList<CommentModel>
    private lateinit var selectedCommentData: CommentModel
    private var selectedCommentType: Int = 0

    private fun init(){
        arguments?.let {
            postId = CommentsFragmentArgs.fromBundle(it).posId
            postTitle = CommentsFragmentArgs.fromBundle(it).postTitle

            selectedAvatar = Avatar(0, "defaultavatar.png", null)
            commentsBinding.commentsFragmentTxtPostTitle.text = postTitle
            commentsBinding.defaultavatar = selectedAvatar

            commentsBinding.commentsFragmentRecyclerView.setHasFixedSize(true)
            commentsLayoutManager = LinearLayoutManager(v.context, LinearLayoutManager.VERTICAL, false)
            commentsBinding.commentsFragmentRecyclerView.layoutManager = commentsLayoutManager
            commentsAdapter = CommentsAdapter(arrayListOf())
            commentsBinding.commentsFragmentRecyclerView.adapter = commentsAdapter

            commentsViewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)
            observeLiveData()
            commentsViewModel.getAvatars()
            commentsViewModel.getCommentList(postId, 0, selectedShortingIn, AppUtil.getDeviceId(v.context))

            /*commentsBinding.root.setOnApplyWindowInsetsListener { view, windowInsets ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                    commentsBinding.root.setPadding(0, 0, 0, imeHeight)
                    val insets = windowInsets.getInsets(WindowInsets.Type.ime() or WindowInsets.Type.systemGestures())
                    insets
                } else
                    Singleton.setSoftInput(2)

                windowInsets
            }*/

            Singleton.setSoftInput(2)

            commentsBinding.commentsFragmentImgBack.setOnClickListener(this)
            commentsBinding.commentsFragmentImgSend.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        commentsBinding = FragmentCommentsBinding.inflate(inflater)
        return commentsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()

        commentsBinding.commentsFragmentEditSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()){
                commentValue = it.toString()
                commentsBinding.commentsFragmentImgSend.visibility = View.VISIBLE
            } else {
                commentValue = ""
                commentsBinding.commentsFragmentImgSend.visibility = View.GONE
            }
        }

        commentsAdapter.setOnUpdateFavoriteClickListener(object : CommentsAdapter.UpdateFavoriteClickListener{
            override fun onUpdateClick(commentData: CommentModel, commentType: Int) {
                selectedCommentData = commentData
                selectedCommentType = commentType

                commentsViewModel.updateFavoriteComment(
                    FavoritePostModel(
                        AppUtil.getDeviceId(v.context),
                        postId,
                        commentType
                    )
                )
            }
        })
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.comments_fragment_imgAvatars -> avatarsFragment.show(childFragmentManager, "Avatars")
                R.id.comments_fragment_imgBack -> backToPage()
                R.id.comments_fragment_imgSend -> sendComment()
                R.id.comments_fragment_linearShorting -> showSortingFragment(selectedShortingIn)
            }
        }
    }

    private fun observeLiveData(){
        commentsViewModel.avatarList.observe(viewLifecycleOwner, Observer {
            it?.let {
                avatarList = it
                avatarsFragment = AvatarsFragment(avatarList)
                commentsBinding.commentsFragmentLinearShorting.setOnClickListener(this)

                avatarsFragment.setOnAvatarItemListener(object : AvatarsFragment.AvatarItemListener{
                    override fun onListener(avatar: Avatar) {
                        selectedAvatar = avatar

                        avatarsFragment.dismiss()
                        setUserAvatar(avatar)
                    }
                })

                commentsBinding.commentsFragmentImgAvatars.setOnClickListener(this)
            }
        })

        commentsViewModel.rootComment.observe(viewLifecycleOwner, Observer {
            it?.let {
                rootComment = it
                commentList = it.commentList

                commentsAdapter.loadData(commentList)
                attachUpcomingCommentsOnScrollListener()
            }
        })

        commentsViewModel.commentFavoriteResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                commentList = AppUtil.getEditedCommentList(commentList, selectedCommentData, it)
                commentsAdapter.loadData(commentList)
            }
        })

        commentsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
            }
        })

        commentsViewModel.successMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.show(v, it)
                commentsBinding.commentsFragmentEditSearch.setText("")
            }
        })
    }

    private fun setUserAvatar(avatar: Avatar){
        commentsBinding.commentsFragmentImgAvatars.downloadImageUrl(AppUtil.getAvatarImageUrl(avatar.AVATARNAME))
    }

    private fun sendComment(){
        if (!commentValue.isEmpty()){
            commentsViewModel.addComment(
                CommentPostModel(
                    -1,
                    postId,
                    null,
                    null,
                    commentValue,
                    -1,
                    AppUtil.getDeviceId(v.context),
                    selectedAvatar.AVATARNAME
                )
            )
        } else
            "message".show(v, "Lütfen yorum göndermeden önce bir şeyler yazınız")
    }

    private fun showSortingFragment(shortIn: Int){
        commentShortingFragment = CommentShortingFragment(shortIn)
        commentShortingFragment.show(childFragmentManager, "Shorting")

        commentShortingFragment.setOnCommentShortingItemClickListener(object : CommentShortingFragment.CommentShortingOnItemClickListener{
            override fun onItemClick(shortIn: Int, shortName: String) {
                selectedShortingIn = shortIn
                commentsBinding.commentsFragmentTxtShorting.text = shortName
                commentsViewModel.getCommentList(postId, 0, selectedShortingIn, AppUtil.getDeviceId(v.context))
            }
        })
    }

    private fun attachUpcomingCommentsOnScrollListener(){
        commentsBinding.commentsFragmentRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = commentsLayoutManager.itemCount
                val visibleItemCount = commentsLayoutManager.childCount
                val firstVisibleItem = commentsLayoutManager.findFirstVisibleItemPosition()

                if ((firstVisibleItem + visibleItemCount) == totalItemCount){
                    commentsBinding.commentsFragmentRecyclerView.removeOnScrollListener(this)
                    commentsViewModel.getCommentList(postId, commentsAdapter.itemCount, selectedShortingIn, AppUtil.getDeviceId(v.context))
                }
            }
        })
    }

    private fun backToPage(){
        Navigation.findNavController(v).popBackStack()
    }
}