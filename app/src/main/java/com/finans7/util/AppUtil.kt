package com.finans7.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.finans7.R
import com.finans7.api.AppAPI
import com.finans7.model.homepage.News
import com.finans7.repository.GetCategoryListRepository
import com.finans7.repository.GetHomePageNewsRepository
import com.finans7.repository.GetNewsByCategory
import com.finans7.repository.GetPostDetailRepository
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object AppUtil {
    lateinit var disposable: CompositeDisposable
    private lateinit var imageUrl: String

    lateinit var getHomePageNewsRepository: GetHomePageNewsRepository
    lateinit var getCategoryListRepository: GetCategoryListRepository
    lateinit var getPostDetailRepository: GetPostDetailRepository
    lateinit var getNewsByCategory: GetNewsByCategory

    lateinit var newsData: News

    fun getAppAPI() : AppAPI {
        return Retrofit.Builder()
            .baseUrl(Singleton.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AppAPI::class.java)
    }

    fun getColorByIntFromAttr(context: Context, color: Int) : Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme?.resolveAttribute(color, typedValue, true)

        return typedValue.data
    }

    fun getPostImageUrl(imageName: String) : String {
        imageUrl = "${Singleton.SERVICE_URL}${Singleton.POST_IMAGE_PATH}$imageName"
        return imageUrl
    }

    fun getEditNumberByString(number: Int) : String {
        var numberS: String = "$number"

        if (number < 10)
            numberS = "0$number"

        return numberS
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context) : String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}