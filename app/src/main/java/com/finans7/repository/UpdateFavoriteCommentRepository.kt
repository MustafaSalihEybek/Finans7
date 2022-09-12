package com.finans7.repository

import com.finans7.model.favorite.CommentFavoriteResponse
import com.finans7.model.favorite.FavoritePostModel
import com.finans7.util.AppUtil
import io.reactivex.Single

class UpdateFavoriteCommentRepository {
    fun updateFavoriteComment(favoritePostModel: FavoritePostModel) : Single<CommentFavoriteResponse> {
        return AppUtil.getAppAPI().updateFavoriteComment(favoritePostModel)
    }
}