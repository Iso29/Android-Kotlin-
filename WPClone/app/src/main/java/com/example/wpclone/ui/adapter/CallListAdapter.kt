package com.example.wpclone.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wpclone.R
import com.example.wpclone.databinding.CallItemBinding
import com.example.wpclone.model.CallList

class CallListAdapter(val mContext: Context,val callList:ArrayList<CallList>)
    :RecyclerView.Adapter<CallListAdapter.CallListItemHolder>() {
    inner class CallListItemHolder(var binding:CallItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CallListItemHolder {
        val binding = CallItemBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return CallListItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CallListItemHolder, position: Int) {
        val b = holder.binding
        val call = callList.get(position)
        Glide.with(mContext).load(call.profileUrl).into(b.callItemImageView)
        b.callItemUsernameTextView.text = call.userName
        b.callItemDateTextView.text = call.date
        if(call.callType.equals("missed")) {
            b.callItemCallType.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
            b.callItemCallType.drawable.setTint(mContext.resources.getColor(R.color.error))
        }else if(call.callType.equals("income")){
            b.callItemCallType.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
            b.callItemCallType.drawable.setTint(mContext.resources.getColor(R.color.green))
        }else {
            b.callItemCallType.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
            b.callItemCallType.drawable.setTint(mContext.resources.getColor(R.color.green))
        }
    }

    override fun getItemCount(): Int {
        return callList.size
    }
}