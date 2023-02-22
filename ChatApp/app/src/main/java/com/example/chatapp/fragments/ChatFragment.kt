package com.example.chatapp.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.chatapp.adapters.ChatAdapter
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.models.ChatMessage
import com.example.chatapp.models.User
import com.example.chatapp.network.ApiClient
import com.example.chatapp.network.ApiService
import com.example.chatapp.utilities.Constants
import com.example.chatapp.utilities.PreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFragment : Fragment() {
    private lateinit var binding : FragmentChatBinding
    private lateinit var receivedUser :User
    private lateinit var chatMessages : ArrayList<ChatMessage>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var database : FirebaseFirestore
    private var conversionId: String? = null
    private var isReceiverAvailable = false

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
        listenAvailabilityOfReceiver()
        return binding.root
    }

    private fun init(){
        preferenceManager = PreferenceManager(requireContext())
        chatMessages=ArrayList()
        chatAdapter = ChatAdapter(
            requireContext(),
            chatMessages,
            getBitmapFromEncodedString(receivedUser.image!!),
            preferenceManager.getString(Constants.KEY_USER_ID)!!)
        binding.chatRv.adapter=chatAdapter
        database= FirebaseFirestore.getInstance()

    }

    private fun addConversion(conversion : kotlin.collections.HashMap<String,Any>){
        database.collection(Constants.KEY_COLLECTION_CONVERSION)
            .add(conversion)
            .addOnSuccessListener{
                conversionId = it.id
            }

    }

    private fun updateConversion(message:String){
        val documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSION).document(conversionId!!)
        documentReference.update(Constants.KEY_LAST_MESSAGE,message,Constants.KEY_TIMESTAMP,Date())
    }

    private fun sendMessage(){
        val message = HashMap<String,Any>()
        message.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID)!!)
        message.put(Constants.KEY_RECEIVED_ID,receivedUser.id)
        message.put(Constants.KEY_MESSAGE,binding.inputText.text.toString())
        message.put(Constants.KEY_TIMESTAMP,Date())
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message)
        if(conversionId!=null){
            updateConversion(binding.inputText.text.toString())
        }else{
            val conversion = kotlin.collections.HashMap<String,Any>()
            conversion.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID)!!)
            conversion.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_NAME)!!)
            conversion.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_IMAGE)!!)
            conversion.put(Constants.KEY_RECEIVED_ID,receivedUser.id)
            conversion.put(Constants.KEY_RECEIVER_NAME,receivedUser.name)
            conversion.put(Constants.KEY_RECEIVER_IMAGE,receivedUser.image!!)
            conversion.put(Constants.KEY_LAST_MESSAGE,binding.inputText.text.toString())
            conversion.put(Constants.KEY_TIMESTAMP,Date())
            addConversion(conversion)
        }
        //
        if(!isReceiverAvailable){
            try {
                var tokens = JSONArray()
                tokens.put(receivedUser.token)

                val data = JSONObject()
                data.put(Constants.KEY_USER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                data.put(Constants.KEY_NAME,preferenceManager.getString(Constants.KEY_NAME))
                data.put(Constants.KEY_IMAGE,preferenceManager.getString(Constants.KEY_IMAGE))
                data.put(Constants.KEY_FCM_TOKEN,preferenceManager.getString(Constants.KEY_FCM_TOKEN))
                data.put(Constants.KEY_MESSAGE,preferenceManager.getString(Constants.KEY_MESSAGE))

                val body = JSONObject()
                body.put(Constants.REMOTE_MSG_DATA,data)
                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens)

                sendNotification(body.toString())
            }catch (e : Exception){
               showToast(e.message.toString())
            }
        }
        binding.inputText.text=null
    }

    private fun listenAvailabilityOfReceiver(){
        database.collection(Constants.KEY_COLLECTION_USERS).document(receivedUser.id)
            .addSnapshotListener{value, error->
                if(error!=null){
                    return@addSnapshotListener
                }
                if (value!=null){
                    if(value.getLong(Constants.KEY_AVAILABILITY)!=null){
                        var availability = Objects.requireNonNull(value.getLong(Constants.KEY_AVAILABILITY))!!.toInt()
                        isReceiverAvailable = availability==1
                    }
                    receivedUser.token = value.getString(Constants.KEY_FCM_TOKEN)
                    if(receivedUser.image==null){
                        receivedUser.image = value.getString(Constants.KEY_IMAGE)
                        chatAdapter.setReceivedProfileImage(getBitmapFromEncodedString(receivedUser.image!!)!!)
                        chatAdapter.notifyItemRangeChanged(0,chatMessages.size)
                    }

                }
                if(isReceiverAvailable){
                    binding.textAvailability.visibility=View.VISIBLE
                }else{
                    binding.textAvailability.visibility=View.GONE
                }
            }
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
                        documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!,null,null,null)
                    chatMessages.add(chatMessage)
                }
            }
            Collections.sort(chatMessages,{object1,object2 -> object1.dateObject!!.compareTo(object2.dateObject)})
            if(count==0){
                chatAdapter.notifyDataSetChanged()
            }else{
                chatAdapter.notifyItemRangeInserted(chatMessages.size,chatMessages.size)
                binding.chatRv.smoothScrollToPosition(chatMessages.size-1)
            }
            binding.chatRv.visibility=View.VISIBLE
        }
        binding.progressBarChat.visibility=View.GONE
        if(conversionId==null){
            checkForConversion()
        }
    }

    private fun checkForConversion(){
        if(chatMessages.size!=0){
            checkForConversionRemotly(preferenceManager.getString(Constants.KEY_USER_ID)!!,receivedUser.id)

            checkForConversionRemotly(receivedUser.id,preferenceManager.getString(Constants.KEY_USER_ID)!!)
        }

    }

    private fun checkForConversionRemotly(senderId : String,receiverId: String){
        database.collection(Constants.KEY_COLLECTION_CONVERSION)
            .whereEqualTo(Constants.KEY_SENDER_ID,senderId)
            .whereEqualTo(Constants.KEY_RECEIVED_ID,receiverId)
            .get()
            .addOnCompleteListener(conversionOnCompleteListener)
    }

    private val conversionOnCompleteListener = OnCompleteListener<QuerySnapshot>{
        task -> if(task.isSuccessful && task.result != null && task.result.documents.size>0){
            val documentSnapshot = task.result.documents.get(0)
            conversionId = documentSnapshot.id
        }
    }

    private fun getBitmapFromEncodedString(encodedProfileImage:String):Bitmap{
        if(encodedProfileImage!=null){
            val bytes = Base64.decode(encodedProfileImage,Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        }
        else{
            val bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        }
    }

    private fun loadReceivedDetails(){
        val bundle : ChatFragmentArgs by navArgs()
        receivedUser = bundle.user
        binding.textNameChat.text = receivedUser.name

    }

    private fun getReadableDateTime(date:Date):String{
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
    }

    override fun onResume() {
        super.onResume()
        listenAvailabilityOfReceiver()
    }

    private fun showToast(message:String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun sendNotification(messageBody:String){
        ApiClient.getClient().create(ApiService::class.java).sendMessage(
            Constants.getRemoteMsgHeaders(),
            messageBody
        ).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    try {
                        if(response.body()!=null){
                            val responseJson = JSONObject(response.body())
                            val result = responseJson.getJSONArray("results")
                            if(responseJson.getInt("failure")==1){
                                val error = result.get(0) as (JSONObject)
                                showToast(error.getString("error"))
                                return
                            }
                        }
                    }catch (e:JSONException){
                        e.printStackTrace()
                    }
                }else{
                    showToast("Error: "+response.code())
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                showToast(t.message.toString())
            }
        })
    }

}

