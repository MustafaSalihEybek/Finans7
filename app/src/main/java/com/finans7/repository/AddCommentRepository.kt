package com.finans7.repository

import com.finans7.model.comment.CommentPostModel
import com.finans7.util.AppUtil
import io.reactivex.Single
import okhttp3.ResponseBody

class AddCommentRepository {
    fun addComment(comment: CommentPostModel) : Single<ResponseBody> {
        return AppUtil.getAppAPI().addComment(comment)
    }
}