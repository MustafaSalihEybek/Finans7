package com.finans7.service

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.finans7.util.AppUtil
import com.finans7.util.Singleton
import com.finans7.view.CommentsFragmentDirections
import com.finans7.view.MainFragmentDirections
import com.finans7.view.NewsByCategoryFragmentDirections
import com.finans7.view.NewsFragmentDirections
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var notificationData: Map<String, String>
    private lateinit var notificationNewsId: String
    private lateinit var notificationType: String
    private lateinit var navDirections: NavDirections
    private lateinit var mAlert: AlertDialog.Builder

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        println("New Token: $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        p0.notification?.let {
            notificationData = p0.data
            notificationNewsId = notificationData.get("Bildirim_Id").toString()
            notificationType = notificationData.get("Bildirim_Tipi").toString()

            setNotificationProcess(notificationNewsId, notificationType, if (it.title != null) it.title!! else "Yeni haber eklendi görmek ister misin?")
        }
    }

    fun setNotificationProcess(newsId: String, type: String, alertMessage: String) {
        if (type.equals("1")){
            when (Singleton.currentPage){
                "Main" -> navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(intArrayOf(newsId.toInt()), 0)
                "Category" -> navDirections = NewsByCategoryFragmentDirections.actionNewsByCategoryFragmentToNewsFragment(intArrayOf(newsId.toInt()), 0)
                "Post" -> navDirections = NewsFragmentDirections.actionNewsFragmentSelf(intArrayOf(newsId.toInt()), 0)
                "Comments" -> navDirections = CommentsFragmentDirections.actionCommentsFragmentToNewsFragment(intArrayOf(newsId.toInt()), 0)
            }

            Singleton.showAlertDialog("Yeni Haber", alertMessage, navDirections)
        } else
            AppUtil.openWebUrl(newsId, applicationContext)
    }
}