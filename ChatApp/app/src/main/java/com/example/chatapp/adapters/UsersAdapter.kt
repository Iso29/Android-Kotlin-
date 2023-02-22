package com.example.chatapp.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ItemContainerUserBinding
import com.example.chatapp.listeners.UserListener
import com.example.chatapp.models.User

class UsersAdapter (var mContext: Context,val users:List<User>,val userListener: UserListener ):RecyclerView.Adapter<UsersAdapter.UserViewHolder>(){

    inner class UserViewHolder(var binding : ItemContainerUserBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemContainerUserBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users.get(position)
        val b = holder.binding
        b.textNameUsers.text = user.name
        b.textEmailUsers.text = user.email
        b.imageProfileUsers.setImageBitmap(getUserImage(user.image!!))
        b.root.setOnClickListener {
            userListener.onUserCliked(user)
        }


    }

    override fun getItemCount(): Int {
        return users.size
    }

    private fun getUserImage(encodedImage:String):Bitmap{
        val bytes = Base64.decode(encodedImage,Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }
}