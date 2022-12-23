package com.example.foodsapp.data.repository

import com.example.foodsapp.data.datasourse.DataSource
import com.example.foodsapp.data.entitiy.CartFood
import com.example.foodsapp.data.entitiy.CartFoodsResponse
import com.example.foodsapp.data.entitiy.Food
import com.example.foodsapp.data.entitiy.FoodsResponse

class Repository(var dataSource: DataSource) {
//    suspend fun loadAllFoods(): List<Food> =dataSource.loadAllFoods()
    suspend fun loadAllFoods(): FoodsResponse=dataSource.loadAllFoods()

    suspend fun getFoodsFromCart(userName:String) :List<CartFood> =dataSource.getFoodsFromCart(userName)

    suspend fun insertFoodIntoCart(name:String,image:String,price:Int,category:String,orderAmount:Int,userName:String)=
        dataSource.insertFoodIntoCart(name,image,price,category,orderAmount,userName)

    suspend fun deleteFoodFromCart(cartId:Int,userName:String)=dataSource.deleteFoodFromCart(cartId,userName)
}