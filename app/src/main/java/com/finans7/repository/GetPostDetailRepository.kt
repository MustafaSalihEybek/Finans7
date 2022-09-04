package com.finans7.repository

import com.finans7.model.postdetail.PostDetailModel
import com.finans7.util.AppUtil
import io.reactivex.Single

class GetPostDetailRepository {
    fun getPostDetail(postId: Int, userId: String) : Single<PostDetailModel> {
        return AppUtil.getAppAPI().getPostDetail(postId, userId)
    }
}