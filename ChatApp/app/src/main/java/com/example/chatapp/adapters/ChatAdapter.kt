package com.example.chatapp.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.databinding.ItemContainerReceivedBinding
import com.example.chatapp.databinding.ItemContainerSentMessageBinding
import com.example.chatapp.models.ChatMessage

class ChatAdapter(var mContext: Context,val chatMessages : List<ChatMessage>,var receivedProfileImage : Bitmap,val senderId:String) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    val VIEW_TYPE_SENT =1
    val VIEW_TYPE_RECEIVED =2

    @JvmName("setReceivedProfileImage1")
    fun setReceivedProfileImage (bitmap: Bitmap){
        receivedProfileImage = bitmap
    }

    inner class SendMessageViewHolder (var binding : ItemContainerSentMessageBinding): RecyclerView.ViewHolder(binding.root){
        fun setDetails(message: ChatMessage){
            binding.textMessage.text = message.message
            binding.textMessageTime.text = message.dateTime

        }
    }

    inner class ReceivedMessageViewHolder(var binding : ItemContainerReceivedBinding):RecyclerView.ViewHolder(binding.root){
        fun setDetails(message: ChatMessage,receivedProfileImage : Bitmap){
            if(receivedProfileImage!=null){
                binding.imageProfileChat.setImageBitmap(receivedProfileImage)
            }
            binding.textReceivedMessage.text = message.message
            binding.textReceivedMessageTime.text = message.dateTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==VIEW_TYPE_SENT){
            return SendMessageViewHolder(
                ItemContainerSentMessageBinding.inflate(LayoutInflater.from(mContext),parent,false)
            )
        }else{
            return ReceivedMessageViewHolder(ItemContainerReceivedBinding.inflate(
                LayoutInflater.from(mContext),parent,false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)==VIEW_TYPE_SENT){
            (holder as SendMessageViewHolder).setDetails(chatMessages.get(position))
        }else{
            (holder as ReceivedMessageViewHolder).setDetails(chatMessages.get(position),receivedProfileImage)
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun getItemViewType(position: Int): Int {
        if(chatMessages.get(position).senderId.equals(senderId)) {

            return VIEW_TYPE_SENT
        }
        else{
            return VIEW_TYPE_RECEIVED
        }
    }


}