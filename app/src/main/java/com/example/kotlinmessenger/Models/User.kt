package com.example.kotlinmessenger.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User( val uid:String,val username:String, val profileImageUrl: String,val aboutUser:String,val email:String,val phoneNo:String):Parcelable{
    constructor():this("","","","","","")
}
