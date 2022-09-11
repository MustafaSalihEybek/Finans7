package com.finans7.repository

import com.finans7.util.AppUtil
import io.reactivex.Single
import okhttp3.ResponseBody

class GenerateUserTopicRepository {
    fun generateUserTopic(userPlatform: String, userUnique: String, deviceName: String, deviceVersion: String) : Single<ResponseBody> {
        return AppUtil.getAppAPI().generateUserTopic(userPlatform, userUnique, deviceName, deviceVersion)
    }
}