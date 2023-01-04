package com.example.tiktaktoe.datasource


class DataSource {
    val board = mutableListOf(" "," "," "," "," "," "," "," "," ")

    fun loadBoardList(): List<String> {
        return board
    }

    fun clearBoard(){
        board.clear()
        var i =0
        while(i<9){
            board.add(" ")
            i+=1
        }
    }

    fun changeXorY(index:Int,xorY :String):Boolean{
        if(board[index]!="X" && board[index]!="0"){
            board[index]=xorY
            return true
        }else{
            return false
        }
    }


}