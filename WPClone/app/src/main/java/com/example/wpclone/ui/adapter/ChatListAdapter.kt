package com.example.wpclone.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wpclone.databinding.ChatItemBinding
import com.example.wpclone.model.ChatList

class ChatListAdapter(val mContext: Context,val chatList:List<ChatList>)
    : RecyclerView.Adapter<ChatListAdapter.ChatItemViewHolder>(){
    inner class ChatItemViewHolder(var binding: ChatItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        var binding = ChatItemBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return ChatItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val b = holder.binding
        val chat = chatList.get(position)
        b.dateChatItemTextView.text = chat.date
        b.chatItemUserNameTextView.text = chat.userName
        b.chatItemDescTextView.text = chat.description
        Glide.with(mContext).load(chat.profileUrl).into(b.chatItemImageView)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}