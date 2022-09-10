package com.finans7.model.category

data class RootCategory(
    val MainCategoryList: List<CategoryModel>,
    val AllCategoryList: List<CategoryModel>,
    val OtherCategoryList: List<CategoryModel>
)
