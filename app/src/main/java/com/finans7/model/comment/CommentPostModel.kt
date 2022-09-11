package com.finans7.model.comment

data class CommentPostModel(
    val COMMENTID: Int,
    val POSTID: Int,
    val COMMENTAUTHOR: String?,
    val AUTHOREMAIL: String?,
    val COMMENTCONTENT: String,
    val PARENTID: Int,
    val USERID: String,
    val AVATAR: String
)
