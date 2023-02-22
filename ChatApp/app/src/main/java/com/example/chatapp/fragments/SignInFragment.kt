package com.example.chatapp.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignInBinding
import com.example.chatapp.utilities.Constants
import com.example.chatapp.utilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore

class SignInFragment : Fragment() {
    private lateinit var binding : FragmentSignInBinding
    private lateinit var preferenceManager : PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSignInBinding.inflate(inflater,container,false)
        preferenceManager= PreferenceManager(requireContext())

        binding.btnSignIn.setOnClickListener {
            if(isValidSignIn()){
                signIn()
            }
        }

        binding.textViewSignUp.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.fromSignInToSignUp)
        }

        return binding.root
    }

    private fun showToast(message : String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun signIn(){
        loading(true)
        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL,binding.editTextEmailSignIn.text.toString())
            .whereEqualTo(Constants.KEY_PASSWORD,binding.editTextPasswordSignIn.text.toString())
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful && it.result!=null
                    && it.result.documents.size>0){
                    val documentSnapshot = it.result.documents.get(0)
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED,true)
                    preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.id)
                    preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME)!!)

                    preferenceManager.putString(Constants.KEY_IMAGE,documentSnapshot.getString(Constants.KEY_IMAGE)!!)

                    Navigation.findNavController(requireView()).navigate(R.id.fromSignInToHome)
                }
                else{
                    loading(false)
                    showToast("Unable to sign in")
                }
            }
    }

    private fun isValidSignIn():Boolean{
        if(binding.editTextEmailSignIn.text.toString().isEmpty()){
            showToast("email can not left empty")
            return false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmailSignIn.text.toString().trim()).matches()){
            showToast("enter valid email")
            return false
        }
        else if(binding.editTextPasswordSignIn.text.toString().isEmpty()){
            showToast("password can not left empty")
            return false
        }
        else{
            return true
        }
    }

    private fun loading(isLoading : Boolean){
        if(isLoading){
            binding.btnSignIn.visibility=View.INVISIBLE
            binding.progressBarSignIn.visibility=View.VISIBLE
        }
        else{
            binding.btnSignIn.visibility=View.VISIBLE
            binding.progressBarSignIn.visibility=View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceManager= PreferenceManager(requireContext())
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED)){
            Navigation.findNavController(requireView()).navigate(R.id.fromSignInToHome)
        }
    }
}