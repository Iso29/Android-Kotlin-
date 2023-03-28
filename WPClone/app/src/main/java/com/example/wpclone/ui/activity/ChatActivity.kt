package com.example.wpclone.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.wpclone.R
import com.example.wpclone.databinding.ActivityChatBinding
import com.example.wpclone.model.Message
import com.example.wpclone.model.User
import com.example.wpclone.ui.adapter.MessageAdapter
import com.example.wpclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar

class ChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var receiverId : String
    private var messageAdapter: MessageAdapter? = null
    private lateinit var messageList : ArrayList<Message>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        val recievedUser = intent.getSerializableExtra("user") as User
        receiverId = recievedUser.userId
        messageList = ArrayList()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference()

        getChat()

        binding.apply {
            if(recievedUser.imageProfile!=null){
                Glide.with(applicationContext)
                    .load(recievedUser.imageProfile)
                    .placeholder(R.drawable.person_placeholder)
                    .into(userProfileChat)
            }
            usernameTextViewChat.text = recievedUser.userName

            backArrowImageView.setOnClickListener {
                onBackPressed()
            }

            messageEditText.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!s.toString().isEmpty()){
                        michrafonIcFloatingButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24))
                        michrafonIcFloatingButton.setOnClickListener {
                            sendMessage(s.toString())
                        }
                    }else{
                        michrafonIcFloatingButton.setImageDrawable(getDrawable(R.drawable.ic_round_mic_24))
                        michrafonIcFloatingButton.setOnClickListener {
                            Toast.makeText(applicationContext,s.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                }

            })
        }

    }

    private fun getChat(){
        val ref = FirebaseDatabase.getInstance().getReference()
        ref.child(Constants.messageCollection).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (snp in snapshot.children){
                    val message = Message (
                        snp.child(Constants.Message_dateTime).value.toString()
                        ,snp.child(Constants.Message).value.toString()
                        ,snp.child(Constants.Message_Type).value.toString()
                        ,snp.child(Constants.senderId).value.toString()
                        ,snp.child(Constants.receiverId).value.toString()
                            )

                    if(message!!.senderId.equals(firebaseUser.uid) && message.receiverId.equals(receiverId)){
                        messageList.add(message)
                    }else if(message!!.senderId.equals(receiverId) && message.receiverId.equals(firebaseUser.uid)){
                        messageList.add(message)
                    }
                }
                if(messageAdapter!=null){
                    messageAdapter!!.notifyDataSetChanged()
                }
                else{
                    messageAdapter = MessageAdapter(applicationContext,messageList,firebaseUser)
                    binding.chatRv.adapter = messageAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun sendMessage(message:String){
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val today = formatter.format(date)

        val df = SimpleDateFormat("hh:mm a")
        val currentTime = df.format(Calendar.getInstance().time)

        val msg = Message(
            today+currentTime,
            message.trim(),
            "Text",
            firebaseUser.uid,
            receiverId
        )
        reference.child(Constants.messageCollection)
            .push().setValue(msg)
            .addOnSuccessListener {
                Toast.makeText(applicationContext,"send success",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(applicationContext,"send fail",Toast.LENGTH_SHORT).show()
            }
        val chatRef1 = FirebaseDatabase.getInstance().getReference(Constants.chatList)
            .child(firebaseUser.uid)
            .child(receiverId)
        chatRef1.child("chatId").setValue(receiverId)

        val chatRef2 = FirebaseDatabase.getInstance().getReference(Constants.chatList)
            .child(receiverId)
            .child(firebaseUser.uid)
        chatRef2.child("chatId").setValue(firebaseUser.uid)
    }
}