package com.example.foodsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsapp.data.entitiy.Food
import com.example.foodsapp.data.entitiy.FoodsResponse
import com.example.foodsapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(var repository: Repository) :ViewModel() {
//    var foodList = MutableLiveData<List<Food>>()
    private val _foodsList=MutableLiveData<FoodsResponse>()
    val foodList:LiveData<FoodsResponse>
        get() =_foodsList


    init {
        loadAllFoods()
    }

    fun loadAllFoods(){
        viewModelScope.launch {
            try {
                _foodsList.postValue(repository.loadAllFoods())

            }catch (e:java.lang.Exception){

            }

        }
    }


}