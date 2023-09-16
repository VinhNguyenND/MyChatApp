package com.example.mychatapp.Service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.mychatapp.R
import com.example.mychatapp.Ui.MainActivity
import java.util.Date

class Notify() : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    } }
     @RequiresApi(Build.VERSION_CODES.S)
     fun sendNotify(tittle:String, content:String, bigText:String, channelId:String, context: Context){
         val intent=Intent(context,MainActivity::class.java)
         val pending=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(context,channelId)
            .setSmallIcon(R.drawable.mess_icon)
            .setContentTitle(tittle)
            .setContentText(content)
            .setContentIntent(pending)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

         val notificationManager: NotificationManager =context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         notificationManager.notify(getNotifyId(), builder.build())
    }
    companion object{
       const val  CHANNEL_ID="chanel_mess"
    }
    private fun getNotifyId():Int{
        return  Date().time.toInt()
    }
}