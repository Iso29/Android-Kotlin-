package com.example.tiktaktoe.repository

import com.example.tiktaktoe.datasource.DataSource

class Repository(var dataSource: DataSource){

    fun loadBoardList() : List<String>{
         return dataSource.loadBoardList()
     }

     fun clearBoard() = dataSource.clearBoard()

    fun changeXorY(index:Int,xorY :String):Boolean = dataSource.changeXorY(index, xorY )
}