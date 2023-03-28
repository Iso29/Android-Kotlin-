package com.example.wpclone.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.wpclone.R
import com.example.wpclone.ui.activity.MainActivity
import com.example.wpclone.ui.activity.SplashActivity
import com.example.wpclone.databinding.ActivityMainBinding
import com.example.wpclone.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private lateinit var binding : FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater,container,false)

        binding.apply {
            buttonContinueButton.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.fromWelcomeToLogin)
            }
        }
        return binding.root
    }

}