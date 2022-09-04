package com.finans7.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.finans7.model.postdetail.PostDetailModel
import com.finans7.repository.GetPostDetailRepository
import com.finans7.util.AppUtil
import com.finans7.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class NewViewModel(application: Application) : BaseViewModel(application) {
    val postDetailData = MutableLiveData<PostDetailModel>()

    fun getPostDetail(postId: Int, userId: String){
        AppUtil.getPostDetailRepository = GetPostDetailRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.getPostDetailRepository.getPostDetail(postId, userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<PostDetailModel>(){
                    override fun onSuccess(t: PostDetailModel) {
                        postDetailData.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}