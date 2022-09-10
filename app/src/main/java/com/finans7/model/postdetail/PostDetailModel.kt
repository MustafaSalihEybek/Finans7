package com.finans7.model.postdetail

data class PostDetailModel(
    val postDetailFromModel: PostDetailFromModel,
    val latestPostList: String,
    val previousPost: Any,
    val nextPost: String,
    val commentList: String
)
