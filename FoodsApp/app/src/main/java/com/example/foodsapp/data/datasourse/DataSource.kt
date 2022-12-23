package com.example.foodsapp.data.datasourse

import com.example.foodsapp.data.entitiy.CartFood
import com.example.foodsapp.data.entitiy.CartFoodsResponse
import com.example.foodsapp.data.entitiy.Food
import com.example.foodsapp.data.entitiy.FoodsResponse
import com.example.foodsapp.retrofit.FoodDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataSource(var foodDao: FoodDao){
    suspend fun loadAllFoods():FoodsResponse=foodDao.loadAllFoods()
//    suspend fun loadAllFoods(): List<Food> =
//        withContext(Dispatchers.IO){
//            foodDao.loadAllFoods().foods
//        }

    suspend fun getFoodsFromCart(userName: String):List<CartFood> = foodDao.getFoodsFromCart(userName).foods_cart

//    suspend fun getFoodsFromCart(userName:String): List<CartFood> =
//        withContext(Dispatchers.IO){
//            foodDao.getFoodsFromCart(userName).foods
//        }

    suspend fun insertFoodIntoCart(name:String,image:String,price:Int,category:String,orderAmount:Int,userName:String)=
        foodDao.insertFoodIntoCart(name,image,price,category,orderAmount,userName)

    suspend fun deleteFoodFromCart(cartId:Int,userName:String)=
        foodDao.deleteFoodFromCart(cartId,userName)




}