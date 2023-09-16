package com.example.mychatapp.Service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AppService: Service() {
    private  val notifyService=NotifyService()
    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notifyService
        return START_STICKY
    }
}