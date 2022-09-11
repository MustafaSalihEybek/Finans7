package com.finans7.model.postdetail

import com.finans7.model.categorynews.PostListModel
import com.finans7.model.comment.CommentModel

data class PostDetailModel(
    val postDetail: Any?,
    val postDetailFromModel: PostDetailFromModel,
    val latestPostList: Any?,
    val latestPostList_1: List<PostListModel>,
    val previousPost: Any?,
    val nextPost: String,
    val commentList: Any?,
    val commentList_1: List<CommentModel>
)