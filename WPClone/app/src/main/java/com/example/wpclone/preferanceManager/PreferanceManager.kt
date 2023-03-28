package com.example.wpclone.preferanceManager

import android.content.Context
import android.content.SharedPreferences

class PreferanceManager(val mContext: Context) {
    private val sharedPreferences : SharedPreferences

    init {
        sharedPreferences = mContext.getSharedPreferences("wp_clone",Context.MODE_PRIVATE)
    }

    public fun getString(key:String):String?{
        return sharedPreferences.getString(key,null)
    }

    public fun setString(key: String,value:String){
        val editor = sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }

    public fun getBoolean(key: String):Boolean{
        return sharedPreferences.getBoolean(key,false)
    }

    public fun setBoolean(key: String,value: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun clear(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}