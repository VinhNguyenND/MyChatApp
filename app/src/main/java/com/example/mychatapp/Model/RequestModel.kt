package com.example.mychatapp.Model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class RequestModel(
    var FromId:String="",
    var ReceiveId:String="",
    @ServerTimestamp
    var time: Date?=null,
){
   constructor(FromId:String,ReceiveId:String):this(FromId,ReceiveId,null)
}