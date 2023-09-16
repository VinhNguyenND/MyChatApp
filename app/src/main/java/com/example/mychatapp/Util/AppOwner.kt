package com.example.mychatapp.Util

import android.annotation.SuppressLint
import android.content.Context

class AppOwner(context:Context) {
    private val mContext=context
    private val sharedPref = mContext.getSharedPreferences("Ui.LoginSignup", Context.MODE_PRIVATE)
    fun getUid(): String? =sharedPref!!.getString("Uid","")
    fun getMyEmail():String?=sharedPref!!.getString("Email","")
}