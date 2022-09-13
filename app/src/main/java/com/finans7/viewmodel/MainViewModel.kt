package com.finans7.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.finans7.model.category.RootCategory
import com.finans7.repository.GenerateUserTopicRepository
import com.finans7.repository.GetCategoryListRepository
import com.finans7.util.AppUtil
import com.finans7.viewmodel.base.BaseViewModel
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class MainViewModel(application: Application) : BaseViewModel(application) {
    val categoryList = MutableLiveData<RootCategory>()
    val topicToken = MutableLiveData<String>()

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

    fun generateUserTopic(userPlatform: String, userUnique: String, deviceName: String, deviceVersion: String){
        AppUtil.generateUserTopicRepository = GenerateUserTopicRepository()
        AppUtil.disposable = CompositeDisposable()

        AppUtil.disposable.add(
            AppUtil.generateUserTopicRepository.generateUserTopic(userPlatform, userUnique, deviceName, deviceVersion)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseBody>(){
                    override fun onSuccess(t: ResponseBody) {
                        successMessage.value = "Topic token başarıyla oluşturuldu"
                        topicToken.value = t.string()
                    }

                    override fun onError(e: Throwable) {
                        errorMessage.value = e.localizedMessage
                    }
                })
        )
    }

    fun subscribeTopicFromFirebase(topic: String){
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener {
            println("Başarıyla abone olundu")
        }
    }
}