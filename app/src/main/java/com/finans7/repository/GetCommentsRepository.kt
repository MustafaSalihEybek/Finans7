package com.finans7.repository

import com.finans7.model.comment.RootComment
import com.finans7.util.AppUtil
import io.reactivex.Single

class GetCommentsRepository {
    fun getComments(postId: Int, skip: Int, sortType: Int, userId: String) : Single<RootComment> {
        return AppUtil.getAppAPI().getComments(postId, skip, sortType, userId)
    }
}