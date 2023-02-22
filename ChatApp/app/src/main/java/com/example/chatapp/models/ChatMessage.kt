package com.example.chatapp.models

import java.util.Date

data class ChatMessage(var senderId :String?,
                       var receivedId : String?,
                       var message:String?,
                       var dateTime : String?,
                       var dateObject:Date?,
                       var conversionId : String?,
                       var conversionName : String?,
                       var conversionIMage : String?) {
}