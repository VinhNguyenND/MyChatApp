package com.example.mychatapp.Service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.mychatapp.R
import com.example.mychatapp.Ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.RemoteMessage.Notification
import okhttp3.internal.notify
import java.util.*

class NotifyService(): FirebaseMessagingService() {
    private val notify=Notify()
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notification: Notification = message.notification ?: return
        Log.d("notify nay:",notification.title.toString())
        notify.sendNotify(notification.title.toString()
            ,notification.body.toString()
            ,notification.title.toString()
            ,Notify.CHANNEL_ID
            ,this
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}