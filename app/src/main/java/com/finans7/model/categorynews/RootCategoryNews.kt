package com.finans7.model.categorynews

data class RootCategoryNews(
    val postList: ArrayList<PostListModel>,
    val releatedPost: ArrayList<PostListModel>,
    val categoryName: String,
    val categorySlug: String,
    val skipCount: Int
)
