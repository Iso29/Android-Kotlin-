package com.example.chatapp.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.databinding.ItemContainerRecentChatBinding
import com.example.chatapp.listeners.ConversionListener
import com.example.chatapp.listeners.UserListener
import com.example.chatapp.models.ChatMessage
import com.example.chatapp.models.User

class RecentChatAdapter(var mContext: Context,val chatMessage: List<ChatMessage>,val conversionListener: ConversionListener) : RecyclerView.Adapter<RecentChatAdapter.RecentChatViewHolder>() {

    inner class RecentChatViewHolder(var binding : ItemContainerRecentChatBinding) : RecyclerView.ViewHolder(binding.root){
        fun setData(chatMessage: ChatMessage){
            binding.textRecentMessage.text = chatMessage.message
            binding.imageProfileUsers.setImageBitmap(getConversionImage(chatMessage.conversionIMage!!))
            binding.textNameUsers.text = chatMessage.conversionName
            binding.root.setOnClickListener {
                val user = User(chatMessage.conversionId!!,chatMessage.conversionName!!,chatMessage.conversionIMage,null,null)
                conversionListener.onConversionClicked(user = user)
            }

        }
    }

    private fun getConversionImage(encodedImage : String):Bitmap{
        val bytes = Base64.decode(encodedImage,Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentChatViewHolder {
        val binding = ItemContainerRecentChatBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return RecentChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentChatViewHolder, position: Int) {
        holder.setData(chatMessage.get(position))
    }

    override fun getItemCount(): Int {
        return chatMessage.size
    }
}