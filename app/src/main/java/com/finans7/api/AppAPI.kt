package com.finans7.api

import com.finans7.model.category.RootCategory
import com.finans7.model.categorynews.RootCategoryNews
import com.finans7.model.homepage.HomePageNews
import com.finans7.model.postdetail.PostDetailModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppAPI {
    @GET("/f7api/selectHomePageList")
    fun getHomePageNews() : Single<HomePageNews>

    @GET("/f7api/getCategory")
    fun getCategoryList() : Single<RootCategory>

    @GET("/f7api/getNewsDetail")
    fun getPostDetail(@Query("id") postId: Int, @Query("userId") userId: String) : Single<PostDetailModel>

    @GET("/f7api/getByCategory")
    fun getNewsByCategory(@Query("CategoryName") categoryName: String, @Query("Skip") skip: Int) : Single<RootCategoryNews>
}