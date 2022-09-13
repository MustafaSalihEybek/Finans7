package com.finans7.service

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
    private lateinit var dataIntent: Intent

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        println("New Token: $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        p0.notification?.let {
            notificationData = p0.data
            notificationNewsId = notificationData.get("Bildirim_Id").toString()
            notificationType = notificationData.get("Bildirim_Tipi").toString()

            dataIntent = Intent()
            dataIntent.action = "com.finans7"
            dataIntent.putExtra("newsId", notificationNewsId)
            dataIntent.putExtra("newsType", notificationType)
            dataIntent.putExtra("message", it.title)

            sendBroadcast(dataIntent)
        }
    }
}