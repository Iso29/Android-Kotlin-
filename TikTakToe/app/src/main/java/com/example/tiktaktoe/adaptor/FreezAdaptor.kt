package com.example.tiktaktoe.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktaktoe.databinding.PvpRvFreezLayoutBinding
import com.example.tiktaktoe.databinding.PvpRvLayoutBinding
import com.example.tiktaktoe.viewModel.PVPViewModel

class FreezAdaptor(var mContext: Context, var boardList : List<String>)
    : RecyclerView.Adapter<FreezAdaptor.FreezLayoutHolder>(){

    inner class FreezLayoutHolder(var binding: PvpRvFreezLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreezLayoutHolder {
        val binding = PvpRvFreezLayoutBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return FreezLayoutHolder(binding)
    }

    override fun onBindViewHolder(holder: FreezLayoutHolder, position: Int) {
        val b = holder.binding
        b.textView2.text=boardList.get(position)

    }

    override fun getItemCount(): Int {
        return boardList.size
    }

}