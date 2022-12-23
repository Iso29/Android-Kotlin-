package com.example.foodsapp.retrofit

import com.example.foodsapp.data.entitiy.CartFoodsResponse
import com.example.foodsapp.data.entitiy.CrudResponse
import com.example.foodsapp.data.entitiy.FoodsResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodDao {
    @GET("getAllFoods.php")
    suspend fun loadAllFoods():FoodsResponse

    @POST("getFoodsCart.php")
    @FormUrlEncoded
    suspend fun getFoodsFromCart(@Field("userName") userName:String="kasim_adalan"): CartFoodsResponse

    @POST("insertFood.php")
    @FormUrlEncoded
    suspend fun insertFoodIntoCart(@Field("name") name:String, @Field("image") image:String, @Field("price")price:Int,
                                   @Field("category")category:String, @Field("orderAmount")orderAmount:Int, @Field("userName")userName:String): CrudResponse

    @POST("deleteFood.php")
    @FormUrlEncoded
    suspend fun deleteFoodFromCart(@Field("cartId")cartId:Int, @Field("userName")userName: String): CrudResponse
}