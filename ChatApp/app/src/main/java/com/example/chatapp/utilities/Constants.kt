package com.example.chatapp.utilities

class Constants {
    companion object{
        val KEY_COLLECTION_USERS = "users"
        val KEY_NAME = "name"
        val KEY_EMAIL = "email"
        val KEY_PASSWORD = "password"
        val KEY_PREFERENCE_NAME = "chatAppPreference"
        val KEY_IS_SIGNED = "isSignedIn"
        val KEY_USER_ID = "userId"
        val KEY_IMAGE = "image"
        val KEY_FCM_TOKEN = "fcmToken"
        val KEY_COLLECTION_CHAT = "chat"
        val KEY_SENDER_ID = "senderId"
        val KEY_RECEIVED_ID = "receiverId"
        val KEY_MESSAGE = "message"
        val KEY_TIMESTAMP = "timestamp"
        val KEY_COLLECTION_CONVERSION = "conversion"
        val KEY_SENDER_NAME = "senderName"
        val KEY_RECEIVER_NAME = "receiverName"
        val KEY_SENDER_IMAGE = "senderImage"
        val KEY_RECEIVER_IMAGE = "receiverImage"
        val KEY_LAST_MESSAGE = "lastMessage"
        val KEY_AVAILABILITY = "availability"

        val REMOTE_MSG_AUTHORIZATION = "Authorization"
        val REMOTE_MSG_CONTENT_TYPE = "Content-Type"
        val REMOTE_MSG_DATA = "data"
        val REMOTE_MSG_REGISTRATION_IDS = "registration_ids"

        var remoteMsgHeaders: HashMap<String, String>? = null

        @JvmName("getRemoteMsgHeaders1")
        fun getRemoteMsgHeaders():HashMap<String,String>{
            if(remoteMsgHeaders==null){
                remoteMsgHeaders=HashMap<String,String>()
                remoteMsgHeaders!!.put(REMOTE_MSG_AUTHORIZATION,
                "key=AAAAxFsO_1s:APA91bGiIOdS_yDostsiBGf2j22bgnk6OxQ9wlA6dt3dhPHa1BEQVO2y-zhOc-1xYzgphd9T4HUzRhAq1F-eNBlCXWPHrkBNX71Vr-Fn1dQ-lYGjjycZz6S6GWtgfS5TXHugVTPO0YLw")
                remoteMsgHeaders!!.put(REMOTE_MSG_CONTENT_TYPE,"application/json")
            }
            return remoteMsgHeaders!!
        }
    }


}