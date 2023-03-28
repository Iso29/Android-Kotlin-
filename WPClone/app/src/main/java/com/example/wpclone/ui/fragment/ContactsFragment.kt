package com.example.wpclone.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wpclone.R
import com.example.wpclone.databinding.FragmentContactsBinding
import com.example.wpclone.model.User
import com.example.wpclone.ui.adapter.ContactAdapter
import com.example.wpclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class ContactsFragment : Fragment() {
    private lateinit var binding : FragmentContactsBinding
    private lateinit var contactList : ArrayList<User>
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater,container,false)
        init()
        return binding.root
    }

    private fun init(){
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        firebaseFirestore = FirebaseFirestore.getInstance()
        binding.apply {
            backArrowImageView.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
        getContactList()
    }

    private fun getContactList(){
        firebaseFirestore.collection(Constants.userCollection)
            .get()
            .addOnSuccessListener {
                contactList = ArrayList()
                for(snapshot in it){
                    val user = User(
                        snapshot.getString(Constants.userId)!!,
                        snapshot.getString(Constants.userName)!!,
                        "",
                        snapshot.getString(Constants.imageProfile)!!,
                        "",
                        "",
                        "",
                        "",
                        snapshot.getString(Constants.bio)!!
                    )
                    if(!user.userId.equals(firebaseUser.uid)){
                        contactList.add(user)
                    }
                }
                contactAdapter = ContactAdapter(requireContext(),contactList)
                binding.contactsRv.adapter = contactAdapter
            }.addOnFailureListener {

            }

    }
}