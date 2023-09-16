package com.example.mychatapp.Model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var Email: String?=null,
    var Name: String?=null,
    var status:String?=null,
    var image: String?=null,
    val online: String?=null,
  ):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserModel

        if (Email != other.Email) return false
        if (Name != other.Name) return false
        if (status != other.status) return false
        if (image != other.image) return false
        if (online != other.online) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Email.hashCode()
        result = 31 * result + Name.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + online.hashCode()
        return result
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object : Parceler<UserModel> {
        override fun UserModel.write(parcel: Parcel, flags: Int) {
            parcel.writeString(Email)
            parcel.writeString(Name)
            parcel.writeString(status)
            parcel.writeString(image)
            parcel.writeString(online)
        }

        override fun create(parcel: Parcel): UserModel = TODO()
    }

}