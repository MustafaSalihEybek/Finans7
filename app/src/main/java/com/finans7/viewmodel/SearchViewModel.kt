package com.finans7.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.finans7.model.categorynews.RootCategoryNews
import com.finans7.repository.GetNewsBySearchRepository
import com.finans7.util.AppUtil
import com.finans7.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class SearchViewModel(application: Application) : BaseViewModel(application) {
    val rootCategoryNews = MutableLiveData<RootCategoryNews>()

    fun getNewsBySearch(categoryName: String, skip: Int) {
        AppUtil.getNewsBySearchRepository = GetNewsBySearchRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.getNewsBySearchRepository.getNewsBySearch(categoryName, skip)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RootCategoryNews>(){
                    override fun onSuccess(t: RootCategoryNews) {
                        rootCategoryNews.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}