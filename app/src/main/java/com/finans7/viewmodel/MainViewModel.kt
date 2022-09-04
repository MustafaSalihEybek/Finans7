package com.finans7.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.finans7.model.category.RootCategory
import com.finans7.repository.GetCategoryListRepository
import com.finans7.util.AppUtil
import com.finans7.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : BaseViewModel(application) {
    val categoryList = MutableLiveData<RootCategory>()

    fun getCategoryList(){
        AppUtil.getCategoryListRepository = GetCategoryListRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.getCategoryListRepository.getCategoryList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RootCategory>(){
                    override fun onSuccess(t: RootCategory) {
                        categoryList.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}