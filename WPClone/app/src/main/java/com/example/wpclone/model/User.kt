package com.example.wpclone.model

data class User(var userId:String
                ,var userName:String
                ,var userPhone:String
                ,var imageProfile:String?
                ,var email:String?
                ,var dateOfBirth:String?
                ,var gender:String?
                ,var status:String
                ,var bio:String):java.io.Serializable {
}