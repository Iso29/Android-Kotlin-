package com.example.wpclone.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wpclone.R
import com.example.wpclone.model.Message
import com.google.firebase.auth.FirebaseUser

class MessageAdapter(val mContext: Context, val messageList : List<Message>,val firebaseUser: FirebaseUser)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val MSG_TYPE_RECEIVED = 0
    private val MSG_TYPE_SEND = 1


    inner class ViewHolder( itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(message: Message){
            val messageTextView = itemView.findViewById<TextView>(R.id.messageTextView)
            messageTextView.text = message.textMessage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType==MSG_TYPE_RECEIVED){
            val view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_receive,parent,false)
            return ViewHolder(view)
        }else{
           val view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_send,parent,false)
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.bind(messageList.get(position))
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        if(messageList.get(position).senderId.equals(firebaseUser.uid)){
            return MSG_TYPE_SEND
        }else{
            return MSG_TYPE_RECEIVED
        }
    }
}