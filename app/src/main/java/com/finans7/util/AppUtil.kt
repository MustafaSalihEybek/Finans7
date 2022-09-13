package com.finans7.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.TypedValue
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.finans7.api.AppAPI
import com.finans7.model.Tag
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.comment.CommentModel
import com.finans7.model.favorite.CommentFavoriteResponse
import com.finans7.repository.*
import com.finans7.view.FooterFragment
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
    private lateinit var socialIntent: Intent
    private lateinit var transaction: FragmentTransaction

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
    lateinit var updateFavoriteCommentRepository: UpdateFavoriteCommentRepository

    @SuppressLint("StaticFieldLeak")
    private lateinit var splashDialog: SplashDialog

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

    fun goToTwitterPage(context: Context){
        try {
            context.packageManager.getPackageInfo("com.twitter.android", 0)
            socialIntent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=${Singleton.TWITTER_USER_ID}"))
            socialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } catch (e: Exception){
            socialIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/${Singleton.TWITTER_PROFILE_NAME}"))
        }

        context.startActivity(socialIntent)
    }

    fun goToInstagramPage(context: Context){
        try {
            context.packageManager.getPackageInfo("com.instagram.android", 0)
            socialIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/${Singleton.INSTAGRAM_PROFILE_NAME}"))
            socialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } catch (e: Exception){
            socialIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/${Singleton.INSTAGRAM_PROFILE_NAME}"))
        }

        context.startActivity(socialIntent)
    }

    fun loadFooterFragment(frameLayout: Int, manager: FragmentManager, fromMain: Boolean){
        transaction = manager.beginTransaction()
        transaction.replace(frameLayout, FooterFragment(fromMain))
        transaction.commit()
    }

    fun getEditedCommentList(commentList: ArrayList<CommentModel>, selectedCommentData: CommentModel, commentFavoriteResponse: CommentFavoriteResponse) : ArrayList<CommentModel> {
        val comments: ArrayList<CommentModel> = ArrayList()

        for (comment in commentList){
            if (comment.commentid == selectedCommentData.commentid){
                comments.add(
                    CommentModel(
                        comment.commentid,
                        comment.postid,
                        comment.commentauthor,
                        comment.commentdatetext,
                        comment.authoremail,
                        comment.commentcontent,
                        comment.commentapproved,
                        comment.parentid,
                        commentFavoriteResponse.likeCount,
                        commentFavoriteResponse.dislikeCount,
                        comment.userid,
                        comment.avatar,
                        comment.userislike,
                        comment.userisdislike
                    )
                )
            } else
                comments.add(comment)
        }

        return comments
    }

    fun getPostIdList(newsList: List<PostListModel>) : IntArray {
        val idList: ArrayList<Int> = ArrayList()

        for (n in newsList.indices)
            idList.add(newsList.get(n).postid)

        return idList.toIntArray()
    }

    fun openWebUrl(url: String, context: Context){
        val webIntent: Intent = Intent(Intent.ACTION_VIEW)
        webIntent.data = Uri.parse(url)
        context.startActivity(webIntent)
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