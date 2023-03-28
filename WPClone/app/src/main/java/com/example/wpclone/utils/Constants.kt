package com.example.wpclone.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class Constants {
    companion object{
        val userId = "userId"
        val userCollection = "Users"
        val messageCollection = "Messages"
        val chatList = "ChatList"
        val userName = "userName"
        val userPhone = "userPhone"
        val imageProfile = "imageProfile"
        val imageCover ="imageCover"
        val email = "email"
        val dateOfBirth ="dateOfBirth"
        val gender = "gender"
        val status = "status"
        val bio = "bio"
        val isSignedIn = "isSigned"
        val chatId = "chatId"
        var IMAGE_BITMAP : Bitmap? = null

        val Message_dateTime = "dateTime"
        val Message = "textMessage"
        val Message_Type = "type"
        val senderId = "senderId"
        val receiverId = "receiverId"

        fun convertStringToBitmap(profileImage : String):Bitmap{
            val bytes = Base64.decode(profileImage, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        }

        fun encodedImage(bitmap: Bitmap):String{
            val previewWidth = 150
            val previewHeigth = bitmap.height*previewWidth/bitmap.width
            val previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeigth,false)
            val byteArrayOutputStream = ByteArrayOutputStream()
            previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream)
            val bytes = byteArrayOutputStream.toByteArray()
            return android.util.Base64.encodeToString(bytes,android.util.Base64.DEFAULT)
        }
    }
}