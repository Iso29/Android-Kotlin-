package com.example.wpclone.model

data class Message(var dateTime:String
                    ,var textMessage:String
                   ,var type:String
                   ,var senderId:String
                   ,var receiverId:String):java.io.Serializable {
}