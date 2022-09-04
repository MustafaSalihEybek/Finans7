package com.finans7.repository

import com.finans7.model.categorynews.RootCategoryNews
import com.finans7.util.AppUtil
import io.reactivex.Single

class GetNewsByCategory {
    fun getNewsByCategory(categoryName: String, skip: Int) : Single<RootCategoryNews> {
        return AppUtil.getAppAPI().getNewsByCategory(categoryName, skip)
    }
}