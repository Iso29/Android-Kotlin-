package com.example.wpclone.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wpclone.databinding.ContactItemBinding
import com.example.wpclone.model.User
import com.example.wpclone.ui.activity.ChatActivity
import com.makeramen.roundedimageview.RoundedImageView

class ContactAdapter(val mContext: Context,val contactList : List<User>)
    :RecyclerView.Adapter<ContactAdapter.ContactItemViewHolder>(){
    inner class ContactItemViewHolder(val binding : ContactItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactItemViewHolder {
        var binding = ContactItemBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return ContactItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactItemViewHolder, position: Int) {
        val b = holder.binding
        val user = contactList.get(position)
        b.userNameTextViewContact.text = user.userName
        if(!user.bio.isEmpty()){
            b.bioTextViewContact.text = user.bio
        }
        if(!user.imageProfile!!.isEmpty()){
            placeWithGlide(user.imageProfile!!,b.profileImageViewContact)
        }
        b.root.setOnClickListener {
            mContext.startActivity(Intent(mContext,ChatActivity::class.java).putExtra("user",user))
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    private fun placeWithGlide(imageUri:String,roundedImageView: RoundedImageView){
        Glide.with(mContext)
            .load(imageUri)
            .into(roundedImageView)
    }
}