package com.example.wpclone.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wpclone.ui.adapter.ChatListAdapter
import com.example.wpclone.databinding.FragmentChatBinding
import com.example.wpclone.model.ChatList
import com.example.wpclone.ui.adapter.MessageAdapter
import com.example.wpclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore


class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatList : ArrayList<ChatList>
    private var adapter : ChatListAdapter? = null
    private lateinit var reference : DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        init()

        return binding.root
    }

    private fun init(){
        reference = FirebaseDatabase.getInstance().getReference()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        firestore = FirebaseFirestore.getInstance()
        chatList = ArrayList()
        getChatList()
    }

    private fun getChatList(){
        recyclerIsLoaded(true)
        reference.child(Constants.chatList)
            .child(firebaseUser.uid).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snp in snapshot.children){
                        val userId = snp.child(Constants.chatId).value.toString()
                        getUserData(userId)
                    }
                    if(adapter!=null){
                        adapter!!.notifyDataSetChanged()
                    }
                    else{
                        Log.e("List",chatList.toString())
                        adapter = ChatListAdapter(requireContext(),chatList)
                        binding.chatsRv.adapter = adapter
                    }
                    recyclerIsLoaded(false)
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun getUserData(userId:String){
        firestore.collection(Constants.userCollection)
            .whereEqualTo(Constants.userId,userId).get().addOnSuccessListener {
                val ref = it.documents.get(0)
                val chatItem = ChatList(ref.getString(Constants.userId)!!
                    ,ref.getString(Constants.userName)!!
                    ,""
                    ,""
                    ,ref.getString(Constants.imageProfile)!!)
                chatList.add(chatItem)
            }
    }

    private fun recyclerIsLoaded(loading:Boolean){
        binding.apply {
            if(loading){
                linearNoFriend.visibility = View.VISIBLE
                chatsRv.visibility = View.GONE
            }else{
                linearNoFriend.visibility = View.GONE
                chatsRv.visibility = View.VISIBLE
            }
        }
    }
}