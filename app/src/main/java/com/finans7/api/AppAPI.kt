package com.finans7.api

import com.finans7.model.Avatar
import com.finans7.model.category.RootCategory
import com.finans7.model.categorynews.RootCategoryNews
import com.finans7.model.comment.CommentPostModel
import com.finans7.model.comment.RootComment
import com.finans7.model.homepage.HomePageNews
import com.finans7.model.postdetail.PostDetailModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.Body
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

    @GET("/f7api/getBySearch")
    fun getNewsBySearch(@Query("CategoryName") categoryName: String, @Query("Skip") skip: Int) : Single<RootCategoryNews>

    @GET("/f7api/getByTags")
    fun getNewsByTag(@Query("CategoryName") categoryName: String, @Query("Skip") skip: Int) : Single<RootCategoryNews>

    @GET("/f7api/getComments")
    fun getComments(@Query("PostId") postId: Int, @Query("Skip") skip: Int, @Query("sortType") sortType: Int, @Query("userId") userId: String) : Single<RootComment>

    @GET("/f7api/getAvatarList")
    fun getAvatars() : Single<List<Avatar>>

    @POST("/f7api/postComment")
    fun addComment(@Body comment: CommentPostModel) : Single<ResponseBody>

    @GET("user/f7api/generateUserTopic")
    fun generateUserTopic(
        @Query("userPlatform") userPlatform: String,
        @Query("userUnique") userUnique: String,
        @Query("deviceName") deviceName: String,
        @Query("deviceVersion") deviceVersion: String
    ) : Single<ResponseBody>
}