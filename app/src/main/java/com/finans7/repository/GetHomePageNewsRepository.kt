package com.finans7.repository

import com.finans7.model.homepage.HomePageNews
import com.finans7.util.AppUtil
import io.reactivex.Single
import okhttp3.ResponseBody

class GetHomePageNewsRepository {
    fun getHomePageNews() : Single<HomePageNews> {
        return AppUtil.getAppAPI().getHomePageNews()
    }
}