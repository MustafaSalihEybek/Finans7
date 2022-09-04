package com.finans7.model.categorynews

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class PostListModel(
    val posttitle: String,
    val postcontent: String,
    val tags: String,
    val postid: Int,
    val mainimage: String,
    val keywords: String,
    val authornamesurname: String,
    val username: String,
    val userimage: @RawValue Any,
    val insertmonth: String,
    val insertday: String,
    val insertyear: String,
    val termid: String,
    val termname: String,
    val termslug: String,
    val isvideo: @RawValue Any,
    val commentcount: @RawValue Any,
    val postviewcount: @RawValue Any,
    val imagewidth: Int,
    val imageheight: Int,
    val shortdescription: String,
    val usernickname: @RawValue Any,
    val coverimage: @RawValue Any,
    val posttype: String,
    val posttime: String,
    val videoname: @RawValue Any,
    val sagmansetimage: @RawValue Any
) : Parcelable
