package com.finans7.model.favorite

data class FavoritePostModel(
    val UserId: String,
    val PostId: Int,
    val FavoriteType: Int
)
