package com.finans7.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FCM"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, "Token: $p0")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        if (p0.notification != null)
            println("Notif Data = ${p0.data}")

        if (p0.data.isNotEmpty())
            println("Data: ${p0.data}")

        println("N Data: ${p0.notification?.body}")
    }
}