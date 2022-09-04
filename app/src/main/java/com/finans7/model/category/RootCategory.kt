package com.finans7.model.category

data class RootCategory(
    val MainCategoryList: List<CategoryModel>,
    val AllMainCategoryList: List<CategoryModel>,
    val OtherCategoryList: List<CategoryModel>
)
