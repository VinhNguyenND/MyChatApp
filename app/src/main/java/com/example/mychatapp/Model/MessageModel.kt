package com.example.mychatapp.Model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class MessageModel(
    var idChat:String="",
    var senderId:String="",
    var receiveId:String="",
    var receiveImage:String="",
    var senderName:String="",
    var message:String="",
    @ServerTimestamp
    var time: Date?=null,
) {
    constructor(idChat:String,image: String, senderId: String, receiveId: String, senderName: String, message: String)
            : this(idChat,image,senderId,receiveId,senderName,message, null)

}