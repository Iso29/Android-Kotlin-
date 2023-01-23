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
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.utilities.Constants
import com.example.chatapp.utilities.PreferenceManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        preferenceManager= PreferenceManager(requireContext())
        loadUserDetails()
        getToken()

        binding.imageSignOut.setOnClickListener {
            signOut()
        }

        binding.fabNewChatt.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.fromHomeToUsers)
        }
        return binding.root
    }
    private fun loadUserDetails(){
        binding.textNameHome.text=preferenceManager.getString(Constants.KEY_NAME)
        val bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        binding.imageProfile.setImageBitmap(bitmap)
    }

    private fun showToast(message:String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnSuccessListener(this::updateToken)
    }

    private fun updateToken(token : String){
        val database = FirebaseFirestore.getInstance()
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
            preferenceManager.getString(Constants.KEY_USER_ID)!!
        )
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
            .addOnSuccessListener { showToast("Token update succesfully") }
            .addOnFailureListener { showToast("unable to update token") }
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
}