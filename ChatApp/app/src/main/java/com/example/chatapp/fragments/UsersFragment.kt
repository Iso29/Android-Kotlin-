package com.example.chatapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.chatapp.R
import com.example.chatapp.adapters.UsersAdapter
import com.example.chatapp.databinding.FragmentUsersBinding
import com.example.chatapp.listeners.UserListener
import com.example.chatapp.models.User
import com.example.chatapp.utilities.Constants
import com.example.chatapp.utilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore

class UsersFragment : Fragment() , UserListener{
    private lateinit var binding : FragmentUsersBinding
    private lateinit var preferenceManager: PreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(inflater,container,false)
        preferenceManager= PreferenceManager(requireContext())
        binding.imageBack.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.fromUsersToHome)
        }

        getUsers()
        return binding.root
    }

    private fun getUsers(){
        loading(true)
        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS).get()
            .addOnCompleteListener {
                loading(false)
                val currentUserId = preferenceManager.getString(Constants.KEY_USER_ID)
                if(it.isSuccessful && it.result !=null){
                    val users = ArrayList<User>()
                    for(queryDocumentSnapshot in it.result ){
                        if(currentUserId.equals(queryDocumentSnapshot.id)){
                            continue
                        }
                        val user = User(queryDocumentSnapshot.id,
                            queryDocumentSnapshot.getString(Constants.KEY_NAME)!!,
                            queryDocumentSnapshot.getString(Constants.KEY_IMAGE)!!,
                            queryDocumentSnapshot.getString(Constants.KEY_EMAIL)!!,
                            queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN))
                        users.add(user)
                    }
                    if(users.size>0){
                        val userAdapter = UsersAdapter(requireContext(),users,this)
                        binding.usersRv.adapter=userAdapter
                        binding.usersRv.visibility=View.VISIBLE
                     }else{
                         showErrorMessage()
                     }
                }else{
                    showErrorMessage()
                }
            }
    }

    private fun showErrorMessage(){
        binding.textErrorMessage.text=String.format("%s","no user available")
        binding.textErrorMessage.visibility=View.VISIBLE
    }

    private fun loading(isLoading:Boolean){
        if(isLoading){
            binding.progressBarUsers.visibility=View.VISIBLE
        }
        else{
            binding.progressBarUsers.visibility=View.INVISIBLE
        }
    }

    override fun onUserCliked(user: User) {
        val intent = UsersFragmentDirections.fromUsersToChat(user = user)
        Navigation.findNavController(requireView()).navigate(intent)
    }
}