package com.example.tiktaktoe.adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktaktoe.databinding.PvpRvLayoutBinding
import com.example.tiktaktoe.viewModel.PVPViewModel

class PVPAdaptor(var mContext: Context, var boardList : List<String>, var viewModel : PVPViewModel )
    : RecyclerView.Adapter<PVPAdaptor.XorYLayoutHolder>(){

    inner class XorYLayoutHolder(var binding: PvpRvLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XorYLayoutHolder {
        val binding = PvpRvLayoutBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return XorYLayoutHolder(binding)
    }

    override fun onBindViewHolder(holder: XorYLayoutHolder, position: Int) {
        val b = holder.binding
        b.textViewXorY.text=boardList.get(position)

        b.CardViewPvp.setOnClickListener {
            if(viewModel.firstPlayer){
                viewModel.changeXorY(position,"0")
            }else{
                viewModel.changeXorY(position,"X")
            }
        }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

}