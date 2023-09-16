package com.example.mychatapp.Model

data class Account(
    var Email:String?="",
    var Name:String="",
    var PassWord:String="",
    var uid:String="",
    var fcmToken:String=""
) {
}