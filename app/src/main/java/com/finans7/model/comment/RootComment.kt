package com.finans7.model.comment

data class RootComment(
    val commentList: List<CommentModel>,
    val postTitle: String,
    val fullNewsId: Any,
    val totalComment: Int,
    val postId: Int
)
