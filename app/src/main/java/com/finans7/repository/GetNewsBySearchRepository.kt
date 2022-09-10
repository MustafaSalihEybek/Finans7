package com.finans7.repository

import com.finans7.model.categorynews.RootCategoryNews
import com.finans7.util.AppUtil
import io.reactivex.Single

class GetNewsBySearchRepository {
    fun getNewsBySearch(categoryName: String, skip: Int) : Single<RootCategoryNews> {
        return AppUtil.getAppAPI().getNewsBySearch(categoryName, skip)
    }
}