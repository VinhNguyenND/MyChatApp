package com.example.mychatapp.Model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.*

@Parcelize
data class ChatListModel(
    var chatId:String?=null,
    var idOne:String?=null,
    var nameOne:String?=null,
    var imgOne:String?=null,
    var idTwo:String?=null,
    var nameTwo:String?=null,
    var imgTwo:String?=null,
    var lastMessOne:String?=null,
    @ServerTimestamp
    var timeOne: Date?=null,
    ):Parcelable {

    }