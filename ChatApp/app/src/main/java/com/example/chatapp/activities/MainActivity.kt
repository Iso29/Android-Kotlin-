package com.example.chatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp.R
import com.example.chatapp.fragments.ChatFragment
import com.example.chatapp.models.User
import com.example.chatapp.utilities.Constants
import com.example.chatapp.utilities.PreferenceManager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var documentReferance : DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferenceManager = PreferenceManager(applicationContext)

        val database = FirebaseFirestore.getInstance()
        if(preferenceManager.getString(Constants.KEY_USER_ID)!=null){
            documentReferance = database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID)!!)
        }
        setContentView(R.layout.activity_main)
    }

    override fun onPause() {
        super.onPause()
        val preferenceManager = PreferenceManager(applicationContext)
        if(preferenceManager.getString(Constants.KEY_USER_ID)!=null){
            documentReferance.update(Constants.KEY_AVAILABILITY,0)
        }

    }

    override fun onResume() {
        super.onResume()
        val preferenceManager = PreferenceManager(applicationContext)
        if(preferenceManager.getString(Constants.KEY_USER_ID)!=null) {
            documentReferance.update(Constants.KEY_AVAILABILITY, 1)
        }
    }
}