package com.finans7.model.postdetail

import com.finans7.model.comment.CommentModel

data class PostDetailModel(
    val postDetailFromModel: PostDetailFromModel,
    val latestPostList: String,
    val previousPost: Any,
    val nextPost: String,
    val commentList: List<CommentModel>
)
