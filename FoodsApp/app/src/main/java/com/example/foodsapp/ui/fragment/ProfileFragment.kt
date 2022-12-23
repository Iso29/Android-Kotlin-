package com.example.foodsapp.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.foodsapp.R
import com.example.foodsapp.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        val sharedPref = (activity as AppCompatActivity).getSharedPreferences("user",
            Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        binding.textViewUserId.text="#"+sharedPref.getString("userId",null)
        binding.textViewEmail.text=sharedPref.getString("email",null)

        binding.btnLogout.setOnClickListener {
            editor.apply{
                remove("email")
                apply()
            }
            FirebaseAuth.getInstance().signOut()
            Navigation.findNavController(it).navigate(R.id.fromProfileToLogin)
        }

        return binding.root
    }

}