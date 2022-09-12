package com.finans7.model.postdetail

import com.finans7.model.categorynews.PostListModel
import com.finans7.model.comment.CommentModel

data class PostDetailModel(
    val postDetailFromModel: PostListModel,
    val latestPostList_1: List<PostListModel>,
    val commentList_1: List<CommentModel>
)