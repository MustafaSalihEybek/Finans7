package com.finans7.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.finans7.model.homepage.HomePageNews
import com.finans7.repository.GetHomePageNewsRepository
import com.finans7.util.AppUtil
import com.finans7.viewmodel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class HomeViewModel(application: Application) : BaseViewModel(application) {
    val homePageNews = MutableLiveData<HomePageNews>()

    fun getHomePageNews(){
        AppUtil.getHomePageNewsRepository = GetHomePageNewsRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.getHomePageNewsRepository.getHomePageNews()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<HomePageNews>(){
                    override fun onSuccess(t: HomePageNews) {
                        homePageNews.value = t
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }
}