package com.finans7.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.finans7.model.Avatar
import com.finans7.model.comment.CommentPostModel
import com.finans7.model.comment.RootComment
import com.finans7.model.favorite.CommentFavoriteResponse
import com.finans7.model.favorite.FavoritePostModel
import com.finans7.repository.AddCommentRepository
import com.finans7.repository.GetAvatarsRepository
import com.finans7.repository.GetCommentsRepository
import com.finans7.repository.UpdateFavoriteCommentRepository
import com.finans7.util.AppUtil
import com.finans7.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class CommentsViewModel(application: Application) : BaseViewModel(application) {
    val avatarList = MutableLiveData<List<Avatar>>()
    val rootComment = MutableLiveData<RootComment>()
    val commentFavoriteResponse = MutableLiveData<CommentFavoriteResponse>()

    fun getAvatars(){
        AppUtil.getAvatarsRepository = GetAvatarsRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.getAvatarsRepository.getAvatars()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Avatar>>(){
                    override fun onSuccess(t: List<Avatar>) {
                        avatarList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun getCommentList(postId: Int, skip: Int, sortType: Int, userId: String){
        AppUtil.getCommentsRepository = GetCommentsRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.getCommentsRepository.getComments(postId, skip, sortType, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RootComment>(){
                    override fun onSuccess(t: RootComment) {
                        rootComment.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun addComment(comment: CommentPostModel){
        AppUtil.addCommentRepository = AddCommentRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.addCommentRepository.addComment(comment)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseBody>(){
                    override fun onSuccess(t: ResponseBody) {
                        successMessage.value = "Yorumunuz başarılı bir şekilde bize ulaşmıştır, onaylandıktan sonra yayınlanacaktır"
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