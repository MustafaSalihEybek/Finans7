package com.finans7.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.finans7.model.categorynews.RootCategoryNews
import com.finans7.repository.GetNewsByCategory
import com.finans7.util.AppUtil
import com.finans7.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class NewsByCategoryViewModel(application: Application) : BaseViewModel(application) {
    val rootCategoryNews = MutableLiveData<RootCategoryNews>()

    fun getNewsByCategory(categoryName: String, skip: Int){
        AppUtil.getNewsByCategory = GetNewsByCategory()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.getNewsByCategory.getNewsByCategory(categoryName, skip)
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