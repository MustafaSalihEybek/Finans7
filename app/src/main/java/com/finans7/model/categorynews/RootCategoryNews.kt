package com.finans7.model.categorynews

data class RootCategoryNews(
    val postList: List<PostListModel>,
    val releatedPost: List<PostListModel>,
    val categoryName: String,
    val categorySlug: String,
    val skipCount: Int
)
