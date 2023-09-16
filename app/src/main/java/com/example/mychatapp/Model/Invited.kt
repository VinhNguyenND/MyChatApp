package com.example.mychatapp.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Invited(
    var FromId:String="",
    var ReceiveId:String=""
): Parcelable {
}