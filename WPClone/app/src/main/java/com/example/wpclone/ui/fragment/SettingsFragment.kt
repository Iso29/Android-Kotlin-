package com.example.wpclone.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.wpclone.R
import com.example.wpclone.databinding.FragmentSettingsBinding
import com.example.wpclone.preferanceManager.PreferanceManager
import com.example.wpclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsFragment : Fragment() {
    private lateinit var preferanceManager: PreferanceManager
    private lateinit var binding : FragmentSettingsBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        init()
        onClick()
        return binding.root
    }

    private fun init(){
        preferanceManager = PreferanceManager(requireContext())
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        getUserInfoOffline()
        getUserInfoOnline()

    }

    private fun onClick(){
        binding.apply {
            profileLn.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.fromSettingsToProfile)
            }
            backArrowImageView.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }

    }

    private fun getUserInfoOffline(){
        binding.userNameTextViewSettings.text = preferanceManager.getString(Constants.userName)
        if(preferanceManager.getString(Constants.imageProfile)!=null){
            binding.userProfileImageViewSettings.setImageBitmap(Constants.convertStringToBitmap(preferanceManager.getString(Constants.imageProfile)!!))
        }
    }

    private fun getUserInfoOnline(){
        firestore.collection(Constants.userCollection)
            .whereEqualTo(Constants.userId,mAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful && it.result.documents.size>0){
                    val user = it.result.documents.get(0)
                    binding.userNameTextViewSettings.text = user.getString(Constants.userName)
                    if(user.getString(Constants.imageProfile)!=null){
                        Glide.with(requireContext())
                            .load(user.getString(Constants.imageProfile))
                            .placeholder(R.drawable.person_placeholder)
                            .into(binding.userProfileImageViewSettings)
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        getUserInfoOffline()
        getUserInfoOnline()
    }

}