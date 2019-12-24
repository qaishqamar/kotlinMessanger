package com.example.kotlinmessenger.Models

class ChatMessage(val id:String, val toId:String, val text:String, val fromId:String, val timeStamp: Long){
    constructor():this("","","","",-1)
}