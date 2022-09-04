package com.finans7.model.postdetail

data class PostDetailModel(
    val postDetail: String,
    val latestPostList: String,
    val previousPost: Any,
    val nextPost: String,
    val commentList: String
)
