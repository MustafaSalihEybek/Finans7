package com.finans7.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.finans7.R
import com.finans7.model.categorynews.PostListModel
import com.finans7.model.homepage.News
import com.google.android.material.snackbar.Snackbar

fun String.show(v: View, msg: String){
    Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
}

fun ImageView.downloadImageUrl(imageUrl: String?){
    val options = RequestOptions()
        //.placeholder(placeHolderProgress(context))
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(imageUrl)
        .into(this)
}

/*fun placeHolderProgress(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}*/

@BindingAdapter("android:downloadImg")
fun downloadNewImage(view: ImageView, imageName: String?){
    imageName?.let {
        view.downloadImageUrl(AppUtil.getPostImageUrl(it))
    }
}

@BindingAdapter("android:setNewsDate")
fun setNewsDate(view: TextView, news: News){
    if (news.INSERTDAY != null && news.INSERTMONTH != null && news.INSERTYEAR != null)
        view.text = "${AppUtil.getEditNumberByString(news.INSERTDAY)}.${AppUtil.getEditNumberByString(news.INSERTMONTH)}.${AppUtil.getEditNumberByString(news.INSERTYEAR)} ${news.POSTTIME}"
}

@BindingAdapter("android:setPostDate")
fun setPostDate(view: TextView, post: PostListModel){
    view.text = "${AppUtil.getEditNumberByString(post.insertday.toInt())}.${AppUtil.getEditNumberByString(post.insertmonth.toInt())}.${AppUtil.getEditNumberByString(post.insertyear.toInt())} ${post.posttime}"
}