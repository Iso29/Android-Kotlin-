package com.example.foodsapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodsapp.data.entitiy.CartFood
import com.example.foodsapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(var repository: Repository) : ViewModel(){
    var cartList = MutableLiveData<List<CartFood>?>()
    fun insertFoodIntoCart(name:String,image:String,price:Int,category:String,orderAmount:Int,userName:String){
        CoroutineScope(Dispatchers.Main).launch {
            repository.insertFoodIntoCart(name,image,price,category,orderAmount,userName)
        }
    }


    fun getCartList(userName: String){
        CoroutineScope(Dispatchers.Main).launch{
            try {
                cartList.value=repository.getFoodsFromCart(userName)
            }catch (e:Exception){

            }
        }
    }

    fun deleteFoodFromCart(cartId:Int,userName: String) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.deleteFoodFromCart(cartId, userName)
        }
    }

}