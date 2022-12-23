package com.example.foodsapp.ui.viewmodel

import android.util.Log
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
class SearchViewModel @Inject constructor(var repository: Repository):ViewModel() {

    var searchedFood = MutableLiveData<List<Food>?>()
    var tempList = mutableListOf<Food>()

    fun searchFood(keyWord:String){
        CoroutineScope(Dispatchers.Main).launch {
            val allFoods=repository.loadAllFoods().foods
            tempList.removeAll(tempList)
            if(!keyWord.isEmpty()){
                for(f in allFoods){
                    if (f.name.lowercase().contains(keyWord)){
                        tempList.add(f)
                    }
                }
            }

            searchedFood.value=tempList
        }
    }

}