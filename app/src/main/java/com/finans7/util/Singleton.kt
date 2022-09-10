package com.finans7.util

import com.finans7.model.homepage.HomePageNews

class Singleton {
    companion object{
        val BASE_URL: String = "https://www.finans7.com"
        val SERVICE_URL: String = "https://www.finans7.com/"
        val POST_IMAGE_PATH: String = "ProjectImages/PostImages/"
        val USER_IMAGE_PATH: String = "ProjectImages/UserProfiles/"
        val AVATAR_IMAGE_PATH: String = "ProjectImages/avatarImages/"
        val MAIN_IMAGE_WIDTH: Double = 637.0
        val MAIN_IMAGE_HEIGHT: Double = 332.0
        val V_SIZE: Int = 35
        val H_SIZE: Int = 25
        val H_SIZE_TAG: Int = 15

        var themeMode: String = "Light"
        var homeIsCreated: Boolean = false
        var sliderCurrentPage: Int = 0
        var lastNewsCurrentPage: Int = 0
        var headlineNewsCurrentPage: Int = 0
        var trendingNewsCurrentPage: Int = 0
        var mostNewsCurrentPage: Int = 0
        var scrollXPosition: Int = 0
        var scrollYPosition: Int = 0
        lateinit var homePageNews: HomePageNews
    }
}