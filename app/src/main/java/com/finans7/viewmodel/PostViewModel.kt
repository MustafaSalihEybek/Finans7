package com.finans7.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.finans7.model.favorite.CommentFavoriteResponse
import com.finans7.model.favorite.FavoritePostModel
import com.finans7.model.postdetail.PostDetailModel
import com.finans7.repository.GetPostDetailRepository
import com.finans7.repository.UpdateFavoriteCommentRepository
import com.finans7.util.AppUtil
import com.finans7.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class PostViewModel(application: Application) : BaseViewModel(application) {
    val postDetailModel = MutableLiveData<PostDetailModel>()
    val commentFavoriteResponse = MutableLiveData<CommentFavoriteResponse>()

    fun getPostDetail(postId: Int, userId: String){
        AppUtil.getPostDetailRepository = GetPostDetailRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.getPostDetailRepository.getPostDetail(postId, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PostDetailModel>(){
                    override fun onSuccess(t: PostDetailModel) {
                        postDetailModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun updateFavoriteComment(favoritePostModel: FavoritePostModel){
        AppUtil.updateFavoriteCommentRepository = UpdateFavoriteCommentRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.updateFavoriteCommentRepository.updateFavoriteComment(favoritePostModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CommentFavoriteResponse>(){
                    override fun onSuccess(t: CommentFavoriteResponse) {
                        commentFavoriteResponse.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}