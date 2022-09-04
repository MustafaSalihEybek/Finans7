package com.finans7.model.homepage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val POSTTITLE: String?,
    val POSTCONTENT: String?,
    val TAGS: String?,
    val POSTID: Int?,
    val MAINIMAGE: String?,
    val KEYWORDS: String?,
    val AUTHORNAMESURNAME: String?,
    val USERNAME: String?,
    val USERIMAGE: String?,
    val INSERTMONTH: Int?,
    val INSERTDAY: Int?,
    val INSERTYEAR: Int?,
    val TERMID: Int?,
    val TERMNAME: String?,
    val TERMSLUG: String?,
    val ISVIDEO: Boolean,
    val COMMENTCOUNT: Int?,
    val POSTVIEWCOUNT: Int?,
    val IMAGEWIDTH: Int?,
    val IMAGEHEIGHT: Int?,
    val SHORTDESCRIPTION: String?,
    val USERNICKNAME: String?,
    val COVERIMAGE: String?,
    val POSTTYPE: Int?,
    val POSTTIME: String?,
    val VIDEONAME: String?,
    val SAGMANSETIMAGE: String?,
    val METADESCRIPTION: String?,
    val MAINIMAGEALT: String?
) : Parcelable
