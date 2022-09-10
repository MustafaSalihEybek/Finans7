package com.finans7.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.finans7.model.comment.RootComment
import com.finans7.repository.GetCommentsRepository
import com.finans7.util.AppUtil
import com.finans7.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class PostViewModel(application: Application) : BaseViewModel(application) {
    val rootComment = MutableLiveData<RootComment>()

    fun getComments(postId: Int, skip: Int, sortType: Int, userId: String){
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
}