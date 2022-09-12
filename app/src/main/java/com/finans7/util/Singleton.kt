package com.finans7.util

import android.os.Build
import android.view.Window
import android.view.WindowManager
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.homepage.HomePageNews

class Singleton {
    companion object{
        val BASE_URL: String = "https://www.finans7.com"
        val SERVICE_URL: String = "https://www.finans7.com/"
        val POST_IMAGE_PATH: String = "ProjectImages/PostImages/"
        val USER_IMAGE_PATH: String = "ProjectImages/UserProfiles/"
        val AVATAR_IMAGE_PATH: String = "ProjectImages/avatarImages/"
        val NEWS_SHARE_BASE_URL: String = "https://www.finans7.com/haber/"
        val TWITTER_USER_ID: String = "gRm5NJ2JCU0Zc1PrzeONiA&s"
        val TWITTER_PROFILE_NAME: String = "Finans7haber1"
        val INSTAGRAM_PROFILE_NAME: String = "finans7haber"
        val PRIVACY_POLICY_URL: String = "https://www.finans7.com/ProjectImages/news/gizlilik.html"
        val TERMS_OF_SERVICE: String = "https://www.finans7.com/ProjectImages/news/kunye.html"
        val MAIN_IMAGE_WIDTH: Double = 637.0
        val MAIN_IMAGE_HEIGHT: Double = 332.0
        val V_SIZE: Int = 35
        val H_SIZE: Int = 25
        val H_SIZE_TAG: Int = 15

        val FACEBOOK_PACKAGE: String = "com.facebook.katana"
        val TWITTER_PACKAGE: String = "com.twitter.android"
        val WHATSAPP_PACKAGE: String = "com.whatsapp"
        val PINTEREST_PACKAGE: String = "com.pinterest"
        val GMAIL_PACKAGE: String = "com.google.android.gm"
        val FACEBOOK_PAGE: String = "https://www.facebook.com/sharer/sharer.php?u="
        val TWITTER_PAGE: String = "https://twitter.com/intent/tweet?text="
        val WHATSAPP_PAGE: String = "https://web.whatsapp.com/send?text="
        val PINTEREST_PAGE: String = "https://pinterest.com/pin/create/link/?url="
        val GMAIL_PAGE: String = "mailto:?subject="

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

        var selectedPageIn: Int = 0

        var searchIsCreated: Boolean = false
        var searchedValue: String = ""
        var postList: ArrayList<PostListModel> = arrayListOf()

        lateinit var window: Window

        fun setSoftInput(inputMode: Int){
            if (inputMode == 1){
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
            else{
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
        }
    }
}