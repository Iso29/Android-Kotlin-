package com.example.foodsapp.retrofit


class RetrofitUtil {
    companion object{
        val BASE_URL="http://kasimadalan.pe.hu/foods/"
        fun getFoodDao():FoodDao{
            return RetrofitClient.getClient(BASE_URL).create(FoodDao::class.java)
        }
    }
}