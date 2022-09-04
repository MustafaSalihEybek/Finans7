package com.finans7.model.category

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    val TERMID: Int,
    val NAME: String,
    val SLUG: String,
    val ICON: String,
    val DISPLAYORDER: Int,
    val ISMAINCATEGORY: Boolean,
    val PARENTID: Int
) : Parcelable
