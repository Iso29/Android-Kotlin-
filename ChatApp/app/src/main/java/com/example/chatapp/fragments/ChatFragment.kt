package com.example.chatapp.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.chatapp.adapters.ChatAdapter
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.models.ChatMessage
import com.example.chatapp.models.User
import com.example.chatapp.utilities.Constants
import com.example.chatapp.utilities.PreferenceManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFragment : Fragment() {
    private lateinit var binding : FragmentChatBinding
    private lateinit var receivedUser :User
    private lateinit var chatMessages : kotlin.collections.ArrayList<ChatMessage>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var database : FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChatBinding.inflate(inflater,container,false)
        binding.imageBackChat.setOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }
        loadReceivedDetails()
        init()

        binding.layoutSend.setOnClickListener{
            sendMessage()
        }
        listenMessages()
        return binding.root
    }

    private fun init(){
        preferenceManager = PreferenceManager(requireContext())
        chatMessages=ArrayList()
        chatAdapter = ChatAdapter(
            requireContext(),
            chatMessages,
            getBitmapFromEncodedString(receivedUser.image),
            preferenceManager.getString(Constants.KEY_USER_ID)!!)
        binding.chatRv.adapter=chatAdapter
        database= FirebaseFirestore.getInstance()

    }

    private fun sendMessage(){
        val message = HashMap<String,Any>()
        message.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID)!!)
        message.put(Constants.KEY_RECEIVED_ID,receivedUser.id)
        message.put(Constants.KEY_MESSAGE,binding.inputText.text.toString())
        message.put(Constants.KEY_TIMESTAMP,Date())
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message)
        binding.inputText.text=null
    }

    private fun listenMessages(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
            .whereEqualTo(Constants.KEY_RECEIVED_ID,receivedUser.id)
            .addSnapshotListener(eventListener)
        database.collection(Constants.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constants.KEY_SENDER_ID,receivedUser.id)
            .whereEqualTo(Constants.KEY_RECEIVED_ID,preferenceManager.getString(Constants.KEY_USER_ID))
            .addSnapshotListener(eventListener)
    }

    private val eventListener = com.google.firebase.firestore.EventListener<QuerySnapshot>{
        value, error ->if(error!=null){
            return@EventListener
        }
        if(value!=null){
            val count = chatMessages.size
            for(documentChange in value.documentChanges){
                if(documentChange.type==DocumentChange.Type.ADDED){
                    val chatMessage = ChatMessage(documentChange.document.getString(Constants.KEY_SENDER_ID)!!,
                        documentChange.document.getString(Constants.KEY_RECEIVED_ID)!!,
                        documentChange.document.getString(Constants.KEY_MESSAGE),
                        getReadableDateTime(documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!),
                        documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!)
                    chatMessages.add(chatMessage)
                }
            }
            Collections.sort(chatMessages,{object1,object2 -> object1.dateObject.compareTo(object2.dateObject)})
            if(count==0){
                chatAdapter.notifyDataSetChanged()
            }else{
                chatAdapter.notifyItemRangeInserted(chatMessages.size,chatMessages.size)
                binding.chatRv.smoothScrollToPosition(chatMessages.size-1)
            }
            binding.chatRv.visibility=View.VISIBLE
        }
        binding.progressBarChat.visibility=View.GONE
    }

    private fun getBitmapFromEncodedString(encodedProfileImage:String):Bitmap{
        val bytes = Base64.decode(encodedProfileImage,Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }

    private fun loadReceivedDetails(){
        val bundle : ChatFragmentArgs by navArgs()
        receivedUser = bundle.user
        binding.textNameChat.text = receivedUser.name

    }

    private fun getReadableDateTime(date:Date):String{
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
    }


}

