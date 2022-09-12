package com.finans7.model.comment

data class RootComment(
    val commentList: ArrayList<CommentModel>,
    val postTitle: String,
    val fullNewsId: Any,
    val totalComment: Int,
    val postId: Int
)
