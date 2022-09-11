package com.finans7.model.homepage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val posttitle: String?,
    val postcontent: String?,
    val tags: String?,
    val postid: Int?,
    val mainimage: String?,
    val keywords: String?,
    val authornamesurname: String?,
    val username: String?,
    val userimage: String?,
    val insertmonth: Int?,
    val insertday: Int?,
    val insertyear: Int?,
    val postdate: String,
    val termid: Int?,
    val termname: String?,
    val termslug: String?,
    val isvideo: Boolean,
    val commentcount: Int?,
    val postviewcount: Int?,
    val imagewidth: Int?,
    val imageheight: Int?,
    val shortdescription: String?,
    val usernickname: String?,
    val coverimage: String?,
    val posttype: Int?,
    val posttime: String?,
    val videoname: String?,
    val sagmansetimage: String?
) : Parcelable
