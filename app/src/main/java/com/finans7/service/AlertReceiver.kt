package com.finans7.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.navigation.NavDirections
import com.finans7.util.AppUtil
import com.finans7.util.Singleton
import com.finans7.view.CommentsFragmentDirections
import com.finans7.view.MainFragmentDirections
import com.finans7.view.NewsByCategoryFragmentDirections
import com.finans7.view.NewsFragmentDirections

class AlertReceiver : BroadcastReceiver() {
    private lateinit var navDirections: NavDirections
    private lateinit var notificationNewsId: String
    private lateinit var notificationType: String
    private var message: String? = null

    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let {
            notificationNewsId = p1.getStringExtra("newsId")!!
            notificationType = p1.getStringExtra("newsType")!!
            message = p1.getStringExtra("message")

            setNotificationProcess(notificationNewsId, notificationType, if (message != null) message!! else "Yeni haber eklendi görmek ister misin?")
        }
    }

    private fun setNotificationProcess(newsId: String, type: String, alertMessage: String) {
        if (type.equals("1")){
            when (Singleton.currentPage){
                "Main" -> navDirections = MainFragmentDirections.actionMainFragmentToNewsFragment(intArrayOf(newsId.toInt()), 0)
                "Category" -> navDirections = NewsByCategoryFragmentDirections.actionNewsByCategoryFragmentToNewsFragment(intArrayOf(newsId.toInt()), 0)
                "Post" -> navDirections = NewsFragmentDirections.actionNewsFragmentSelf(intArrayOf(newsId.toInt()), 0)
                "Comments" -> navDirections = CommentsFragmentDirections.actionCommentsFragmentToNewsFragment(intArrayOf(newsId.toInt()), 0)
            }

            Singleton.showAlertDialog("Yeni Haber", alertMessage, navDirections)
        } else
            AppUtil.openWebUrl(newsId, Singleton.mContext)
    }
}