package com.finans7.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.TypedValue
import com.finans7.api.AppAPI
import com.finans7.model.Tag
import com.finans7.model.homepage.News
import com.finans7.repository.*
import com.finans7.view.dialog.SplashDialog
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.Normalizer


object AppUtil {
    lateinit var disposable: CompositeDisposable
    private lateinit var imageUrl: String
    private lateinit var shareIntent: Intent

    lateinit var getHomePageNewsRepository: GetHomePageNewsRepository
    lateinit var getCategoryListRepository: GetCategoryListRepository
    lateinit var getPostDetailRepository: GetPostDetailRepository
    lateinit var getNewsByCategory: GetNewsByCategory
    lateinit var getNewsBySearchRepository: GetNewsBySearchRepository
    lateinit var getCommentsRepository: GetCommentsRepository
    lateinit var getAvatarsRepository: GetAvatarsRepository
    lateinit var addCommentRepository: AddCommentRepository
    lateinit var getNewsByTagRepository: GetNewsByTagRepository
    lateinit var generateUserTopicRepository: GenerateUserTopicRepository

    @SuppressLint("StaticFieldLeak")
    private lateinit var splashDialog: SplashDialog

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

    fun getAvatarImageUrl(imageName: String) : String {
        imageUrl = "${Singleton.SERVICE_URL}${Singleton.AVATAR_IMAGE_PATH}$imageName"
        return imageUrl
    }

    fun getUserImageUrl(imageName: String) : String {
        imageUrl = "${Singleton.SERVICE_URL}${Singleton.USER_IMAGE_PATH}$imageName"
        return imageUrl
    }

    fun getEditNumberByString(number: Int) : String {
        var numberS: String = "$number"

        if (number < 10)
            numberS = "0$number"

        return numberS
    }

    fun getTagList(tags: String) : ArrayList<Tag> {
        val tagList: ArrayList<Tag> = ArrayList()
        val tagArr: List<String> = tags.split(",")

        for (tag in tagArr)
            tagList.add(Tag(tag))

        return tagList
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context) : String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getDeviceName() : String {
        return android.os.Build.MODEL
    }

    fun getDeviceVersion() : String {
        return android.os.Build.VERSION.RELEASE
    }

    fun getUrl(url: String, replacement: String = "-") = Normalizer
        .normalize(url, Normalizer.Form.NFD).lowercase().replace("ı","i")
        .replace("İ","i")
        .replace("ü","u")
        .replace("ü","u")
        .replace("Ü","u")
        .replace("Ğ","g")
        .replace("ğ","g")
        .replace("Ç","c")
        .replace("ç","c")
        .replace("[^\\p{ASCII}]".toRegex(), "")
        .replace("[^a-zA-Z0-9\\s]+".toRegex(), "").trim()
        .replace("\\s+".toRegex(), replacement)

    fun shareNews(url: String, context: Context){
        shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        context.startActivity(shareIntent)
    }

    fun shareWithSocialMedia(url: String, context: Context, socialPackage: String, socialUrl: String) {
        if (checkAppInstall(socialPackage, context)){
            shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setPackage(socialPackage)
            shareIntent.type = "text/plain"
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        } else{
            shareIntent = Intent(Intent.ACTION_VIEW)
            shareIntent.data = Uri.parse(socialUrl)
        }

        context.startActivity(shareIntent)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun checkAppInstall(socialPackage: String, context: Context): Boolean {
        val pm: PackageManager = context.packageManager

        try {
            for (info in pm.getInstalledPackages(0)){
                if (info.packageName.equals(socialPackage))
                    return true
            }

            return true
        } catch (e: PackageManager.NameNotFoundException) {
            println("Err: ${e.localizedMessage}")
        }

        return false
    }

    fun showSplashDialog(mContext: Context){
        splashDialog = SplashDialog(mContext)
        splashDialog.setCancelable(false)
        splashDialog.show()
    }

    fun closeSplashDialog(){
        if (splashDialog.isShowing)
            splashDialog.dismiss()
    }
}