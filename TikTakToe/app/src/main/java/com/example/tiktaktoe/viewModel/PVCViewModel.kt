package com.example.tiktaktoe.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktaktoe.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PVCViewModel @Inject constructor(var repo :Repository) : ViewModel(){
    var boardList = MutableLiveData<List<String>>()
    var firstPlayer = true

    init {
        loadBoardList()
    }

    fun loadBoardList(){
        boardList.value = repo.loadBoardList()
    }

    fun clearBoard(){
        repo.clearBoard()
        loadBoardList()
    }

    fun changeXorY(index : Int,xOrY : String){
        if(repo.changeXorY(index,xOrY)){
            if(firstPlayer){
                firstPlayer=false
            }else{
                firstPlayer=true
            }
            loadBoardList()
        }

    }

    fun checkWin():Boolean{
        val tempList = repo.loadBoardList()
        if((tempList[0]!=" " && tempList[0]==tempList[1] && tempList[1]==tempList[2]) ||
            (tempList[3]!=" " && tempList[3]==tempList[4] && tempList[4]==tempList[5]) ||
            (tempList[6]!=" " && tempList[6]==tempList[7] && tempList[7]==tempList[8])){
            return true
        }else if((tempList[0]!=" " && tempList[0]==tempList[3] && tempList[3]==tempList[6]) ||
            (tempList[1]!=" " && tempList[1]==tempList[4] && tempList[4]==tempList[7]) ||
            (tempList[2]!=" " && tempList[2]==tempList[5] && tempList[5]==tempList[8])){
            return true
        }
        else if((tempList[0]!=" " && tempList[0]==tempList[4] && tempList[4]==tempList[8]) ||
            (tempList[2]!=" " && tempList[2]==tempList[4] && tempList[4]==tempList[6])){
            return true
        }
        else{
            return false
        }
    }

    fun satelment():Boolean{
        val tempList = repo.loadBoardList()
        var count =0
        for((index,laybel) in tempList.withIndex()){
            if(laybel!=" "){
                count++
            }
        }
        if(count==9){
            return true
        }
        else{
            return false
        }
    }

}