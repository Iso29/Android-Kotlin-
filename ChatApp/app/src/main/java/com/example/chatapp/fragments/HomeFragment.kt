package com.example.chatapp.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.chatapp.R
import com.example.chatapp.adapters.RecentChatAdapter
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.listeners.ConversionListener
import com.example.chatapp.listeners.UserListener
import com.example.chatapp.models.ChatMessage
import com.example.chatapp.models.User
import com.example.chatapp.utilities.Constants
import com.example.chatapp.utilities.PreferenceManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import java.util.Collections

class HomeFragment : Fragment(),ConversionListener {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var recentChat : ArrayList<ChatMessage>
    private lateinit var database : FirebaseFirestore
    private lateinit var recentChatAdapter : RecentChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        preferenceManager= PreferenceManager(requireContext())
        init()
        loadUserDetails()
        getToken()
        listenConversion()
        binding.imageSignOut.setOnClickListener {
            signOut()
        }

        binding.fabNewChatt.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.fromHomeToUsers)
        }

        return binding.root
    }

    private fun init(){
        recentChat = ArrayList<ChatMessage>()
        recentChatAdapter = RecentChatAdapter(requireContext(),recentChat, this)
        binding.RecentChatRv.adapter = recentChatAdapter
        database = FirebaseFirestore.getInstance()
    }

    private fun loadUserDetails(){
        binding.textNameHome.text=preferenceManager.getString(Constants.KEY_NAME)
        val bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        binding.imageProfile.setImageBitmap(bitmap)
    }

    private fun listenConversion(){
        database.collection(Constants.KEY_COLLECTION_CONVERSION)
            .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
            .addSnapshotListener(eventListener)
        database.collection(Constants.KEY_COLLECTION_CONVERSION)
            .whereEqualTo(Constants.KEY_RECEIVED_ID,preferenceManager.getString(Constants.KEY_USER_ID))
            .addSnapshotListener(eventListener)
    }

    private val eventListener = EventListener<QuerySnapshot>{
        value, error -> if(error!=null){
            return@EventListener
        }
        if(value!=null){
            for(documentChange in value.documentChanges){
                if(documentChange.type==DocumentChange.Type.ADDED){
                    val senderId = documentChange.document.getString(Constants.KEY_SENDER_ID)
                    val receiverId = documentChange.document.getString(Constants.KEY_RECEIVED_ID)
                    if(preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)){
                        val chatMessage = ChatMessage(preferenceManager.getString(Constants.KEY_USER_ID)
                            ,receiverId
                            ,documentChange.document.getString(Constants.KEY_LAST_MESSAGE)
                            ,null
                            ,documentChange.document.getDate(Constants.KEY_TIMESTAMP)
                            ,documentChange.document.getString(Constants.KEY_RECEIVED_ID)
                            ,documentChange.document.getString(Constants.KEY_RECEIVER_NAME)
                            ,documentChange.document.getString(Constants.KEY_RECEIVER_IMAGE))
                        recentChat.add(chatMessage)
                    }else{
                        val chatMessage = ChatMessage(senderId
                            ,preferenceManager.getString(Constants.KEY_USER_ID)
                            ,documentChange.document.getString(Constants.KEY_LAST_MESSAGE)
                            ,null
                            ,documentChange.document.getDate(Constants.KEY_TIMESTAMP)
                            ,documentChange.document.getString(Constants.KEY_SENDER_ID)
                            ,documentChange.document.getString(Constants.KEY_SENDER_NAME)
                            ,documentChange.document.getString(Constants.KEY_SENDER_IMAGE))
                        recentChat.add(chatMessage)
                    }
                }else if(documentChange.type == DocumentChange.Type.MODIFIED){
                    for( e in recentChat){
                        val senderId = documentChange.document.getString(Constants.KEY_SENDER_ID)
                        val receiverId = documentChange.document.getString(Constants.KEY_RECEIVED_ID)
                        if(e.senderId.equals(senderId) && e.receivedId.equals(receiverId)){
                            e.message = documentChange.document.getString(Constants.KEY_LAST_MESSAGE)
                            e.dateObject = documentChange.document.getDate(Constants.KEY_TIMESTAMP)
                            break
                        }
                    }
                }
            }
            Collections.sort(recentChat,{obj1,obj2 -> obj2.dateObject!!.compareTo(obj1.dateObject)})
            recentChatAdapter.notifyDataSetChanged()
            binding.RecentChatRv.smoothScrollToPosition(0)
            binding.RecentChatRv.visibility = View.VISIBLE
            binding.progressBarHome.visibility = View.GONE
        }


    }


    private fun showToast(message:String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnSuccessListener(this::updateToken)
    }

    private fun updateToken(token : String){
        preferenceManager.putString(Constants.KEY_FCM_TOKEN,token)
        val database = FirebaseFirestore.getInstance()
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
            preferenceManager.getString(Constants.KEY_USER_ID)!!
        )
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
//            .addOnSuccessListener { showToast("Token update succesfully") }
//            .addOnFailureListener { showToast("unable to update token") }
    }

    private fun signOut(){
        showToast("signing out...")
        val database = FirebaseFirestore.getInstance()
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
            preferenceManager.getString(Constants.KEY_USER_ID)!!
        )
        val updates = HashMap<String,Any>()
        updates.put(Constants.KEY_FCM_TOKEN,FieldValue.delete())
        documentReference.update(updates)
            .addOnSuccessListener { preferenceManager.clear()
            Navigation.findNavController(requireView()).navigate(R.id.fromHomeToSignIn)}
            .addOnFailureListener {
                showToast("unable to sign out")
            }
    }

    override fun onConversionClicked(user: User) {
        val intent = HomeFragmentDirections.fromHomeToChat(user = user)
        Navigation.findNavController(requireView()).navigate(intent)
    }
}