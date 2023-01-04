package com.example.tiktaktoe.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktaktoe.databinding.PvcRvLayoutBinding
import com.example.tiktaktoe.viewModel.PVCViewModel

class PVCAdaptor(var mContext: Context, var boardList : List<String>, var viewModel : PVCViewModel)
    : RecyclerView.Adapter<PVCAdaptor.PVCLayoutHolder>(){

    inner class PVCLayoutHolder(var binding: PvcRvLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PVCLayoutHolder {
        val binding = PvcRvLayoutBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return PVCLayoutHolder(binding)
    }

    override fun onBindViewHolder(holder: PVCLayoutHolder, position: Int) {
        val b = holder.binding
        b.textViewLabelPVC.text=boardList.get(position)

        b.cardViewPVC.setOnClickListener {
            if(viewModel.firstPlayer){
                viewModel.changeXorY(position,"0")
            }
        }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

}