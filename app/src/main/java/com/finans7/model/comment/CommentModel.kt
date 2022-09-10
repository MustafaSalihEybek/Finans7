package com.finans7.model.comment

data class CommentModel(
    val commentid: Int,
    val postid: Int,
    val commentauthor: String,
    val commentdatetext: String,
    val authoremail: String,
    val commentcontent: String,
    val commentapproved: Boolean,
    val parentid: Int,
    val likecount: Any,
    val dislikecount: Any,
    val userid: String,
    val avatar: String,
    val userislike: Int,
    val userisdislike: Int
)
