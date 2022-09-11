package com.finans7.repository

import com.finans7.model.Avatar
import com.finans7.util.AppUtil
import io.reactivex.Single

class GetAvatarsRepository {
    fun getAvatars() : Single<List<Avatar>> {
        return AppUtil.getAppAPI().getAvatars()
    }
}