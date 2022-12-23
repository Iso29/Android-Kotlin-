package com.example.foodsapp.ui.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodsapp.data.entitiy.CartFood
import com.example.foodsapp.data.entitiy.CartFoodsResponse
import com.example.foodsapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(var repository: Repository) :ViewModel(){
    var cartFoodList = MutableLiveData<List<CartFood>?>()

    fun getFoodsFromCart(userName : String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                cartFoodList.value=repository.getFoodsFromCart(userName)
            }catch (e:Exception){
                cartFoodList.value= mutableListOf()
            }
        }
    }

    fun deleteFoodFromCart(cartId:Int,userName: String){
        CoroutineScope(Dispatchers.Main).launch{
            repository.deleteFoodFromCart(cartId,userName)
            getFoodsFromCart(userName)
        }
    }

}