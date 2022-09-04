package com.finans7.repository

import com.finans7.model.category.RootCategory
import com.finans7.util.AppUtil
import io.reactivex.Single

class GetCategoryListRepository {
    fun getCategoryList() : Single<RootCategory> {
        return AppUtil.getAppAPI().getCategoryList()
    }
}